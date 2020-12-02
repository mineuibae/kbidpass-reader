package com.kbds.kbidpassreader.domain.usecase.audit

import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.Audit
import javax.inject.Inject

class ObserveAuditUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    operator fun invoke(id: Int) : LiveData<Response<Audit>> {
        return kbIdPassRepository.observeAudit(id)
    }
}