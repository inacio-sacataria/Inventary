package com.decode.microtic.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.decode.microtic.MainActivity
import com.decode.microtic.R
import com.decode.microtic.data.AuthInstance
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel
import com.decode.microtic.ui.viewmodels.UserViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.user.observe(this) {
            if (it != null) {
                if (it.role.equals("user")) {
                    startActivity(Intent(this, ProdutScanActivity::class.java))
                }else{
                    startActivity(Intent(this,MainActivity::class.java))
                }
            }
        }
    }
}