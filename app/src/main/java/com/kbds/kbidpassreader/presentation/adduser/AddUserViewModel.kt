package com.kbds.kbidpassreader.presentation.adduser

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.User
import com.kbds.kbidpassreader.domain.usecase.audit.AddAuditUseCase
import com.kbds.kbidpassreader.domain.usecase.user.AddUserUseCase
import com.kbds.kbidpassreader.domain.usecase.user.GetUserUseCase
import com.kbds.kbidpassreader.util.Event
import kotlinx.coroutines.launch
import java.util.*

class AddUserViewModel @ViewModelInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val addAuditUseCase: AddAuditUseCase
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
                val responseUser = getUserUseCase(id)

                when(responseUser){
                    is Response.Success -> {
                        showSnackbarMessage("이미 등록되어있는 사용자입니다.")
                        addAuditUseCase.addUserFailAudit(responseUser.data, "기등록 사용자")
                    }

                    else -> {
                        try {
                            val user = User(
                                id = id,
                                name = name,
                                pw_hash = password,
                                created_at = Calendar.getInstance().time
                            )

                            addUserUseCase(user)
                            addAuditUseCase.addUserSuccessAudit(user)

                        } catch (e: Exception) {
                            addAuditUseCase.addUserFailAudit(message = e.message)
                        }

                        _taskUpdatedEvent.value = Event(Unit)
                    }
                }
            }

        } else {
            showSnackbarMessage("필수 항목을 입력해주세요.")
        }
    }

    fun showSnackbarMessage(message: String) {
        _snackbarText.value = Event(message)
    }
}