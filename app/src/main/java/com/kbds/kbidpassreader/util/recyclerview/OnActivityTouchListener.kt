package com.kbds.kbidpassreader.util.recyclerview

import android.view.MotionEvent

interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent)
}