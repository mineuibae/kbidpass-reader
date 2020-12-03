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
    private val vibrator: Vibrator
) : ViewModel() {

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> = _permissionGranted

    private val barcodeLoading = MutableLiveData(false)

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

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
                QRCodeResultType.REGISTER -> {
                    registerUser(result.text, qrCodeResult)
                }
                QRCodeResultType.AUTH -> {
                    authUser(result.text, qrCodeResult)
                }
                QRCodeResultType.ERROR -> {
                    showSnackbarMessage("인증에 실패했습니다.")
                    addAuditUseCase.qrFailAudit(
                        content = result.text,
                        desc = "QR 인증 실패",
                        message = qrCodeResult.message ?: ""
                    )
                }
            }
        }
    }

    private suspend fun registerUser(result: String, qrCodeResult: QRCodeResult) {

        qrCodeResult.kbPassResponse?.let { kbPassResponse ->
            val id = kbPassResponse.id ?: ""

            when(val responseUser = getUserUseCase(id)){
                is Response.Success -> {
                    //TODO Check Password

                    updateUserUseCase(responseUser.data.copy(
                        device_id = kbPassResponse.device_id,
                        kb_pass = qrCodeResult.kbPass,
                        registered_at = Calendar.getInstance().time,
                        is_registered = true
                    ))

                    showSnackbarMessage("사용자 등록에 성공했습니다.")
                    addAuditUseCase.qrSuccessAudit(
                        content = result,
                        desc = "QR 등록 성공",
                        message = responseUser.data.name
                    )
                }
                else -> {
                    showSnackbarMessage("등록되어있지 않은 사용자입니다.")
                    addAuditUseCase.qrFailAudit(
                        content = result,
                        desc = "QR 등록 실패",
                        message = id
                    )
                }
            }
        }
    }

    private fun authUser(result: String, qrCodeResult: QRCodeResult) {

    }

    fun showSnackbarMessage(message: String) {
        _snackbarText.value = Event(message)
    }

    fun setBarcodeLoading(isLoading: Boolean) {
        barcodeLoading.value = isLoading
    }

    fun setPermissionGranted(isPermissionGranted: Boolean) {
        _permissionGranted.value = isPermissionGranted
    }
}