package com.kbds.kbidpassreader.presentation.qr

import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.BarcodeResult
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResult
import com.kbds.kbidpassreader.domain.model.qr.QRCodeResultType
import com.kbds.kbidpassreader.domain.usecase.audit.AddAuditUseCase
import com.kbds.kbidpassreader.domain.usecase.qr.ReadQRCodeUseCase
import com.kbds.kbidpassreader.domain.usecase.qr.VerifyQRCodeUseCase
import com.kbds.kbidpassreader.domain.usecase.user.GetUserUseCase
import com.kbds.kbidpassreader.domain.usecase.user.UpdateUserUseCase
import com.kbds.kbidpassreader.util.Event
import kotlinx.coroutines.launch
import java.util.*

class QRCodeViewModel @ViewModelInject constructor(
    private val addAuditUseCase: AddAuditUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val readQRCodeUseCase: ReadQRCodeUseCase,
    private val verifyQRCodeUseCase: VerifyQRCodeUseCase,
    private val vibrator: Vibrator
) : ViewModel() {

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> = _permissionGranted

    private val barcodeLoading = MutableLiveData(false)

    private val _toolTipText = MutableLiveData<Event<String>>()
    val toolTipText: LiveData<Event<String>> = _toolTipText

    fun barcodeFinish(result: BarcodeResult) {
        if(barcodeLoading.value == true) {
            return
        }

        setBarcodeLoading(true)

        viewModelScope.launch {
            val qrCodeResult = readQRCodeUseCase(result)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(100)
            }

            when(qrCodeResult.type) {
                QRCodeResultType.REGISTER, QRCodeResultType.AUTH -> {
                    verifyQRCode(qrCodeResult.dataBody ?: result.text, qrCodeResult)
                }
                QRCodeResultType.TIMEOUT -> {
                    showToolTipMessage("인증 시간이 초과되었습니다.")
                    addAuditUseCase.qrFailAudit("QR 인증 실패", qrCodeResult.dataBody ?: result.text, qrCodeResult.message ?: "")
                }
                QRCodeResultType.ERROR -> {
                    showToolTipMessage("인증에 실패했습니다.")
                    addAuditUseCase.qrFailAudit("QR 인증 실패", qrCodeResult.dataBody ?: result.text, qrCodeResult.message ?: "")
                }
            }
        }
    }

    private suspend fun verifyQRCode(result: String, qrCodeResult: QRCodeResult) {
        qrCodeResult.kbPassResponse?.let { kbPassResponse ->
            val id = kbPassResponse.id?.toUpperCase() ?: ""

            when(val responseUser = getUserUseCase(id)){
                is Response.Success -> {
                    when(verifyQRCodeUseCase(qrCodeResult, responseUser.data)) {
                        VerifyQRCodeUseCase.VerifyQRCodeResult.SUCCESS -> {
                            if(qrCodeResult.type == QRCodeResultType.REGISTER) {
                                updateUserUseCase(responseUser.data.copy(
                                    device_id = kbPassResponse.device_id,
                                    kb_pass = qrCodeResult.kbPass,
                                    registered_at = Calendar.getInstance().time,
                                    is_registered = true
                                ))

                                showToolTipMessage("사용자 등록에 성공했습니다.")
                                addAuditUseCase.qrSuccessAudit("QR 등록 성공", result, responseUser.data.name)
                            }
                            else if(qrCodeResult.type == QRCodeResultType.AUTH) {
                                showToolTipMessage("인증되었습니다.")
                                addAuditUseCase.qrSuccessAudit("QR 로그인 성공", result, responseUser.data.name)
                            }
                        }
                        VerifyQRCodeUseCase.VerifyQRCodeResult.ERROR_UNREGISTERED -> {
                            showToolTipMessage("QR을 등록하지 않은 사용자입니다.\n먼저 QR 등록을 진행해 주세요.")
                            addAuditUseCase.qrFailAudit("QR 인증 실패", result, "${responseUser.data.name} - QR 미등록")
                        }
                        VerifyQRCodeUseCase.VerifyQRCodeResult.ERROR_PASSWORD -> {
                            showToolTipMessage("비밀번호가 일치하지 않습니다.")
                            addAuditUseCase.qrFailAudit("QR 인증 실패", result, "${responseUser.data.name} - 비밀번호 오류")
                        }
                        VerifyQRCodeUseCase.VerifyQRCodeResult.ERROR_KB_PASS -> {
                            showToolTipMessage("고유키가 일치하지 않습니다.")
                            addAuditUseCase.qrFailAudit("QR 인증 실패", result, "${responseUser.data.name} - KBPASS 오류")
                        }
                        VerifyQRCodeUseCase.VerifyQRCodeResult.ERROR_DEVICE_ID -> {
                            showToolTipMessage("단말기 고유값이 일치하지 않습니다.\n다시 등록해 주세요.")
                            addAuditUseCase.qrFailAudit("QR 인증 실패", result, "${responseUser.data.name} - DEVICE ID 오류")
                        }
                        else -> {

                        }
                    }
                }
                else -> {
                    showToolTipMessage("등록되어있지 않은 사용자입니다.")
                    addAuditUseCase.qrFailAudit("QR 인증 실패", result, "${id} - 미등록 사용자")
                }
            }
        }
    }

    fun showToolTipMessage(message: String) {
        _toolTipText.value = Event(message)
    }

    fun setBarcodeLoading(isLoading: Boolean) {
        barcodeLoading.value = isLoading
    }

    fun setPermissionGranted(isPermissionGranted: Boolean) {
        _permissionGranted.value = isPermissionGranted
    }
}