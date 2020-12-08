package com.kbds.kbidpassreader.domain.usecase.user

import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
){
    suspend operator fun invoke(id: String) : Response<UserEntity> {
        return kbIdPassRepository.getUser(id)
    }
}