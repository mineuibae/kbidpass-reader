package com.kbds.kbidpassreader.util.recyclerview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.kbds.kbidpassreader.base.BaseActivity
import kotlin.math.abs

class RecyclerViewTouchListener(
    private val activity: BaseActivity,
    private val recyclerView: RecyclerView
) : RecyclerView.OnItemTouchListener, OnActivityTouchListener {

    var mForegroundView: View? = null //
    var mBackgroundView: View? = null //
    var mForegroundViewId: Int = -1 //
    var mBackgroundViewId: Int = -1 //

    private var mOptionViewIds: List<Int> = arrayListOf()

    private val viewConfiguration: ViewConfiguration = ViewConfiguration.get(recyclerView.context)
    var mTouchSlop = viewConfiguration.scaledTouchSlop
    var mSwipingSlop = 0
    var mMinFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity * 16
    var mMaxFlingVelocity = viewConfiguration.scaledMaximumFlingVelocity

    var mTouchedView: View? = null //
    var mTouchedPosition: Int = -1 //
    var mTouchedX: Float = 0F //
    var mTouchedY: Float = 0F //

    var mBackgroundVisibleView: View? = null
    var mBackgroundVisiblePosition = -1

    var mPaused = false //
    var mIsRecyclerViewScrolling = false
    var mIsForegroundSwiping = false //
    private var mIsBackgroundVisible = false
    private var mIsForegroundPartialViewClicked = false

    var mScreenHeight: Int = 0 //
    var mRecyclerViewOutsideHeight: Int = 0 //
    var mBackgroundWidth: Int = 1

    var mVelocityTracker: VelocityTracker? = null
    private var mRowClickListener: OnRowClickListener? = null
    var mOptionClickListener: OnOptionClickListener? = null

    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                setEnabled(newState != RecyclerView.SCROLL_STATE_DRAGGING)
                mIsRecyclerViewScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
        })
    }

    fun setEnabled(enabled: Boolean) {
        mPaused = !enabled
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return handleTouchEvent(e)
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        handleTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }

    fun setOnRowClickListener(listener: OnRowClickListener) {
        mRowClickListener = listener
    }

    fun setOnOptionClickListener(listener: OnOptionClickListener) {
        mOptionClickListener = listener
    }

    fun setViewIds(foregroundViewId: Int, backgroundViewId: Int) {
//        if(mForegroundViewId != 0 && foregroundViewId != mForegroundViewId)
//            throw IllegalArgumentException("foregroundID does not match previously set ID")

        mForegroundViewId= foregroundViewId
        mBackgroundViewId = backgroundViewId

        if(activity is RecyclerTouchListenerHelper)
            (activity as RecyclerTouchListenerHelper).setOnActivityTouchListener(this)

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        mScreenHeight = displayMetrics.heightPixels
    }

    fun setOptionViewIds(vararg viewIds: Int) {
        mOptionViewIds = viewIds.asList()
    }

    private fun getOptionViewID(motionEvent: MotionEvent): Int {
        for(i in 0..mOptionViewIds.size) {
            if(mTouchedView != null) {
                val rect = Rect()
                val x = motionEvent.rawX.toInt()
                val y = motionEvent.rawY.toInt()
                mTouchedView?.findViewById<View>(mOptionViewIds[i])?.getGlobalVisibleRect(rect)
                if(rect.contains(x, y)) {
                    return mOptionViewIds[i]
                }
            }
        }

        return -1
    }

    private fun closeBackgroundVisibleView(mOptionSwipeListener: OnOptionSwipeListener?) {
        if(mBackgroundVisibleView == null) {
            return
        }

        val objectAnimator = ObjectAnimator.ofFloat(mBackgroundVisibleView, View.TRANSLATION_X, 0f)
        objectAnimator.duration = ANIMATION_CLOSE
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                mOptionSwipeListener?.onOptionClosed()
                objectAnimator.removeAllListeners()
            }

            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
        objectAnimator.start()

        mIsBackgroundVisible = false
        mBackgroundVisibleView = null
        mBackgroundVisiblePosition = -1
    }

    private fun animateForeground(downView: View, animateType: Animation, duration: Long) {
        if(animateType == Animation.OPEN) {
            val objectAnimator = ObjectAnimator.ofFloat(mForegroundView, View.TRANSLATION_X, -mBackgroundWidth.toFloat())
            objectAnimator.duration = duration
            objectAnimator.interpolator = DecelerateInterpolator(1.5f)
            objectAnimator.start()

        } else if(animateType == Animation.CLOSE) {
            val objectAnimator = ObjectAnimator.ofFloat(mForegroundView, View.TRANSLATION_X, 0f)
            objectAnimator.duration = duration
            objectAnimator.interpolator = DecelerateInterpolator(1.5f)
            objectAnimator.start()
        }
    }

    private fun animateForeground(downView: View, animateType: Animation, duration: Long, mOptionSwipeListener: OnOptionSwipeListener?) {
        val objectAnimator = if(animateType == Animation.OPEN) {
            val objectAnimator = ObjectAnimator.ofFloat(mForegroundView, View.TRANSLATION_X, -mBackgroundWidth.toFloat())
            objectAnimator.duration = duration
            objectAnimator.interpolator = DecelerateInterpolator(1.5f)
            objectAnimator.start()
            objectAnimator

        } else {
            val objectAnimator = ObjectAnimator.ofFloat(mForegroundView, View.TRANSLATION_X, 0f)
            objectAnimator.duration = duration
            objectAnimator.interpolator = DecelerateInterpolator(1.5f)
            objectAnimator.start()
            objectAnimator
        }

        objectAnimator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationEnd(animation: Animator?) {
                if(mOptionSwipeListener != null) {
                    if(animateType == Animation.OPEN) {
                        mOptionSwipeListener.onOptionOpened()

                    } else if(animateType == Animation.CLOSE) {
                        mOptionSwipeListener.onOptionClosed()
                    }
                }

                objectAnimator.removeAllListeners()
            }

            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }


    private fun handleTouchEvent(motionEvent: MotionEvent): Boolean {
        if(mBackgroundWidth < 2) {
            if(activity.findViewById<View>(mBackgroundViewId) != null) {
                mBackgroundWidth = activity.findViewById<View>(mBackgroundViewId).width
            }

            mRecyclerViewOutsideHeight = mScreenHeight - recyclerView.height
        }

        when(motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> run {
                if (mPaused) {
                    return@run
                }

                val recyclerViewCoordinates = IntArray(2)
                recyclerView.getLocationOnScreen(recyclerViewCoordinates)
                var x = (motionEvent.rawX - recyclerViewCoordinates[0]).toInt()
                var y = (motionEvent.rawY - recyclerViewCoordinates[1]).toInt()

                val rect = Rect()
                var child: View
                val childCount = recyclerView.childCount

                loop@ for(i in 0..childCount) {
                    child = recyclerView.getChildAt(i)
                    child.getHitRect(rect)
                    if(rect.contains(x, y)) {
                        mTouchedView = child
                        break@loop
                    }
                }

                mTouchedView?.let {
                    mTouchedX = motionEvent.rawX
                    mTouchedY = motionEvent.rawY
                    mTouchedPosition = recyclerView.getChildAdapterPosition(it)

                    mVelocityTracker = VelocityTracker.obtain()
                    mVelocityTracker?.addMovement(motionEvent)
                    mForegroundView = it.findViewById(mForegroundViewId)
                    mBackgroundView = it.findViewById(mBackgroundViewId)
                    mBackgroundView?.minimumHeight = mForegroundView?.height ?: -1

                    if (mIsBackgroundVisible && mForegroundView != null) {
                        x = motionEvent.rawX.toInt()
                        y = motionEvent.rawY.toInt()
                        mForegroundView?.getGlobalVisibleRect(rect)
                        mIsForegroundPartialViewClicked = rect.contains(x, y)

                    } else {
                        mIsForegroundPartialViewClicked = false
                    }
                }

                recyclerView.getHitRect(rect)
                if (mIsBackgroundVisible && mTouchedPosition != mBackgroundVisiblePosition) {
                    closeBackgroundVisibleView(null)
                }
            }

            MotionEvent.ACTION_CANCEL -> run {
                if (mVelocityTracker == null) {
                    return@run
                }

                if (mTouchedView != null && mIsForegroundSwiping) {
                    animateForeground(mTouchedView!!, Animation.CLOSE, ANIMATION_STANDARD)
                }

                mVelocityTracker?.recycle()
                mVelocityTracker = null
                mIsForegroundSwiping = false
                mBackgroundView = null

                mTouchedX = 0F
                mTouchedY = 0F
                mTouchedView = null
                mTouchedPosition = ListView.INVALID_POSITION
            }

            MotionEvent.ACTION_UP -> run {
                if (mVelocityTracker == null) {
                    return@run
                }

                if (mTouchedPosition < 0)
                    return@run

                var swipedLeft = false
                var swipedRight = false
                var swipedLeftProper = false
                var swipedRightProper = false

                val finalDelta = motionEvent.rawX - mTouchedX

                if (mIsForegroundSwiping) {
                    swipedLeft = finalDelta < 0
                    swipedRight = finalDelta > 0
                }

                if (abs(finalDelta) > mBackgroundWidth / 2 && mIsForegroundSwiping) {
                    swipedLeftProper = finalDelta < 0
                    swipedRightProper = finalDelta > 0

                } else {
                    mVelocityTracker?.addMovement(motionEvent)
                    mVelocityTracker?.computeCurrentVelocity(1000)
                    mVelocityTracker?.let {
                        val absVelocityX = abs(it.xVelocity)
                        val absVelocityY = abs(it.yVelocity)

                        if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity && absVelocityY < absVelocityX && mIsForegroundSwiping) {
                            swipedLeftProper = (it.xVelocity < 0) == (finalDelta < 0)
                            swipedRightProper = (it.xVelocity > 0) == (finalDelta > 0)
                        }
                    }
                }

                if (!swipedRight && swipedLeftProper && mTouchedPosition != RecyclerView.NO_POSITION && !mIsBackgroundVisible) {
                    val downView = mTouchedView
                    val downPosition = mTouchedPosition

                    mTouchedView?.let {
                        animateForeground(it, Animation.OPEN, ANIMATION_STANDARD)
                        mIsBackgroundVisible = true
                        mBackgroundVisibleView = mForegroundView
                        mBackgroundVisiblePosition = downPosition
                    }

                } else if (!swipedLeft && swipedRightProper && mTouchedPosition != RecyclerView.NO_POSITION && mIsBackgroundVisible) {
                    val downView = mTouchedView
                    val downPosition = mTouchedPosition

                    mTouchedView?.let {
                        animateForeground(it, Animation.CLOSE, ANIMATION_STANDARD)
                        mIsBackgroundVisible = false
                        mBackgroundVisibleView = null
                        mBackgroundVisiblePosition = -1
                    }

                } else if (swipedLeft && !mIsBackgroundVisible) {
                    val tempBgView = mBackgroundView

                    mTouchedView?.let {
                        animateForeground(it, Animation.CLOSE, ANIMATION_STANDARD, object : OnOptionSwipeListener {
                            override fun onOptionClosed() {

                            }

                            override fun onOptionOpened() {
                                tempBgView?.visibility = View.VISIBLE
                            }
                        })

                        mIsBackgroundVisible = false
                        mBackgroundVisibleView = null
                        mBackgroundVisiblePosition = -1
                    }

                } else if (swipedRight && mIsBackgroundVisible) {
                    mTouchedView?.let {
                        animateForeground(it, Animation.OPEN, ANIMATION_STANDARD)
                        mIsBackgroundVisible = true
                        mBackgroundVisibleView = mForegroundView
                        mBackgroundVisiblePosition = mTouchedPosition
                    }

                } else if (swipedRight && !mIsBackgroundVisible) {
                    mTouchedView?.let {
                        animateForeground(it, Animation.CLOSE, ANIMATION_STANDARD)
                        mIsBackgroundVisible = false
                        mBackgroundVisibleView = null
                        mBackgroundVisiblePosition = -1
                    }

                } else if (swipedLeft && mIsBackgroundVisible) {
                    mTouchedView?.let {
                        animateForeground(it, Animation.OPEN, ANIMATION_STANDARD)
                        mIsBackgroundVisible = true
                        mBackgroundVisibleView = mForegroundView
                        mBackgroundVisiblePosition = mTouchedPosition
                    }

                } else if (!swipedRight && !swipedLeft) {
                    if (mIsForegroundPartialViewClicked) {
                        mTouchedView?.let {
                            animateForeground(it, Animation.CLOSE, ANIMATION_STANDARD)
                            mIsBackgroundVisible = false
                            mBackgroundVisibleView = null
                            mBackgroundVisiblePosition = -1
                        }

                    } else if (!mIsBackgroundVisible && mTouchedPosition >= 0 && !mIsRecyclerViewScrolling) {
                        mRowClickListener?.onRowClicked(mTouchedPosition)

                    } else if (mIsBackgroundVisible && !mIsForegroundPartialViewClicked) {
                        val optionID = getOptionViewID(motionEvent)

                        if (optionID >= 0 && mTouchedPosition >= 0) {
                            val downPosition = mTouchedPosition
                            closeBackgroundVisibleView(object : OnOptionSwipeListener {
                                override fun onOptionClosed() {
                                    mOptionClickListener?.onOptionClicked(optionID, downPosition)
                                }

                                override fun onOptionOpened() {}
                            })
                        }
                    }
                }

                mVelocityTracker?.recycle()
                mVelocityTracker = null
                mTouchedX = 0F
                mTouchedY = 0F
                mTouchedView = null
                mTouchedPosition = ListView.INVALID_POSITION
                mIsForegroundSwiping = false
                mBackgroundView = null
            }

            MotionEvent.ACTION_MOVE -> run {
                if (mVelocityTracker == null || mPaused) {
                    return@run
                }

                mVelocityTracker?.addMovement(motionEvent)
                val deltaX = motionEvent.rawX - mTouchedX
                val deltaY = motionEvent.rawY - mTouchedY

                if (!mIsForegroundSwiping && abs(deltaX) > mTouchSlop && abs(deltaY) < abs(deltaX) / 2) {
                    mIsForegroundSwiping = true
                    mSwipingSlop = if(deltaX > 0) mTouchSlop else -mTouchSlop
                }

                if (mIsForegroundSwiping) {
                    if (mBackgroundView == null) {
                        mBackgroundView = mTouchedView?.findViewById(mBackgroundViewId)
                        mBackgroundView?.visibility = View.VISIBLE
                    }

                    if (deltaX < mTouchSlop && !mIsBackgroundVisible) {
                        val translateAmount = deltaX - mSwipingSlop
                        mForegroundView?.let {
                            it.translationX = if(abs(translateAmount) > mBackgroundWidth) -mBackgroundWidth.toFloat() else translateAmount

                            if (it.translationX > 0) {
                                it.translationX = 0F
                            }
                        }

                    } else if (deltaX > 0 && mIsBackgroundVisible) {
                        if (mIsBackgroundVisible) {
                            val translateAmount = (deltaX - mSwipingSlop) - mBackgroundWidth
                            mForegroundView?.translationX = if(translateAmount > 0) 0F else translateAmount

                        } else {
                            val translateAmount = (deltaX - mSwipingSlop) - mBackgroundWidth
                            mForegroundView?.translationX = if(translateAmount > 0) 0F else translateAmount
                        }
                    }

                    return true
                }
            }
        }

        return false
    }

    override fun getTouchCoordinates(ev: MotionEvent) {
        val y = ev.rawY.toInt()

        if (mIsBackgroundVisible && ev.actionMasked == MotionEvent.ACTION_DOWN && y < mRecyclerViewOutsideHeight) {
            closeBackgroundVisibleView(null)
        }
    }

    enum class Animation {
        OPEN,
        CLOSE
    }

    interface OnRowClickListener {
        fun onRowClicked(position: Int)
    }

    interface OnOptionClickListener {
        fun onOptionClicked(viewId: Int, position: Int)
    }

    interface RecyclerTouchListenerHelper {
        fun setOnActivityTouchListener(listener: OnActivityTouchListener)
    }

    interface OnOptionSwipeListener {
        fun onOptionClosed()
        fun onOptionOpened()
    }

    companion object {
        const val ANIMATION_CLOSE = 100L
        const val ANIMATION_STANDARD = 100L
    }
}