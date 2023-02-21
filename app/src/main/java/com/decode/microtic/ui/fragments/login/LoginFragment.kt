package com.decode.microtic.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.decode.microtic.MainActivity
import com.decode.microtic.R
import com.decode.microtic.databinding.FragmentLoginBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.activities.ProdutScanActivity
import com.decode.microtic.ui.viewmodels.UserViewModel

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.bind(view)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.button.setOnClickListener {
            fillData()
        }

        binding.txtCreateAcount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registUser)
        }

        return binding.root
    }

    fun fillData(){
        var email = binding.editEmail.text.toString()
        var password = binding.editPassword.text.toString()

        if (!email.isNullOrEmpty() && !email.isNullOrEmpty()){
            viewModel.login(email , password)
            viewModel.getCurrentUser()
            viewModel.user.observe(viewLifecycleOwner){
                if (it !=null){
                    if (it.role.equals("user")) {

                        var i= Intent(requireActivity(), ProdutScanActivity::class.java)
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)

                    }else{

                        var i= Intent(requireActivity(),MainActivity::class.java)
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)

                    }
                }
            }
        }else{
            requireActivity().showInfo("Preencha todos campos")
        }
    }

}