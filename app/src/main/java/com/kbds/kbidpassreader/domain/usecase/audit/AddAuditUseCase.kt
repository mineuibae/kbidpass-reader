package com.kbds.kbidpassreader.domain.usecase.audit

import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.Audit
import com.kbds.kbidpassreader.domain.model.AuditType
import com.kbds.kbidpassreader.domain.model.User
import java.util.*
import javax.inject.Inject

class AddAuditUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(audit: Audit) {
        return kbIdPassRepository.addAudit(audit)
    }

    suspend operator fun invoke(
        user: User? = null,
        content: String? = null,
        desc: String? = null,
        audit_type: AuditType? = null) {

        return kbIdPassRepository.addAudit(Audit(
            user_id = user?.id,
            user_name = user?.name,
            content = content,
            desc = desc,
            audit_type = audit_type,
            logged_at = Calendar.getInstance().time
        ))
    }
}