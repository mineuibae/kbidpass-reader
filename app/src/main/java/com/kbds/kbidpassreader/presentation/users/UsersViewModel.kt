package com.kbds.kbidpassreader.presentation.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.Response.Success
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import com.kbds.kbidpassreader.domain.model.user.UserFilterType
import com.kbds.kbidpassreader.domain.usecase.user.DeleteUserUseCase
import com.kbds.kbidpassreader.domain.usecase.user.ObserveUsersUseCase
import com.kbds.kbidpassreader.util.Event
import kotlinx.coroutines.launch

class UsersViewModel @ViewModelInject constructor(
    private val observeUsersUseCase: ObserveUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
): ViewModel() {

    private val _editUserEvent = MutableLiveData<Event<String>>()
    val editUserEvent: LiveData<Event<String>> = _editUserEvent

    private var currentFiltering = MutableLiveData<UserFilterType>(
        UserFilterType.ALL_USERS)

    private val _users: LiveData<Response<List<UserEntity>>> = currentFiltering.switchMap { filter ->
        liveData {
            emit(Response.Loading)
            emitSource(observeUsersUseCase(filter))
        }
    }

    val users: LiveData<List<UserEntity>> = _users.map {
        when (it) {
            is Success -> {
                it.data
            }
            else -> {
                emptyList()
            }
        }
    }

    fun loadUsers(filterType: UserFilterType) {
        currentFiltering.value = filterType
    }

    fun editUser(userId: String) {
        _editUserEvent.value = Event(userId)
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            deleteUserUseCase(userId)
        }
    }

    fun refresh() {
        loadUsers(currentFiltering.value ?: UserFilterType.ALL_USERS)
    }

    init {
        loadUsers(UserFilterType.ALL_USERS)
    }
}