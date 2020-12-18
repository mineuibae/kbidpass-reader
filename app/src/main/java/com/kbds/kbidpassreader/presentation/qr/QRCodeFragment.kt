package com.kbds.kbidpassreader.presentation.qr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.kbds.kbidpassreader.base.BaseFragment
import com.kbds.kbidpassreader.databinding.FragmentQrCodeBinding
import com.kbds.kbidpassreader.extension.setToolTip
import com.kbds.kbidpassreader.view.ToolTip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class QRCodeFragment : BaseFragment() {

    private lateinit var binding: FragmentQrCodeBinding
    lateinit var tts: TextToSpeech
    private val qrCodeViewModel by viewModels<QRCodeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false).apply {
            this.viewModel = qrCodeViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        initView()
        checkPermission()
    }

    private val requestPermission  = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        qrCodeViewModel.setPermissionGranted(it)
    }

    private fun checkPermission() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            lifecycleScope.launch {
                delay(1000) // Delay 안하면 UI가 Block 되는 현상 있음 (androidx.fragment:fragment-ktx:1.3.0-beta01)
                requestPermission.launch(Manifest.permission.CAMERA)
            }

        } else {
            qrCodeViewModel.setPermissionGranted(true)
        }
    }

    private fun initView() {
        setToolTip()

        binding.permissionLayout.setOnClickListener {
            checkPermission()
        }

        binding.flashToggleButton.setOnCheckedChangeListener(flashToggleListener)

        qrCodeViewModel.permissionGranted.observe(viewLifecycleOwner, Observer {isGranted ->
            if(isGranted) {
                tts = TextToSpeech(requireContext()) {
                    if(it != TextToSpeech.ERROR) {
                        tts.language = Locale.KOREAN
                    }
                }
            }

            binding.barcodeView.apply {
                if(isGranted) {
                    decodeContinuous(barcodeCallback)
                } else {
                    pause()
                }
            }
        })

        qrCodeViewModel.ttsText.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { ttsText ->
                tts?.speak(ttsText, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        })
    }

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            if(result != null) {
                qrCodeViewModel.barcodeFinish(result)
            }
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {

        }
    }

    private val flashToggleListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if(isChecked) binding.barcodeView.setTorchOn() else binding.barcodeView.setTorchOff()
    }

    override fun onResume() {
        binding.barcodeView.resume()
        super.onResume()
    }

    override fun onPause() {
        binding.barcodeView.pause()
        super.onPause()
    }

    private fun setToolTip() {
        view?.setToolTip(
            this,
            qrCodeViewModel.toolTipText,
            2000,
            object : ToolTip.Callback {
                override fun onDismissed() {
                    lifecycleScope.launch {
                        delay(1000)
                        qrCodeViewModel.setBarcodeLoading(false)
                    }
                }
            }
        )
    }
}