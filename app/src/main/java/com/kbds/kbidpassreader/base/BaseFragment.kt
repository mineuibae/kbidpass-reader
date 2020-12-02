package com.kbds.kbidpassreader.base

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.kbds.kbidpassreader.extension.setWindowTopInset

open class BaseFragment : Fragment() {

    fun setWindowTopInset() {
        with(requireView()) {
            setWindowTopInset { inset ->
                updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = inset
                }
                requestLayout()
            }
        }
    }
}