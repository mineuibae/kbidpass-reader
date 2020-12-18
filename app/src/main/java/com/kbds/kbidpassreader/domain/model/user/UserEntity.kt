package com.kbds.kbidpassreader.domain.model.user

import java.util.*

data class UserEntity (
    var id: String,
    var name: String,
    var pw_hash: String,
    var device_id: String? = null,
    var kb_pass: String? = null,
    var created_at: Date? = null,
    var registered_at: Date? = null,
    var is_registered: Boolean = false
)