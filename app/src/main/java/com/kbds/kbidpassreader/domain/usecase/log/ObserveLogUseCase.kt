package com.kbds.kbidpassreader.domain.usecase.log

import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import javax.inject.Inject

class ObserveLogUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    operator fun invoke(id: Int) : LiveData<Response<LogEntity>> {
        return kbIdPassRepository.observeLog(id)
    }
}