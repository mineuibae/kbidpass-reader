package com.kbds.kbidpassreader.domain.usecase.user

import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.User
import com.kbds.kbidpassreader.domain.model.UserFilterType
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(
        currentFiltering: UserFilterType = UserFilterType.ALL_USERS
    ) : Response<List<User>> {

        val users = kbIdPassRepository.getUsers()

        return when {
            (users is Response.Success) && (currentFiltering != UserFilterType.ALL_USERS) -> {
                when(currentFiltering) {
                    UserFilterType.REGISTERED_USERS -> {
                        Response.Success(users.data.filter {
                            it.is_registered
                        })
                    }
                    else -> {
                        Response.Success(users.data.filter {
                            !it.is_registered
                        })
                    }
                }
            }

            else -> users
        }
    }
}