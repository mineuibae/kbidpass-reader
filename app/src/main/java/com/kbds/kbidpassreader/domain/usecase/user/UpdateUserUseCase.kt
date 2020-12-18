package com.kbds.kbidpassreader.domain.usecase.user

import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(user: UserEntity) {
        return kbIdPassRepository.updateUser(user)
    }
}