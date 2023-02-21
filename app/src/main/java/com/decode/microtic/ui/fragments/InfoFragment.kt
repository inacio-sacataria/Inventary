package com.decode.microtic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.decode.microtic.R
import com.decode.microtic.data.AuthInstance
import com.decode.microtic.databinding.FragmentInfoBinding
import com.decode.microtic.deviceClicked
import com.decode.microtic.ui.activities.LoginActivity
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class InfoFragment : Fragment() {
    lateinit var binding : FragmentInfoBinding
    lateinit var viewModel: HomeFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_info, container, false)
        binding = FragmentInfoBinding.bind(view)

        viewModel= ViewModelProvider(this).get()
        fillData()

        return binding.root
    }

    override fun onResume() {

        fillData()
        super.onResume()
    }


    fun fillData(){
        val imageList = ArrayList<SlideModel>()
        var responsavel = ""
        var device = deviceClicked
        for (url in device!!.photoUrls){
            imageList.add(SlideModel(url, ScaleTypes.CENTER_CROP))
        }

        binding.txtBrand.text = device.marca
        binding.txtDescription.text = device.decricao
        binding.txtModel.text = device.modelo
        binding.txtQrCode.text = device.qrCode
        binding.txtSerie.text = device.serie
        binding.txtDateAquisition.text = device.dataDeAquisicao
        binding.txtDateGarantia.text = device.dataDeGarantia
        binding.txtCategoria.text = device.tipo
        binding.txtEstado.text = device.condicoes
        binding.txtNome.text = device.nomeProduto


        GlobalScope.launch (Dispatchers.Main) {
            binding.imageSlider.setImageList(imageList)
            binding.imageSlider.stopSliding()

            if (device.alocado == true) {
                binding.txtAlocado.text = "Alocado a : ${device.responsavel}"
                binding.txtAlocado.visibility = View.VISIBLE
            } else {
                binding.txtAlocado.visibility = View.GONE
            }

            binding.imageSlider.setItemClickListener(object : ItemClickListener {
                override fun onItemSelected(position: Int) {
                    requireActivity().intent.putExtra("device", device)
                    findNavController().navigate(R.id.fullScreenImageFragment2)
                }

            })
        }
    }
}