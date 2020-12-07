package com.kbds.kbidpassreader.presentation.edituser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.kbds.kbidpassreader.base.BaseFragment
import com.kbds.kbidpassreader.databinding.FragmentEditUserBinding
import com.kbds.kbidpassreader.extension.setSnackbar
import com.kbds.kbidpassreader.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserFragment : BaseFragment() {
    private lateinit var binding: FragmentEditUserBinding
    private val editUserViewModel by viewModels<EditUserViewModel>()
    private val args: EditUserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserBinding.inflate(inflater, container, false).apply {
            this.viewModel = editUserViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setWindowTopInset()
        setNavigation()
        setSnackbar()

        editUserViewModel.start(args.userId)
    }

    private fun setNavigation() {
        editUserViewModel.taskUpdatedEvent.observe(this.viewLifecycleOwner, EventObserver {
            val action = EditUserFragmentDirections
                .actionEditUserFragmentToUsersFragment(/*EDIT_USER_RESULT_OK*/)
            findNavController().navigate(action)
        })
    }

    private fun setSnackbar() {
        view?.setSnackbar(this, editUserViewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }
}