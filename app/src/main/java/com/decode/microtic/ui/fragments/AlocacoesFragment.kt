package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.decode.microtic.R
import com.decode.microtic.compareDates
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentAlocacoesBinding
import com.decode.microtic.showInfo
import com.decode.microtic.timePickerdialog
import com.decode.microtic.ui.viewmodels.AlocacoesViewModel

class AlocacoesFragment : Fragment() {

    lateinit var binding : FragmentAlocacoesBinding
    lateinit var viewModel: AlocacoesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_alocacoes, container, false)
        binding = FragmentAlocacoesBinding.bind(view)
        viewModel = ViewModelProvider(this).get()

        binding.lytDataAlocacoa.setOnClickListener {
            timePickerdialog(binding.edtDataAlocacoa,requireActivity())
        }

        binding.lytFimAlocacao.setOnClickListener {
            timePickerdialog(binding.editFimAlocacao,requireActivity())
        }

        binding.edtDataAlocacoa.setOnClickListener {
            timePickerdialog(binding.edtDataAlocacoa,requireActivity())
        }

        binding.editFimAlocacao.setOnClickListener {
            timePickerdialog(binding.editFimAlocacao,requireActivity())
        }

        binding.btnContinue.setOnClickListener {
            fillData()

        }

        return binding.root
    }

    fun fillData(){
        var responsavel = binding.edtResposanvel.selectedItem.toString()
        var periodAlocacoes = binding.editContratoTempo.text.toString()
        var dataInicio = binding.edtDataAlocacoa.text.toString()
        var dataFinal = binding.editFimAlocacao.text.toString()

        if(!responsavel.equals("--Selecione Colaborador--", true)){
            if (!responsavel.isNullOrEmpty() && !periodAlocacoes.isNullOrEmpty() && !dataInicio.isNullOrEmpty() && !dataFinal.isNullOrEmpty()){
                var testDate : Boolean =  requireActivity().compareDates(binding.edtDataAlocacoa.text.toString(),binding.editFimAlocacao.text.toString())
                if(testDate==true) {
                    var devices = requireActivity().intent.getParcelableExtra<Devices>("device")
                    viewModel.createAlocacoes(
                        responsavel,
                        periodAlocacoes,
                        dataInicio,
                        dataFinal,
                        devices!!
                    )
                    requireActivity().onBackPressed()
                }
            }
        }else{
            requireActivity().showInfo("Por favor selecione o colaborador")
        }
    }

}