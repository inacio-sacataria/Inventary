package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.decode.microtic.R
import com.decode.microtic.data.adapters.AlocacoesAdapter
import com.decode.microtic.databinding.FragmentAlocacoesBinding
import com.decode.microtic.databinding.FragmentAlocacoesListBinding
import com.decode.microtic.databinding.FragmentWorkersBinding
import com.decode.microtic.ui.viewmodels.AlocacoesViewModel


class AlocacoesListFragment : Fragment() {

    lateinit var binding: FragmentAlocacoesListBinding
    lateinit var viewModel: AlocacoesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_alocacoes_list, container, false)
        binding = FragmentAlocacoesListBinding.bind(view)
        viewModel = ViewModelProvider(this).get(AlocacoesViewModel::class.java)

        var worker : String? = requireActivity().intent.getStringExtra("worker")
        binding.txtWorkerName.setText(worker)
        viewModel.getDevicesByAlocation(worker!!)
        binding.progress.visibility = View.GONE
        viewModel.alocacoesList.observe(viewLifecycleOwner,
            {
                var adapter = AlocacoesAdapter(it, requireActivity())
                binding.rcvWorkers.adapter = adapter
                if (it.size>0){
                    binding.txtInfo.visibility =View.GONE

                }
            }
        )

        return binding.root
    }


}