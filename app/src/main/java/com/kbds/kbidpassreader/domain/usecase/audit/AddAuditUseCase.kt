package com.kbds.kbidpassreader.domain.usecase.audit

import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.audit.Audit
import com.kbds.kbidpassreader.domain.model.audit.AuditType
import com.kbds.kbidpassreader.domain.model.user.User
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
        title: String,
        content: String? = null,
        desc: String,
        audit_type: AuditType
    ) {

        return kbIdPassRepository.addAudit(
            Audit(
                user_id = user?.id,
                user_name = user?.name,
                title = title,
                content = content,
                desc = desc,
                audit_type = audit_type,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun addUserSuccessAudit(user: User) {
        return kbIdPassRepository.addAudit(
            Audit(
                user_id = user.id,
                user_name = user.name,
                title = "사용자 등록",
                desc = "사용자 등록 성공",
                audit_type = AuditType.SUCCESS,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun addUserFailAudit(user: User? = null, message: String?) {
        return kbIdPassRepository.addAudit(
            Audit(
                user_id = user?.id,
                user_name = user?.name,
                title = "사용자 등록",
                desc = "사용자 등록 실패 - $message",
                audit_type = AuditType.ERROR,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun updateUserSuccessAudit(user: User) {
        return kbIdPassRepository.addAudit(
            Audit(
                user_id = user.id,
                user_name = user.name,
                title = "사용자 수정",
                desc = "사용자 수정 성공",
                audit_type = AuditType.SUCCESS,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun updateUserFailAudit(user: User? = null, message: String?) {
        return kbIdPassRepository.addAudit(
            Audit(
                user_id = user?.id,
                user_name = user?.name,
                title = "사용자 수정",
                desc = "사용자 수정 실패 - $message",
                audit_type = AuditType.ERROR,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun qrSuccessAudit(desc: String, content: String, message: String) {
        return kbIdPassRepository.addAudit(
            Audit(
                title = "QR 인증",
                content = content,
                desc = "$desc - $message",
                audit_type = AuditType.SUCCESS,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun qrFailAudit(desc: String, content: String, message: String) {
        return kbIdPassRepository.addAudit(
            Audit(
                title = "QR 인증",
                content = content,
                desc = "$desc - $message",
                audit_type = AuditType.ERROR,
                logged_at = Calendar.getInstance().time
            )
        )
    }
}