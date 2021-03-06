package com.kbds.kbidpassreader.domain.usecase.qr

import com.kbds.kbidpassreader.domain.model.qr.QRCodeResult
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResultType
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import com.kbds.kbidpassreader.extension.digestSha256
import javax.inject.Inject

class VerifyQRCodeUseCase @Inject constructor() {

    operator fun invoke(qrCodeResult: QRCodeResult, user: UserEntity) : VerifyQRCodeResult {
        when(qrCodeResult.type) {
            QRCodeResultType.REGISTER -> {
                return if(verifyPassword(qrCodeResult, user)) VerifyQRCodeResult.SUCCESS else VerifyQRCodeResult.ERROR_PASSWORD
            }
            QRCodeResultType.AUTH -> {
                return when {
                    !isRegistered(user) -> { VerifyQRCodeResult.ERROR_UNREGISTERED }
                    !verifyKBPass(qrCodeResult, user) -> { VerifyQRCodeResult.ERROR_KB_PASS }
                    !verifyPassword(qrCodeResult, user) -> { VerifyQRCodeResult.ERROR_PASSWORD }
                    !verifyDeviceId(qrCodeResult, user) -> { VerifyQRCodeResult.ERROR_DEVICE_ID }
                    else -> { VerifyQRCodeResult.SUCCESS }
                }
            }
            else -> {
                return VerifyQRCodeResult.ERROR_UNKNOWN
            }
        }
    }

    private fun isRegistered(user: UserEntity) =
        (user.is_registered)

    private fun verifyPassword(qrCodeResult: QRCodeResult, user: UserEntity) =
        (user.pw_hash == qrCodeResult.kbPassResponse?.pw_hash?.digestSha256()) && user.pw_hash.isNotEmpty()

    private fun verifyKBPass(qrCodeResult: QRCodeResult, user: UserEntity) =
        (user.kb_pass == qrCodeResult.kbPass) && !user.kb_pass.isNullOrEmpty()

    private fun verifyDeviceId(qrCodeResult: QRCodeResult, user: UserEntity) =
        (user.device_id == qrCodeResult.kbPassResponse?.device_id) && !user.device_id.isNullOrEmpty()

    enum class VerifyQRCodeResult {
        SUCCESS,
        ERROR_UNREGISTERED,
        ERROR_PASSWORD,
        ERROR_KB_PASS,
        ERROR_DEVICE_ID,
        ERROR_UNKNOWN
    }
}