package com.kbds.kbidpassreader.presentation.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.kbds.kbidpassreader.base.BaseFragment
import com.kbds.kbidpassreader.databinding.FragmentLogsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogsFragment : BaseFragment() {

    private lateinit var binding: FragmentLogsBinding
    private val logsViewModel by activityViewModels<LogsViewModel>()
    private lateinit var listAdapter: LogsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogsBinding.inflate(inflater, container, false).apply {
            this.viewModel = logsViewModel
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
            listAdapter = LogsAdapter(viewModel)
            binding.logsList.apply {
                adapter = listAdapter
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }
        }
    }
}