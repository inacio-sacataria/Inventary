package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.decode.microtic.R
import com.decode.microtic.databinding.FragmentAddCategoriaBinding
import com.decode.microtic.databinding.FragmentInfoProductBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.viewmodels.CategoriasViewModel

class AddCategoriaFragment : Fragment() {
    lateinit var binding: FragmentAddCategoriaBinding
    lateinit var viewModel: CategoriasViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_add_categoria, container, false)
        binding = FragmentAddCategoriaBinding.bind(view)
        viewModel = ViewModelProvider(this).get(CategoriasViewModel::class.java)
        binding.btnContinue.setOnClickListener {
            add()
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    fun add(){
        var categoria = binding.editCategoria.text.toString()
        var descricao = binding.editDescricao.text.toString()

        if (!categoria.isNullOrEmpty() && !categoria.isNullOrEmpty()){
            viewModel.addCategoria(categoria, descricao)
        }else{
            requireActivity().showInfo("Por gavor preencha todos campo")
        }
    }


}