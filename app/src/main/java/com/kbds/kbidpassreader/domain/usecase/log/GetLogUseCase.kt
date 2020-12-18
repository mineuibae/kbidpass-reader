package com.kbds.kbidpassreader.domain.usecase.log

import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import javax.inject.Inject

class GetLogUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
){
    suspend operator fun invoke(id: Int) : Response<LogEntity> {
        return kbIdPassRepository.getLog(id)
    }
}