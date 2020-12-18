package com.kbds.kbidpassreader.domain.usecase.log

import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.log.LogFilterType
import com.kbds.kbidpassreader.domain.model.log.LogType
import javax.inject.Inject

class GetLogsUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(
        currentFiltering: LogFilterType = LogFilterType.ALL_LOGS,
        user_id: String? = null
    ) : Response<List<LogEntity>> {

        val logs = if(user_id == null) {
            kbIdPassRepository.getLogs()
        } else {
            kbIdPassRepository.getLogsFromUser(user_id)
        }

        return when {
            (logs is Response.Success) && (currentFiltering != LogFilterType.ALL_LOGS) -> {
                when(currentFiltering) {
                    LogFilterType.SUCCESS_LOGS -> {
                        Response.Success(logs.data.filter {
                            it.log_type == LogType.SUCCESS
                        })
                    }
                    else -> {
                        Response.Success(logs.data.filter {
                            it.log_type == LogType.NORMAL
                        })
                    }
                }
            }

            else -> logs
        }
    }
}