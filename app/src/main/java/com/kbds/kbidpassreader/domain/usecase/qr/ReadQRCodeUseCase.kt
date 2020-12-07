package com.kbds.kbidpassreader.domain.usecase.qr

import android.util.Log
import com.journeyapps.barcodescanner.BarcodeResult
import com.kbds.kbidpassreader.domain.model.qr.KBPassResponse
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResponse
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResult
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResultType
import com.kbds.kbidpassreader.extension.decodeBase64
import com.kbds.kbidpassreader.extension.decryptAes256
import com.kbds.kbidpassreader.extension.toDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

class ReadQRCodeUseCase @Inject constructor(
    private val json: Json
){
    operator fun invoke(barcodeResult: BarcodeResult): QRCodeResult {
        val result = barcodeResult.text
        Log.d("ReadQRCodeUseCase", "result: $result")

        try {
            // 등록
            // "{\"id\":\"D190411\", \"pw_hash\":\"qwer1234\", \"device_id\":\"abcd\"}"
            // "{\"type\" : \"register\", \"kbpass\" : \"$encKBPass\", \"time\" : \"2020.12.04 14:00:00\"}"
            // eyJ0eXBlIiA6ICJyZWdpc3RlciIsICJrYnBhc3MiIDogIkdnNG9Zbm5YWWNxRU1PRGVzTU8vUU43eUpDWk4rWUQ5bTZlQVFER3FhTyt1dU95UDRqMmtUcmcyUnFTaEZObHBncWgxWlJhb2JqQS8KaFdXWCtWV3ViZwoiLCAidGltZSIgOiAiMjAyMC4xMi4wNCAxNDowMDowMCJ9

            // 인증
            // "{\"id\":\"D190411\", \"pw_hash\":\"qwer1234\", \"device_id\":\"abcd\"}"
            // "{\"type\" : \"auth\", \"kbpass\" : \"$encKBPass\", \"time\" : \"2020.12.04 14:00:00\"}"
            // eyJ0eXBlIiA6ICJhdXRoIiwgImticGFzcyIgOiAiR2c0b1lublhZY3FFTU9EZXNNTy9RTjd5SkNaTitZRDltNmVBUURHcWFPK3V1T3lQNGoya1RyZzJScVNoRk5scGdxaDFaUmFvYmpBLwpoV1dYK1ZXdWJnCiIsICJ0aW1lIiA6ICIyMDIwLjEyLjA0IDE0OjAwOjAwIn0

            // 1. QR Data Base64 Decode
            val decodeResult = result.decodeBase64()

            // 2. QR Json Parsing
            val kbPassQRCode = json.decodeFromString<QRCodeResponse>(decodeResult)

            if(kbPassQRCode.kbpass.isNullOrEmpty() ||
                kbPassQRCode.type.isNullOrEmpty() ||
                kbPassQRCode.time.isNullOrEmpty()) {

                return QRCodeResult(type = QRCodeResultType.ERROR, message = "KBPassQRCode 필수 값 오류", dataBody = decodeResult)
            }

            // 3. Time Check
            try {
                val resDate = kbPassQRCode.time.toDate()
                val currentDate = Calendar.getInstance().time

                val resTime = resDate.time
                val currentTime = currentDate.time

                val diffTime = if(currentTime >= resTime) currentTime - resTime else resTime - currentTime
                if(diffTime > QR_TIME_OUT) {
                    return QRCodeResult(type = QRCodeResultType.TIMEOUT, message = "인증시간 초과", dataBody = decodeResult)
                }

            } catch (e: Exception) {
                return QRCodeResult(type = QRCodeResultType.ERROR, message = "Date Format 오류", dataBody = decodeResult)
            }

            try {
                // 4. KBPass Decrypt
                val kbPass = kbPassQRCode.kbpass.decryptAes256()

                // 5. KBPass Json Parsing
                val userInfo = json.decodeFromString<KBPassResponse>(kbPass)

                if(userInfo.id.isNullOrEmpty() ||
                    userInfo.pw_hash.isNullOrEmpty() ||
                    userInfo.device_id.isNullOrEmpty()) {

                    return QRCodeResult(type = QRCodeResultType.ERROR, message = "KBPass 사용자 필수 값 오류", dataBody = decodeResult)
                }

                return when(kbPassQRCode.type) {
                    "register", "auth" -> {
                        QRCodeResult(
                            kbPassResponse = userInfo,
                            kbPass = kbPassQRCode.kbpass,
                            type = QRCodeResultType.getQRCodeResultType(kbPassQRCode.type),
                            dataBody = decodeResult)
                    }
                    else -> {
                        QRCodeResult(type = QRCodeResultType.ERROR, message = "알 수 없는 인증 타입", dataBody = decodeResult)
                    }
                }

            } catch (e: Exception) {
                return QRCodeResult(type = QRCodeResultType.ERROR, message = "KBPass 복호화 실패 오류", dataBody = decodeResult)
            }

        } catch(e: Exception){
            return QRCodeResult(type = QRCodeResultType.ERROR, message = "알 수 없는 QR 코드")
        }
    }

    companion object {
        const val QR_TIME_OUT = 15
    }
}