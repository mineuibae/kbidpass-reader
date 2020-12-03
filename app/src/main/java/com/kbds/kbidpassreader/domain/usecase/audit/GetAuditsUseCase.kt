package com.kbds.kbidpassreader.domain.usecase.audit

import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.audit.Audit
import com.kbds.kbidpassreader.domain.model.audit.AuditFilterType
import com.kbds.kbidpassreader.domain.model.audit.AuditType
import javax.inject.Inject

class GetAuditsUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(
        currentFiltering: AuditFilterType = AuditFilterType.ALL_AUDITS,
        user_id: String? = null
    ) : Response<List<Audit>> {

        val audits = if(user_id == null) {
            kbIdPassRepository.getAudits()
        } else {
            kbIdPassRepository.getAuditsFromUser(user_id)
        }

        return when {
            (audits is Response.Success) && (currentFiltering != AuditFilterType.ALL_AUDITS) -> {
                when(currentFiltering) {
                    AuditFilterType.SUCCESS_AUDITS -> {
                        Response.Success(audits.data.filter {
                            it.audit_type == AuditType.SUCCESS
                        })
                    }
                    else -> {
                        Response.Success(audits.data.filter {
                            it.audit_type == AuditType.NORMAL
                        })
                    }
                }
            }

            else -> audits
        }
    }
}