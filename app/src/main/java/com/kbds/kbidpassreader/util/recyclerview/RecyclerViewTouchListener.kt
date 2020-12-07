package com.kbds.kbidpassreader.util.recyclerview

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.kbds.kbidpassreader.base.BaseActivity

class RecyclerViewTouchListener(val activity: BaseActivity, val recyclerView: RecyclerView)
    : RecyclerView.OnItemTouchListener, OnActivityTouchListener {

    /*
    private static final long ANIMATION_STANDARD = 100;
    private static final long ANIMATION_CLOSE = 100;

    private VelocityTracker mVelocityTracker;

    private OnRowClickListener mRowClickListener;
    private OnOptionClickListener mOptionClickListener;
     */
    lateinit var mForegroundView: View
    lateinit var mBackgroundView: View
    var mForegroundViewId: Int = 0
    var mBackgroundViewId: Int = 0

    lateinit var mOptionViewIds: List<Int>

    private val viewConfiguration: ViewConfiguration = ViewConfiguration.get(recyclerView.context)
    var mTouchSlop = viewConfiguration.scaledTouchSlop
    var mSwipingSlop = 0
    var mMinFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity * 16
    var mMaxFlingVelocity = viewConfiguration.scaledMaximumFlingVelocity

    lateinit var mTouchedView: View
    var mTouchedPosition: Int = 0
    var mTouchedX: Float = 0F
    var mTouchedY: Float = 0F

    lateinit var mBackgroundVisibleView: View
    var mBackgroundVisiblePosition = -1

    var mPaused = true
    var mIsRecyclerViewScrolling = false
    var mIsForegroundSwiping = false
    val mIsBackgroundVisible = false
    val mIsForegroundPartialViewClicked = false

    var mScreenHeight: Int = 0
    var mRecyclerViewOutsideHeight: Int = 0
    var mBackgroundWidth: Int = 1

    lateinit var mVelocityTracker: VelocityTracker
    lateinit var mRowClickListener: OnRowClickListener
    lateinit var mOptionClickListener: OnOptionClickListener

    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                setEnabled(newState != RecyclerView.SCROLL_STATE_DRAGGING);
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
        if(mForegroundViewId != 0 && foregroundViewId != mForegroundViewId)
            throw IllegalArgumentException("foregroundID does not match previously set ID")

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

    fun getOptionViewID(motionEvent: MotionEvent): Int {
        for(i in 0..mOptionViewIds.size) {
            if(mTouchedView != null) {
                val rect = Rect()
                val x = motionEvent.rawX as Int
                val y = motionEvent.rawY as Int
                mTouchedView.findViewById<View>(mOptionViewIds[i]).getGlobalVisibleRect(rect)
                if(rect.contains(x, y)) {
                    return mOptionViewIds[i]
                }
            }
        }

        return -1
    }

    fun handleTouchEvent(motionEvent: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTouchCoordinates(ev: MotionEvent) {
        TODO("Not yet implemented")
    }

    private enum class Animation {
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
}