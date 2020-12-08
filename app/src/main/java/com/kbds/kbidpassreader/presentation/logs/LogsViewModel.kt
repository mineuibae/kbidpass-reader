package com.kbds.kbidpassreader.presentation.logs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.log.LogFilterType
import com.kbds.kbidpassreader.domain.usecase.log.ObserveLogsUseCase

class LogsViewModel @ViewModelInject constructor(
    private val observeLogsUseCase: ObserveLogsUseCase
) : ViewModel() {

    private var currentFiltering = MutableLiveData<Pair<LogFilterType, String?>>(Pair(
        LogFilterType.ALL_LOGS, null))

    private val _logs: LiveData<Response<List<LogEntity>>> = currentFiltering.switchMap { filter ->
        liveData {
            emit(Response.Loading)
            emitSource(observeLogsUseCase(filter.first, filter.second))
        }
    }

    val logs: LiveData<List<LogEntity>> = _logs.map {
        when (it) {
            is Response.Success -> {
                it.data
            }
            else -> {
                emptyList()
            }
        }
    }

    fun loadLogs(filterType: LogFilterType, user_id: String? = null) {
        currentFiltering.value = Pair(filterType, user_id)
    }

    fun refresh() {
        loadLogs(
            currentFiltering.value?.first ?: LogFilterType.ALL_LOGS,
            currentFiltering.value?.second
        )
    }

    init {
        loadLogs(LogFilterType.ALL_LOGS)
    }
}