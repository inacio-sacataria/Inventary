package com.decode.microtic.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.decode.microtic.MainActivity
import com.decode.microtic.R
import com.decode.microtic.databinding.FragmentAlocacoesListBinding
import com.decode.microtic.databinding.FragmentRegistUserBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.activities.ProdutScanActivity
import com.decode.microtic.ui.activities.Registrationctivity
import com.decode.microtic.ui.viewmodels.RegistDevicesViewModel
import com.decode.microtic.ui.viewmodels.UserViewModel


class RegistUserFragment : Fragment() {
    lateinit var binding: FragmentRegistUserBinding
    lateinit var viewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_regist_user, container, false)
        binding = FragmentRegistUserBinding.bind(view)
        viewModel = ViewModelProvider(this).get()

        binding.button.setOnClickListener {
            createUser()
        }

        binding.txtHaveAcount.setOnClickListener {
            findNavController().navigate(R.id.action_registUser_to_loginFragment)
        }
        return binding.root
    }


    fun createUser(){
        var nome = binding.editNome.text.toString()
        var email = binding.editEmail.text.toString()
        var password = binding.editPassword.text.toString()

        if(!nome.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            binding.progressBar2.visibility = View.VISIBLE
            viewModel.create(nome, email, password)
            binding.progressBar2.visibility = View.GONE
            startActivity(Intent(requireActivity(),MainActivity::class.java))


        }
        else
            requireActivity().showInfo("Falha ao registrar")
    }

}