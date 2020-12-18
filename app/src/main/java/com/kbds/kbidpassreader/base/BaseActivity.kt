package com.kbds.kbidpassreader.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFullScreen()
    }

    /**
     * mode : null || View.SYSTEM_UI_FLAG_IMMERSIVE || View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
     */
    private fun setFullScreen(
        mode: Int? = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY,
        fullScreen: Boolean = true,
        hideNavigation: Boolean = false
    ) {
        actionBar?.hide()
        supportActionBar?.hide()

        var systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        if(mode != null)
            systemUiVisibility = (systemUiVisibility
                    or mode)

        if (fullScreen) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemUiVisibility = (systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                systemUiVisibility = (systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

                window.statusBarColor = Color.TRANSPARENT
            }
        }

        if (hideNavigation)
            systemUiVisibility = (systemUiVisibility
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)


        window.decorView.systemUiVisibility = systemUiVisibility
    }
}