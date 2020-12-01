package com.kbds.kbidpassreader.presentation.adduser

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.User
import com.kbds.kbidpassreader.domain.usecase.*
import com.kbds.kbidpassreader.util.Event
import kotlinx.coroutines.launch

class AddUserViewModel @ViewModelInject constructor(
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

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    fun addUser() {
        val id = userId.value
        val name = userName.value
        val password = userPassword.value

        if(id != null && name != null && password != null) {
            viewModelScope.launch {
                when(getUserUseCase(id)){
                    is Response.Success -> {
                        _snackbarText.value = Event("이미 등록되어있는 사용자입니다.")
                    }
                    else -> {
                        addUserUseCase(User(id = id, name = name, pw_hash = password))

                        _taskUpdatedEvent.value = Event(Unit)
                    }
                }
            }

        } else {
            _snackbarText.value = Event("필수 항목을 입력해주세요.")
        }
    }
}