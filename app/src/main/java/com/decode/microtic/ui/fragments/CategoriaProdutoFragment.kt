package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.decode.microtic.R
import com.decode.microtic.data.adapters.ProdutsAdapter
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentCategoriaProdutoBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.viewmodels.CategoriaProdutoViewModel
import com.decode.microtic.ui.viewmodels.CategoriasViewModel

class CategoriaProdutoFragment : Fragment() {

    lateinit var binding : FragmentCategoriaProdutoBinding
    lateinit var viewModel : CategoriaProdutoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_categoria_produto, container, false)
        binding = FragmentCategoriaProdutoBinding.bind(view)
        viewModel = ViewModelProvider(this).get(CategoriaProdutoViewModel::class.java)
        var bundle = requireActivity().intent.getParcelableExtra<Categorias>("categorias")
        binding.txtCategoriaProduto.text = bundle!!.categoria
        viewModel.getDevicesByCategory(bundle!!)

        viewModel.categoriaList.observeForever {
            var adapter = ProdutsAdapter(it, requireContext())
            binding.rcvProduts.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
            if (it.size>0){
                binding.txtInfo.visibility =View.GONE
            }
        }
        return binding.root
    }


}