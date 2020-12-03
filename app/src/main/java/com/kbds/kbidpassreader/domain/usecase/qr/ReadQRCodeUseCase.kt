package com.kbds.kbidpassreader.domain.usecase.qr

import com.journeyapps.barcodescanner.BarcodeResult
import com.kbds.kbidpassreader.domain.model.qr.KBPassResponse
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResponse
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResult
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResultType
import com.kbds.kbidpassreader.extension.decodeBase64
import com.kbds.kbidpassreader.extension.decryptAes256
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ReadQRCodeUseCase @Inject constructor(
    private val json: Json
){
    operator fun invoke(barcodeResult: BarcodeResult): QRCodeResult {
        val result = barcodeResult.text

        try {
            // 1. QR Data Base64 Decode
            val decodeResult = result.decodeBase64()

            // 2. QR Json Parsing
            val kbPassQRCode = json.decodeFromString<QRCodeResponse>(decodeResult)

            // TODO 3. Time Check

            if(kbPassQRCode.kb_pass.isNullOrEmpty() ||
                kbPassQRCode.type.isNullOrEmpty()) {

                return QRCodeResult(type = QRCodeResultType.ERROR, message = "KBPassQRCode 필수 값 오류")
            }

            try {
                // 4. KBPass Decrypt
                val kbPass = kbPassQRCode.kb_pass.decryptAes256()

                // 5. KBPass Json Parsing
                val userInfo = json.decodeFromString<KBPassResponse>(kbPass)

                if(userInfo.id.isNullOrEmpty() ||
                    userInfo.pw_hash.isNullOrEmpty() ||
                    userInfo.device_id.isNullOrEmpty()) {

                    return QRCodeResult(type = QRCodeResultType.ERROR, message = "KBPass 사용자 필수 값 오류")
                }

                return when(kbPassQRCode.type) {
                    "register", "auth" -> {
                        QRCodeResult(
                            kbPassResponse = userInfo,
                            kbPass = kbPassQRCode.kb_pass,
                            type = QRCodeResultType.getQRCodeResultType(kbPassQRCode.type))
                    }
                    else -> {
                        QRCodeResult(type = QRCodeResultType.ERROR, message = "알 수 없는 인증 타입")
                    }
                }

            } catch (e: Exception) {
                return QRCodeResult(type = QRCodeResultType.ERROR, message = "KBPass 복호화 실패 오류")
            }

        } catch(e: Exception){
            return QRCodeResult(type = QRCodeResultType.ERROR, message = "알 수 없는 QR 코드")
        }
    }
}