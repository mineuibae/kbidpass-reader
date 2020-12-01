package com.kbds.kbidpassreader.presentation.audits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kbds.kbidpassreader.databinding.FragmentAuditsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditsFragment : Fragment() {

    private lateinit var binding: FragmentAuditsBinding
    private val auditsViewModel by activityViewModels<AuditsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuditsBinding.inflate(inflater, container, false).apply {
            this.viewModel = auditsViewModel
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
    }
}