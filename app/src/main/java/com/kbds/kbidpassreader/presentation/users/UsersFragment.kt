package com.kbds.kbidpassreader.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kbds.kbidpassreader.databinding.FragmentUsersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private val usersViewModel by activityViewModels<UsersViewModel>()
    private lateinit var listAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false).apply {
            this.viewModel = usersViewModel
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setupListAdapter()
        initView()
    }

    private fun initView() {
        binding.addUserFab.setOnClickListener {
            val action = UsersFragmentDirections.actionUsersFragmentToAddUserFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            listAdapter = UsersAdapter(viewModel)
            binding.usersList.adapter = listAdapter
        }
    }
}