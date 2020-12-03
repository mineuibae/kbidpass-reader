package com.kbds.kbidpassreader.presentation.audits

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.audit.Audit
import com.kbds.kbidpassreader.domain.model.audit.AuditFilterType
import com.kbds.kbidpassreader.domain.usecase.audit.ObserveAuditsUseCase

class AuditsViewModel @ViewModelInject constructor(
    private val observeAuditsUseCase: ObserveAuditsUseCase
) : ViewModel() {

    private var currentFiltering = MutableLiveData<Pair<AuditFilterType, String?>>(Pair(
        AuditFilterType.ALL_AUDITS, null))

    private val _audits: LiveData<Response<List<Audit>>> = currentFiltering.switchMap { filter ->
        liveData {
            emit(Response.Loading)
            emitSource(observeAuditsUseCase(filter.first, filter.second))
        }
    }

    val audits: LiveData<List<Audit>> = _audits.map {
        when (it) {
            is Response.Success -> {
                it.data
            }
            else -> {
                emptyList()
            }
        }
    }

    fun loadAudits(filterType: AuditFilterType, user_id: String? = null) {
        currentFiltering.value = Pair(filterType, user_id)
    }

    fun refresh() {
        loadAudits(
            currentFiltering.value?.first ?: AuditFilterType.ALL_AUDITS,
            currentFiltering.value?.second
        )
    }

    init {
        loadAudits(AuditFilterType.ALL_AUDITS)
    }
}