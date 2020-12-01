package com.kbds.kbidpassreader.presentation.adduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kbds.kbidpassreader.databinding.FragmentAddUserBinding
import com.kbds.kbidpassreader.extension.setSnackbar
import com.kbds.kbidpassreader.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private lateinit var binding: FragmentAddUserBinding
    private val addUserViewModel by viewModels<AddUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserBinding.inflate(inflater, container, false).apply {
            this.viewModel = addUserViewModel
        }

        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setNavigation()
        setSnackbar()
    }

    private fun setNavigation() {
        addUserViewModel.taskUpdatedEvent.observe(this.viewLifecycleOwner, EventObserver {
            val action = AddUserFragmentDirections
                .actionAddUserFragmentToUsersFragment(/*ADD_USER_RESULT_OK*/)
            findNavController().navigate(action)
        })
    }

    private fun setSnackbar() {
        view?.setSnackbar(this, addUserViewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }
}