package com.kbds.kbidpassreader.extension

import com.kbds.kbidpassreader.data.source.local.table.LogTable
import com.kbds.kbidpassreader.domain.model.log.LogEntity

fun LogTable.map() =
    LogEntity(
        id = id,
        user_id = user_id,
        user_name = user_name,
        title = title,
        content = content,
        desc = desc,
        logged_at = logged_at,
        log_type = log_type
    )