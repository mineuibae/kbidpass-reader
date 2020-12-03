package com.kbds.kbidpassreader.domain.model.qr

data class QRCodeResult (
    val kbPassResponse: KBPassResponse? = null,
    val kbPass: String? = null,
    val type: QRCodeResultType,
    val message: String? = null
)