package com.kbds.kbidpassreader.presentation.edituser

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.usecase.audit.AddAuditUseCase
import com.kbds.kbidpassreader.domain.usecase.user.GetUserUseCase
import com.kbds.kbidpassreader.domain.usecase.user.UpdateUserUseCase
import kotlinx.coroutines.launch

class EditUserViewModel @ViewModelInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val addAuditUseCase: AddAuditUseCase
): ViewModel() {

    val userId = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    val userDeviceId = MutableLiveData<String>()
    val userKBPass = MutableLiveData<String>()

    fun start(id: String) {
        viewModelScope.launch {
            getUserUseCase(id).let { user ->
                if(user is Response.Success) {
                    userId.value = user.data.id
                    userName.value = user.data.name
                }
            }
        }
    }

    fun updateUser() {

    }
}