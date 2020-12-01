package com.kbds.kbidpassreader.domain.usecase

import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.User
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    operator fun invoke(id: String) : LiveData<Response<User>> {
        return kbIdPassRepository.observeUser(id)
    }
}