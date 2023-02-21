package com.decode.microtic.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.decode.microtic.databinding.ActivityRegistBinding

class Registrationctivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistBinding
    var qrcode: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}