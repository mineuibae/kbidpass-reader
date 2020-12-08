package com.kbds.kbidpassreader.domain.usecase.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import com.kbds.kbidpassreader.domain.model.user.UserFilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveUsersUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(
        currentFiltering: UserFilterType = UserFilterType.ALL_USERS
    ): LiveData<Response<List<UserEntity>>> = withContext(Dispatchers.Default) {
        kbIdPassRepository.observeUsers().map { response ->
            when {
                (response is Response.Success) && (currentFiltering != UserFilterType.ALL_USERS) -> {
                    when (currentFiltering) {
                        UserFilterType.REGISTERED_USERS -> {
                            Response.Success(response.data.filter { user ->
                                user.is_registered
                            })
                        }
                        else -> {
                            Response.Success(response.data.filter { user ->
                                !user.is_registered
                            })
                        }
                    }
                }

                else -> response
            }
        }
    }
}

