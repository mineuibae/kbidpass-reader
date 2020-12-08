package com.kbds.kbidpassreader.extension

import com.kbds.kbidpassreader.data.source.local.table.LogTable
import com.kbds.kbidpassreader.data.source.local.table.UserTable
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.user.UserEntity

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

fun UserTable.map() =
    UserEntity(
        id = id,
        name = name,
        pw_hash = pw_hash,
        device_id = device_id,
        kb_pass = kb_pass,
        created_at = created_at,
        registered_at = registered_at,
        is_registered = is_registered
    )