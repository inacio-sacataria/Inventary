package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.decode.microtic.R
import com.decode.microtic.data.adapters.CategoriaFramentAdapter
import com.decode.microtic.databinding.FragmentCategoriaBinding
import com.decode.microtic.ui.viewmodels.CategoriasViewModel


class CategoriaFragment : Fragment() {
    lateinit var binding : FragmentCategoriaBinding
    lateinit var viewModel : CategoriasViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_categoria, container, false)
        binding = FragmentCategoriaBinding.bind(view)
        viewModel = ViewModelProvider(this).get(CategoriasViewModel::class.java)

        viewModel._categorias.observeForever {
            var adapter = CategoriaFramentAdapter(it, requireActivity())
            binding.rcvCategorias.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_categoriaFragment_to_addCategoriaFragment)
        }
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        viewModel.getCategoria()
    }

}