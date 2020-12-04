package com.kbds.kbidpassreader.extension

import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kbds.kbidpassreader.util.Event
import com.kbds.kbidpassreader.view.ToolTip

fun View.setWindowTopInset(callback: (Int) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        val statusBarHeight = insets.systemWindowInsets.top
        callback(statusBarHeight)
        ViewCompat.setOnApplyWindowInsetsListener(this, null)
        insets
    }
}

fun View.showSnackbar(snackbarText: String, timeLength: Int, callback: Snackbar.Callback? = null) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(callback)
        show()
    }
}

fun View.setSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<String>>,
    timeLength: Int,
    callback: Snackbar.Callback? = null
) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { snackBarText ->
            showSnackbar(snackBarText, timeLength, callback)
        }
    })
}

fun View.showToolTip(tooTipText: String, timeLength: Long, callback: ToolTip.Callback? = null) {
    ToolTip.make(this, tooTipText, timeLength).run {
        addCallback(callback)
        show()
    }
}

fun View.setToolTip(
    lifecycleOwner: LifecycleOwner,
    toolTipEvent: LiveData<Event<String>>,
    timeLength: Long,
    callback: ToolTip.Callback? = null
) {
    toolTipEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { toolTipText ->
            showToolTip(toolTipText, timeLength, callback)
        }
    })
}