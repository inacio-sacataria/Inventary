package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.decode.microtic.R
import com.decode.microtic.data.adapters.WorkersAdapter
import com.decode.microtic.databinding.FragmentWorkersBinding
import com.decode.microtic.ui.viewmodels.AlocacoesViewModel


class WorkersFragment : Fragment() {

    lateinit var binding : FragmentWorkersBinding
    lateinit var viewModel: AlocacoesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_workers, container, false)
        binding = FragmentWorkersBinding.bind(view)
        viewModel =ViewModelProvider(this).get(AlocacoesViewModel::class.java)

        binding.rcvWorkers.adapter = WorkersAdapter(requireActivity())

        return binding.root
    }

}