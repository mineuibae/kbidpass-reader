package com.kbds.kbidpassreader.domain.usecase.user

import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(id: String) {
        return kbIdPassRepository.deleteUser(id)
    }
}