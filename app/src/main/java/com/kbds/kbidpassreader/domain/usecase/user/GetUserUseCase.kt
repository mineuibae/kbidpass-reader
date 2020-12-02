package com.kbds.kbidpassreader.domain.usecase.user

import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
){
    suspend operator fun invoke(id: String) : Response<User> {
        return kbIdPassRepository.getUser(id)
    }
}