package com.kbds.kbidpassreader.presentation.audits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.kbds.kbidpassreader.base.BaseFragment
import com.kbds.kbidpassreader.databinding.FragmentAuditsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditsFragment : BaseFragment() {

    private lateinit var binding: FragmentAuditsBinding
    private val auditsViewModel by activityViewModels<AuditsViewModel>()
    private lateinit var listAdapter: AuditsAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setWindowTopInset()
        setupListAdapter()
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        if(viewModel != null) {
            listAdapter = AuditsAdapter(viewModel)
            binding.auditsList.apply {
                adapter = listAdapter
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }
        }
    }
}