package com.decode.microtic.ui.fragments

import android.app.ProgressDialog
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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.decode.microtic.R
import com.decode.microtic.showInfo
import com.decode.microtic.utils.Contants
import com.decode.microtic.utils.Contants.mutablePhotosUrl
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.invoke.ConstantCallSite
import java.text.SimpleDateFormat
import java.util.*


class PhotoPickerFragment : Fragment() {
    var fotos = mutableListOf<String>()
    var bitmap: Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_picker, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chooseFromPhotoPicker()
    }

    fun chooseFromPhotoPicker(){
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {

                    uris.forEach {
                        Log.d("PhotoPickerUri", "Number of items selected: ${it}")
                        pickerImageUpload(it)
                    }
                }.invokeOnCompletion {
                    Contants.isDone = true
                   requireActivity().onBackPressed()
                }
            } else {
                Log.d("PhotoPickerUri", "No media selected")
            }
        }

        // Launch the photo picker and allow the user to choose only images.
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun pickerImageUpload(uri: Uri){
        var imageURI= uri

        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,imageURI) as Bitmap
            var bytes : ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG,50,bytes)
            if (imageURI != null) {
                uploadImage(imageURI)
            }
        }catch (e: IOException){
            Log.d("upload", "photo not uploaded ${e.message}")
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
                Contants.mutablePhotosUrl.add(it.toString())
                if (progressDialog.isShowing) progressDialog.dismiss()
               Log.d("photoPickerHey","broooo done")
            }.addOnFailureListener {
                requireActivity().showInfo("Failed to download")
            }

        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(requireContext(), "failed upload", Toast.LENGTH_SHORT).show()
        }
    }
}