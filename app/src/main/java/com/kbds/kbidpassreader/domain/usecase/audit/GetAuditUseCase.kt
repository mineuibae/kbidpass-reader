package com.kbds.kbidpassreader.domain.usecase.audit

import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.audit.Audit
import javax.inject.Inject

class GetAuditUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
){
    suspend operator fun invoke(id: Int) : Response<Audit> {
        return kbIdPassRepository.getAudit(id)
    }
}