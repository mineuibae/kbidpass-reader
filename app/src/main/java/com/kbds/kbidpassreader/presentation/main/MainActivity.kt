package com.kbds.kbidpassreader.presentation.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kbds.kbidpassreader.R
import com.kbds.kbidpassreader.base.BaseActivity
import com.kbds.kbidpassreader.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
    }

    private fun initView() {
        val navController = findNavController(R.id.nav_host_fragment)

        binding.bottomNavigation.apply {
            setupWithNavController(navController)
        }
    }
}