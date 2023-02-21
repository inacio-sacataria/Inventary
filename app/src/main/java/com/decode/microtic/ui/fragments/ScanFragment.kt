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
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.decode.microtic.R
import com.decode.microtic.data.AuthInstance
import com.decode.microtic.databinding.FragmentRegistUserBinding
import com.decode.microtic.databinding.FragmentScanBinding
import com.decode.microtic.deviceClicked
import com.decode.microtic.ui.activities.CategoriaActivity
import com.decode.microtic.ui.activities.InfoProductActivity
import com.decode.microtic.ui.activities.LoginActivity
import com.decode.microtic.ui.activities.Registrationctivity
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel


class ScanFragment : Fragment() {
    lateinit var binding : FragmentScanBinding
    private lateinit var codeScanner: CodeScanner
    lateinit var viewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_scan, container, false)
        binding = FragmentScanBinding.bind(view)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

        checkPermission(
            Manifest.permission.CAMERA,
            QrFragment.CAMERA_REQUEST_CODE
        )



        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mlogaout -> {
                    AuthInstance.getInstance()?.signOut()
                    var intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                var text = it.text.split("Série:").get(1)
                var result = viewModel.verifyQR(text);

                if (result!=null){
                    requireActivity().intent.putExtra("device", result)
                    deviceClicked = result
                   findNavController().navigate(R.id.action_qrFragment2_to_infoFragment)
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


    private fun checkPermission(permission: String, requestCode: Int){
        if(ContextCompat.checkSelfPermission(requireContext(),permission)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission),requestCode  )
        }else{

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== CAMERA_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED ){

            }else{
                Toast.makeText(requireContext(), "premission denied", Toast.LENGTH_SHORT ).show()
            }
        }
    }

    companion object {
        val CODE_PROFILE = 3
        val REQUEST_CODE_PROFILE = 100
        val CODE_DOCUMENT_FROM_CAMERA = 2
        val REQUEST_CODE_DOCUMENT_FROM_CAMERA = 101
        var CAMERA_REQUEST_CODE= 123
    }


}