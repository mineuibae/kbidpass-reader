package com.kbds.kbidpassreader.presentation.edituser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kbds.kbidpassreader.databinding.FragmentEditUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserFragment : Fragment() {
    private lateinit var binding: FragmentEditUserBinding
    private val editUserViewModel by viewModels<EditUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserBinding.inflate(inflater, container, false).apply {
            this.viewModel = editUserViewModel
        }

        binding.lifecycleOwner = this.viewLifecycleOwner

        initView()

        return binding.root
    }

    private fun initView() {
        binding.appIcon.apply {
            setMinAndMaxFrame(42, 42)
        }
    }
}