package com.kbds.kbidpassreader.extension

import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kbds.kbidpassreader.util.Event

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