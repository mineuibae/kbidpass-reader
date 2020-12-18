package com.kbds.kbidpassreader.domain.usecase.log

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.log.LogFilterType
import com.kbds.kbidpassreader.domain.model.log.LogType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveLogsUseCase @Inject constructor(
    private val kbIdPassRepository: KBIdPassRepository
) {
    suspend operator fun invoke(
        currentFiltering: LogFilterType = LogFilterType.ALL_LOGS,
        user_id: String? = null
    ): LiveData<Response<List<LogEntity>>> = withContext(Dispatchers.Default) {

        val logs = if(user_id == null) {
            kbIdPassRepository.observeLogs()
        } else {
            kbIdPassRepository.observeLogsFromUser(user_id)
        }

        logs.map { response ->
            when {
                (response is Response.Success) && (currentFiltering != LogFilterType.ALL_LOGS) -> {
                    when (currentFiltering) {
                        LogFilterType.SUCCESS_LOGS -> {
                            Response.Success(response.data.filter { log ->
                                log.log_type == LogType.SUCCESS
                            })
                        }
                        else -> {
                            Response.Success(response.data.filter { log ->
                                log.log_type == LogType.NORMAL
                            })
                        }
                    }
                }

                else -> response
            }
        }
    }
}

