package com.kbds.kbidpassreader.domain.model.qr

import kotlinx.serialization.Serializable

@Serializable
data class KBPassResponse(
    val id: String?,
    val pw_hash: String?,
    val device_id: String?
)