package com.kbds.kbidpassreader.domain.usecase.log

import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.log.LogType
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import java.util.*
import javax.inject.Inject

class AddLogUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(log: LogEntity) {
        return kbIdPassRepository.addLog(log)
    }

    suspend operator fun invoke(
        user: UserEntity? = null,
        title: String,
        content: String? = null,
        desc: String,
        log_type: LogType
    ) {

        return kbIdPassRepository.addLog(
            LogEntity(
                user_id = user?.id,
                user_name = user?.name,
                title = title,
                content = content,
                desc = desc,
                log_type = log_type,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun addUserSuccessLog(user: UserEntity) {
        return kbIdPassRepository.addLog(
            LogEntity(
                user_id = user.id,
                user_name = user.name,
                title = "사용자 등록",
                desc = "사용자 등록 성공",
                log_type = LogType.SUCCESS,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun addUserFailLog(user: UserEntity? = null, message: String?) {
        return kbIdPassRepository.addLog(
            LogEntity(
                user_id = user?.id,
                user_name = user?.name,
                title = "사용자 등록",
                desc = "사용자 등록 실패 - $message",
                log_type = LogType.ERROR,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun updateUserSuccessLog(user: UserEntity) {
        return kbIdPassRepository.addLog(
            LogEntity(
                user_id = user.id,
                user_name = user.name,
                title = "사용자 수정",
                desc = "사용자 수정 성공",
                log_type = LogType.SUCCESS,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun updateUserFailLog(user: UserEntity? = null, message: String?) {
        return kbIdPassRepository.addLog(
            LogEntity(
                user_id = user?.id,
                user_name = user?.name,
                title = "사용자 수정",
                desc = "사용자 수정 실패 - $message",
                log_type = LogType.ERROR,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun qrSuccessLog(desc: String, content: String, message: String) {
        return kbIdPassRepository.addLog(
            LogEntity(
                title = "QR 인증",
                content = content,
                desc = "$desc - $message",
                log_type = LogType.SUCCESS,
                logged_at = Calendar.getInstance().time
            )
        )
    }

    suspend fun qrFailLog(desc: String, content: String, message: String) {
        return kbIdPassRepository.addLog(
            LogEntity(
                title = "QR 인증",
                content = content,
                desc = "$desc - $message",
                log_type = LogType.ERROR,
                logged_at = Calendar.getInstance().time
            )
        )
    }
}