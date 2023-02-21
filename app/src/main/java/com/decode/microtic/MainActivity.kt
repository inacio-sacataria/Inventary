package com.decode.microtic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.decode.microtic.databinding.ActivityMainBinding
import com.decode.microtic.ui.activities.ProdutScanActivity
import com.decode.microtic.ui.fragments.HomeFragment
import com.decode.microtic.ui.fragments.QrFragment
import com.decode.microtic.ui.fragments.WorkersFragment
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel
import com.decode.microtic.ui.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    lateinit var menuu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.user.observe(this){
            if (it !=null)
            {if (it.role.equals("user")){
                startActivity(Intent(this,ProdutScanActivity::class.java))
            }
        }}
        setContentView(binding.root)

        setCurrentFragment(HomeFragment())

        binding.bottomBar.setOnItemSelectedListener {
           when(it){
               0 -> setCurrentFragment(HomeFragment())
               1 -> setCurrentFragment(QrFragment())
               2 -> setCurrentFragment(WorkersFragment())
           }
        }

        }

    private fun setCurrentFragment(fragment: Fragment) {
      var transaction= supportFragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.user.observe(this) {
            if (it != null) {
                if (it.role.equals("user")) {
                    startActivity(Intent(this, ProdutScanActivity::class.java))
                }
            }
        }
    }
}