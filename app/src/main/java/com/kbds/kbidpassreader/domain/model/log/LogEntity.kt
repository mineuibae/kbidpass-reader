package com.kbds.kbidpassreader.domain.model.log

import java.util.*

data class LogEntity(
    var id: Int = 0,
    val user_id: String? = null,
    var user_name: String? = null,
    var title: String,
    var content: String? = null,
    var desc: String,
    var logged_at: Date,
    var log_type: LogType
)