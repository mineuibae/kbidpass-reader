package com.kbds.kbidpassreader.presentation.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.Response.Success
import com.kbds.kbidpassreader.domain.model.User
import com.kbds.kbidpassreader.domain.model.UserFilterType
import com.kbds.kbidpassreader.domain.usecase.user.ObserveUsersUseCase

class UsersViewModel @ViewModelInject constructor(
    private val observeUsersUseCase: ObserveUsersUseCase
): ViewModel() {

    private var currentFiltering = MutableLiveData<UserFilterType>(UserFilterType.ALL_USERS)

    private val _users: LiveData<Response<List<User>>> = currentFiltering.switchMap { filter ->
        liveData {
            emit(Response.Loading)
            emitSource(observeUsersUseCase(filter))
        }
    }

    val users: LiveData<List<User>> = _users.map {
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

    fun refresh() {
        loadUsers(currentFiltering.value ?: UserFilterType.ALL_USERS)
    }

    init {
        loadUsers(UserFilterType.ALL_USERS)
    }
}