package com.decode.microtic.ui.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.decode.microtic.R
import com.decode.microtic.databinding.ActivityInfoProductBinding
import com.decode.microtic.databinding.FragmentHomeBinding
import com.decode.microtic.deviceClicked
import com.decode.microtic.IDCLICK
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel
import com.decode.microtic.ui.viewmodels.RegistDevicesViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class InfoProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfoProductBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}