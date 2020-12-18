package com.kbds.kbidpassreader.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import com.kbds.kbidpassreader.R

class ToolTip(@NonNull val parent: ViewGroup, @NonNull val content: View) {

    private var callback: Callback? = null
    private var duration: Long = 2000

    private val runnable = Runnable {
        content.visibility = View.GONE
        parent.removeView(content)
        callback?.onDismissed()
    }

    fun show() {
        content.removeCallbacks(runnable)
        content.visibility = View.VISIBLE
        parent.addView(content)

        content.postDelayed(runnable, duration)
    }

    fun addCallback(callback: Callback?) {
        this.callback = callback
    }

    interface Callback {
        fun onDismissed()
    }

    companion object {
        fun make(view: View, text: String, duration: Long = 2000) : ToolTip {
            val parent = view as ViewGroup
            val inflater = LayoutInflater.from(parent.context)
            val content = inflater.inflate(R.layout.view_tooltip, parent, false)
            content.findViewById<TextView>(R.id.tooltip_text_view).text = text

            val toolTip = ToolTip(parent, content)
            toolTip.duration = duration

            return toolTip
        }
    }
}