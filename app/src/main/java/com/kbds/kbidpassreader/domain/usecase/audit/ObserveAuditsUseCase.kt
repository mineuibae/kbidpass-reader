package com.kbds.kbidpassreader.domain.usecase.audit

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.audit.Audit
import com.kbds.kbidpassreader.domain.model.audit.AuditFilterType
import com.kbds.kbidpassreader.domain.model.audit.AuditType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveAuditsUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(
        currentFiltering: AuditFilterType = AuditFilterType.ALL_AUDITS,
        user_id: String? = null
    ): LiveData<Response<List<Audit>>> = withContext(Dispatchers.Default) {

        val audits = if(user_id == null) {
            kbIdPassRepository.observeAudits()
        } else {
            kbIdPassRepository.observeAuditsFromUser(user_id)
        }

        audits.map { response ->
            when {
                (response is Response.Success) && (currentFiltering != AuditFilterType.ALL_AUDITS) -> {
                    when (currentFiltering) {
                        AuditFilterType.SUCCESS_AUDITS -> {
                            Response.Success(response.data.filter { audit ->
                                audit.audit_type == AuditType.SUCCESS
                            })
                        }
                        else -> {
                            Response.Success(response.data.filter { audit ->
                                audit.audit_type == AuditType.NORMAL
                            })
                        }
                    }
                }

                else -> response
            }
        }
    }
}

