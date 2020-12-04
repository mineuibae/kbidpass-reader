package com.kbds.kbidpassreader.domain.model.qr

import kotlinx.serialization.Serializable

@Serializable
data class QRCodeResponse(
    val type: String?,
    val kbpass: String?,
    val time: String?
)