package com.kbds.kbidpassreader.presentation.edituser

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.usecase.log.AddLogUseCase
import com.kbds.kbidpassreader.domain.usecase.user.GetUserUseCase
import com.kbds.kbidpassreader.domain.usecase.user.UpdateUserUseCase
import com.kbds.kbidpassreader.extension.digestSha256
import com.kbds.kbidpassreader.util.Event
import kotlinx.coroutines.launch

class EditUserViewModel @ViewModelInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val addLogUseCase: AddLogUseCase
): ViewModel() {

    val userId = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    val userDeviceId = MutableLiveData<String>()
    val userKBPass = MutableLiveData<String>()

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    fun start(id: String) {
        viewModelScope.launch {
            getUserUseCase(id).let { user ->
                if(user is Response.Success) {
                    userId.value = user.data.id
                    userName.value = user.data.name
                    userDeviceId.value = user.data.device_id ?: ""
                    userKBPass.value = user.data.kb_pass ?: ""
                }
            }
        }
    }

    fun updateUser() {
        val id = userId.value
        val name = userName.value
        val password = userPassword.value

        if(id != null && name != null && password != null) {
            viewModelScope.launch {
                try {
                    getUserUseCase(id).let { user ->
                        if(user is Response.Success) {
                            val updateUser = user.data.copy(
                                name = name,
                                pw_hash = password.digestSha256()
                            )
                            updateUserUseCase(updateUser)
                            addLogUseCase.updateUserSuccessLog(updateUser)

                        } else {
                            showSnackbarMessage("등록되어있지 않은 사용자입니다.")
                            addLogUseCase.addUserFailLog(message = "$id - 미등록 사용자")
                        }
                    }

                } catch (e: Exception) {
                    addLogUseCase.updateUserFailLog(message = e.message)
                }

                _taskUpdatedEvent.value = Event(Unit)
            }
        } else {
            showSnackbarMessage("필수 항목을 입력해주세요.")
        }
    }

    fun showSnackbarMessage(message: String) {
        _snackbarText.value = Event(message)
    }
}