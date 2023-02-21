package com.decode.microtic.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.decode.microtic.MainActivity
import com.decode.microtic.R
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentPhotoBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.activities.LoginActivity
import com.decode.microtic.ui.viewmodels.AddPhotoViewModel
import com.decode.microtic.ui.viewmodels.RegistDevicesViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PhotoFragment : Fragment() {

    lateinit var binding : FragmentPhotoBinding
    var documentUrl: String=""
    lateinit var viewModel: RegistDevicesViewModel
    var fotos = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_photo, container, false)
        binding = FragmentPhotoBinding.bind(view)
        viewModel = ViewModelProvider(this).get(RegistDevicesViewModel::class.java)
        binding.btnScanDoc.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)
        }

        binding.btnContinue.setOnClickListener {
            if (fotos.size!=0){
                var device = requireActivity().intent.getParcelableExtra<Devices>("device")
                runBlocking{
                    if (device==null) {
                        viewModel.addPhotoUrl(fotos)
                    }else{
                        device.photoUrls = fotos
                        viewModel.updatePhotoUrl(device)
                    }
                }
                var intent = Intent(requireActivity(), MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }else{
                requireActivity().showInfo("Por favor tire uma foto do produto")
            }
        }
        return binding.root
    }

    private fun checkPermission(permission: String, requestCode: Int){
        if(ContextCompat.checkSelfPermission(requireContext(),permission)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission),requestCode  )
        }else{
            selectImage(CODE_DOCUMENT_FROM_CAMERA)
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
                selectImage(CODE_DOCUMENT_FROM_CAMERA)
            }else{
                Toast.makeText(requireContext(), "premission denied", Toast.LENGTH_SHORT ).show()
            }
        }
    }

    public fun selectImage(number: Int){

        try {
            GlobalScope.launch(Dispatchers.IO){

                Log.d("upload", "run on startactivity result")
                if (number== CODE_DOCUMENT_FROM_CAMERA) {
                    val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")
                    startActivityForResult(cameraIntent, 101)
                }
                if (number== CODE_PROFILE){
                    val intent= Intent()
                    intent.setType("image/*")
                    intent.setAction( Intent.ACTION_GET_CONTENT)
                    startActivityForResult(intent, 101)
                }
            }
        }catch (e: Exception){
            Log.e("upload"," upload ${e.message.toString()}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // if(number== CODE_DOCUMENT_FROM_CAMERA){
        if (requestCode == REQUEST_CODE_DOCUMENT_FROM_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            onCaptureResult(data)
        }

        if(requestCode == REQUEST_CODE_PROFILE && resultCode == Activity.RESULT_OK && data != null)
        {
            var imageURI= data?.data!!
            uploadImage(imageURI)
        }else{
            //  Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show()
        }
        // }

    }

    fun onCaptureResult(data : Intent){
        var thumbnail: Bitmap = data.extras?.get("data") as Bitmap
        var bytes : ByteArrayOutputStream = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        var bb = bytes.toByteArray()
        var file = android.util.Base64.encodeToString(bb,android.util.Base64.DEFAULT)
        binding.imgDoc.setImageBitmap(thumbnail)


        uploadtoFirebase(bb)
    }

    fun uploadtoFirebase(bb: ByteArray){
        val progressDialog= ProgressDialog(requireActivity())
        progressDialog.setMessage("Carregando a photo....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val  formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now= Date()
        val filename= formatter.format(now)
        val storageRef= FirebaseStorage.getInstance().getReference("images/$filename")
        storageRef.putBytes(bb).addOnSuccessListener {

            if (it.task.isSuccessful){
                Toast.makeText(requireActivity(), "complete upload", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                binding.progressDocLoading.visibility = View.GONE
                binding.btnScanDoc.text="Add mais fotos"
                storageRef.downloadUrl.addOnSuccessListener {
                    documentUrl= it.toString()
                    fotos.add(documentUrl)
                    for (s in fotos){
                        Log.d("Fotos","${s}")
                    }
                }.addOnFailureListener {
                    requireContext().showInfo("Failed to download")
                }

            }



            //binding.imageChose.setImageURI(imageURI)
        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(requireActivity(), "failed upload", Toast.LENGTH_SHORT).show()
        }
    }

    public fun uploadImage(imageURI: Uri){
        val progressDialog= ProgressDialog(requireActivity())
        progressDialog.setMessage("Upload you profile....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val  formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now= Date()
        val filename= formatter.format(now)
        val storageRef= FirebaseStorage.getInstance().getReference("images/$filename")
        storageRef.putFile(imageURI).addOnSuccessListener {
            Toast.makeText(requireActivity(), "complete upload", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()



        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(requireActivity(), "failed upload", Toast.LENGTH_SHORT).show()
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