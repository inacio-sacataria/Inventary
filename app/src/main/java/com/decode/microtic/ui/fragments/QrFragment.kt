package com.decode.microtic.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.decode.microtic.R
import com.decode.microtic.deviceClicked
import com.decode.microtic.ui.activities.InfoProductActivity
import com.decode.microtic.ui.activities.Registrationctivity
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel

class QrFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    lateinit var viewModel: HomeFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_qr, container, false)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

        checkPermission(
            Manifest.permission.CAMERA,
            CAMERA_REQUEST_CODE
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                if (it.text.contains("Série:")) {
                    var text = it?.text?.split("Série:")?.get(1)
                    if (!text.isNullOrEmpty()) {
                        if (!text.isNullOrEmpty()) {

                            var result = viewModel.verifyQR(text);

                            if (result == null) {
                                var intent =
                                    Intent(requireActivity(), Registrationctivity::class.java)
                                intent.putExtra("qrCode", text)
                                startActivity(intent)
                            } else {
                                var intent =
                                    Intent(requireActivity(), InfoProductActivity::class.java)
                                intent.putExtra("device", result)
                                deviceClicked = result
                                startActivity(intent)
                            }
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), " ESTE QRCODE NAO PERTENCE\nA MICROTIC", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }

        override fun onResume() {
            super.onResume()
            codeScanner.startPreview()
        }

        override fun onPause() {
            codeScanner.releaseResources()
            super.onPause()
        }


        private fun checkPermission(permission: String, requestCode: Int) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            } else {

            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(requireContext(), "premission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

        companion object {
            val CODE_PROFILE = 3
            val REQUEST_CODE_PROFILE = 100
            val CODE_DOCUMENT_FROM_CAMERA = 2
            val REQUEST_CODE_DOCUMENT_FROM_CAMERA = 101
            var CAMERA_REQUEST_CODE = 123
        }

    }