package com.kbds.kbidpassreader.presentation.main

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kbds.kbidpassreader.R
import com.kbds.kbidpassreader.base.BaseActivity
import com.kbds.kbidpassreader.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        startAnimation()
    }

    private fun startAnimation() {
        binding.splashLottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                startMainActivity()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fate_in, R.anim.fade_out)
    }
}