package com.kbds.kbidpassreader.presentation.qr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.kbds.kbidpassreader.base.BaseFragment
import com.kbds.kbidpassreader.databinding.FragmentQrCodeBinding
import com.kbds.kbidpassreader.extension.setSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QRCodeFragment : BaseFragment() {

    private lateinit var binding: FragmentQrCodeBinding
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
        setSnackbar()

        binding.permissionLayout.setOnClickListener {
            checkPermission()
        }

        qrCodeViewModel.permissionGranted.observe(viewLifecycleOwner, Observer {isGranted ->
            binding.barcodeView.apply {
                if(isGranted) {
                    decodeContinuous(barcodeCallback)
                } else {
                    stopDecoding()
                }
            }
        })
    }

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            qrCodeViewModel.barcodeFinish(result)
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {

        }
    }

    override fun onResume() {
        binding.barcodeView.resume()
        super.onResume()
    }

    override fun onPause() {
        binding.barcodeView.pause()
        super.onPause()
    }

    private fun setSnackbar() {
        view?.setSnackbar(
            this,
            qrCodeViewModel.snackbarText,
            Snackbar.LENGTH_SHORT,
            object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    lifecycleScope.launch {
                        delay(1000)
                        qrCodeViewModel.setBarcodeLoading(false)
                    }
                }
            }
        )
    }
}