package com.decode.microtic.ui.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.decode.microtic.MainActivity
import com.decode.microtic.R
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentPhotoBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.viewmodels.RegistDevicesViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ChoosePhotoFrgment : Fragment() {

    lateinit var binding : FragmentPhotoBinding
    var documentUrl: String=""
    lateinit var viewModel: RegistDevicesViewModel
    var fotos = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_photo, container, false)
        binding = FragmentPhotoBinding.bind(view)
        viewModel = ViewModelProvider(this).get(RegistDevicesViewModel::class.java)

        binding.btnScanDoc.setOnClickListener {
            selectImage()
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



    public fun selectImage(){
        try {
            val intent= Intent()
            intent.setType("image/*")
            intent.setAction( Intent.ACTION_GET_CONTENT)

            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select Image from here..."),
                REQUEST_CODE);

        }catch (e: Exception){
            Log.e("upload"," upload ${e.message.toString()}")
        }
    }

    private fun uploadImage(imageURI: Uri){
        val progressDialog= ProgressDialog(requireContext())
        progressDialog.setMessage("Upload cover....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val  formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now= Date()
        val filename= formatter.format(now)
        val cover ="images/$filename"
        val storageRef= FirebaseStorage.getInstance().getReference(cover)
        storageRef.putFile(imageURI).addOnSuccessListener {

            storageRef.downloadUrl.addOnSuccessListener {
                fotos.add(it.toString())
                GlobalScope.launch(Dispatchers.Main){
                    Glide
                    .with(requireActivity())
                    .load(it.toString())
                        .into(binding.imgDoc)
                }
                if (progressDialog.isShowing) progressDialog.dismiss()
            }.addOnFailureListener {
                requireActivity().showInfo("Failed to download")
            }

        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(requireContext(), "failed upload", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadtoFirebase(bb: ByteArray){
        val progressDialog= ProgressDialog(requireActivity())
        progressDialog.setMessage("Scaning your User License....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val  formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now= Date()
        val filename= formatter.format(now)
        val storageRef= FirebaseStorage.getInstance().getReference("images/$filename")

        storageRef.putBytes(bb).addOnSuccessListener {

            if (it.task.isSuccessful){
                storageRef.downloadUrl.addOnSuccessListener {
                    fotos.add( it.toString())
                    Toast.makeText(requireContext(), "complete upload", Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }.addOnFailureListener {
                    requireActivity().showInfo("Failed to download")
                }
            }
        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            requireActivity().showInfo("Failed to Upload")
        }
    }


    var bitmap: Bitmap? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            var imageURI= data?.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,imageURI) as Bitmap
                var bytes : ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG,50,bytes)
                binding.imgDoc.setImageBitmap(bitmap)
                if (imageURI != null) {
                    uploadImage(imageURI)
                }
            }catch (e: IOException){
                Log.d("upload", "photo not uploaded ${e.message}")
            }

        }else{
            Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        val REQUEST_CODE = 100
    }

}