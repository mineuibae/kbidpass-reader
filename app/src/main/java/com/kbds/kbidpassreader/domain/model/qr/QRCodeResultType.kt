package com.kbds.kbidpassreader.domain.model.qr

enum class QRCodeResultType {
    REGISTER,
    AUTH,
    TIMEOUT,
    ERROR;

    companion object {
        fun getQRCodeResultType(type: String) : QRCodeResultType {
            return when(type) {
                "register" -> REGISTER
                "auth" -> AUTH
                else -> ERROR
            }
        }
    }
}