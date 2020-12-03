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
import com.kbds.kbidpassreader.domain.usecase.audit.AddAuditUseCase
import com.kbds.kbidpassreader.util.Event
import kotlinx.coroutines.launch

class QRCodeViewModel @ViewModelInject constructor(
    private val addAuditUseCase: AddAuditUseCase,
    private val vibrator: Vibrator
) : ViewModel() {

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> = _permissionGranted

    private val barcodeLoading = MutableLiveData(false)

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun barcodeFinish(result: BarcodeResult?) {
        if(barcodeLoading.value == true) {
            return
        }

        setBarcodeLoading(true)

        val resultText = result?.text

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }

        if(!resultText.isNullOrEmpty()) {
            showSnackbarMessage(resultText)

            viewModelScope.launch {
                addAuditUseCase.qrSuccessAudit(resultText)
            }

        } else {
            showSnackbarMessage("인증에 실패했습니다.")

            viewModelScope.launch {
                addAuditUseCase.qrFailAudit("QR 코드 인식 실패")
            }
        }
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