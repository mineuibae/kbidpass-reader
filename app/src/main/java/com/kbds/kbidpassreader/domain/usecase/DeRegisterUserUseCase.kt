package com.kbds.kbidpassreader.domain.usecase

import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.User
import javax.inject.Inject

class DeRegisterUserUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(user: User) {
        return kbIdPassRepository.deRegisterUser(user)
    }
}