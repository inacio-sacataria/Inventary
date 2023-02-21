package com.decode.microtic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.decode.microtic.R
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentInfoProductBinding
import com.decode.microtic.deviceClicked
import com.decode.microtic.showInfo
import com.decode.microtic.ui.activities.Registrationctivity
import com.decode.microtic.ui.viewmodels.AlocacoesViewModel
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class InfoProductFragment : Fragment() {
    lateinit var binding : FragmentInfoProductBinding
    lateinit var viewModel: HomeFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_info_product, container, false)
        binding = FragmentInfoProductBinding.bind(view)

        viewModel=ViewModelProvider(this).get()
        fillData()


        return binding.root
    }

    override fun onResume() {

        fillData()
        super.onResume()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("resumido","fragment is Detach....")
    }

    override fun onStart() {
        super.onStart()
        Log.d("resumido","fragment is Start....")
    }

    override fun onPause() {
        super.onPause()
        Log.d("resumido","fragment is Pause....")
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
        binding.txtPrice.text = device.priceDeAquisicao
        binding.txtEstado.text = device.condicoes
        binding.txtNome.text = device.nomeProduto

        GlobalScope.launch (Dispatchers.Main){
            binding.imageSlider.setImageList(imageList)
            binding.imageSlider.stopSliding()




        binding.imageSlider.setItemClickListener(object : ItemClickListener{
            override fun onItemSelected(position: Int) {
                requireActivity().intent.putExtra("device", device)
                findNavController().navigate(R.id.fullScreenImageFragment)
            }

        })
        }

        binding.btnImgDelete.setOnClickListener {
           dialogShow(device)
        }



        if (device.alocado==true) {
            binding.txtAlocado.text = "Alocado a : ${device.responsavel}"
            binding.txtAlocado.visibility= View.VISIBLE
            binding.btnAlocate.text = "Desalocar Item"
            binding.btnVerAlocacoes.visibility = View.VISIBLE
        }else{
            binding.txtAlocado.visibility= View.GONE
            binding.btnAlocate.text = "Alocar Item"
            binding.btnVerAlocacoes.visibility = View.GONE

        }

        binding.btnAlocate.setOnClickListener {
            if (device.alocado==false){
                requireActivity().intent.putExtra("device", device)
                findNavController().navigate(R.id.alocacoesFragment2)
            }else{
                viewModel.dealocate(device)
                requireActivity().showInfo("Item desalocado")
                responsavel = device!!.responsavel.toString();
                binding.btnVerAlocacoes.visibility = View.VISIBLE
                fillData()
            }

        }

        binding.btnImageView.setOnClickListener {
            var intent = Intent(requireActivity(),Registrationctivity::class.java)
            intent.putExtra("device",device)
            startActivity(intent)
        }


        binding.btnVerAlocacoes.setOnClickListener {
            requireActivity().intent.putExtra("worker",device.responsavel)
            findNavController().navigate(R.id.alocacoesListFragment2)
        }

        binding.btnCamera.setOnClickListener {
           requireActivity().intent.putExtra("device",device)
          findNavController().navigate(R.id.choosePhotoFrgment2)
        }
    }

    fun dialogShow(device : Devices){
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(("Deseja apgar este Activo?"))
            .setMessage(("Apagando o Activo , significa que nao vai poder recupera-lo caso precise novamente"))
            .setPositiveButton("Apagar") { dialog, which ->
                viewModel.delete(device)
                requireActivity().finish()
            }
            .setNegativeButton("Cancelar") { dialog, which ->


            }.setCancelable(false)
            .show()
    }
}