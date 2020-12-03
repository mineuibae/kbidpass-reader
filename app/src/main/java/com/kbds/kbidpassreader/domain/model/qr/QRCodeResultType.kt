package com.kbds.kbidpassreader.domain.model.qr

enum class QRCodeResultType {
    REGISTER,
    AUTH,
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