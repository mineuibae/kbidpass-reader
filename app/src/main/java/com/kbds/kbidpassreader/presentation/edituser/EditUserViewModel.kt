package com.kbds.kbidpassreader.presentation.edituser

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbds.kbidpassreader.domain.model.User
import com.kbds.kbidpassreader.domain.usecase.*
import kotlinx.coroutines.launch

class EditUserViewModel @ViewModelInject constructor(
    //private val kbIdPassRepository: KBIdPassRepository,
    private val observeUserUseCase: ObserveUserUseCase,
    private val observeUsersUseCase: ObserveUsersUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
): ViewModel() {

    val userId = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()

    fun saveUser() {
        val id = userId.value
        val name = userName.value
        val password = userPassword.value

        //TODO ID check
        if(id != null && name != null && password != null) {
            val user = User(id = id, name = name, pw_hash = password)

            viewModelScope.launch {
                addUserUseCase(user)
            }
        }
    }
}