package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.decode.microtic.R
import com.decode.microtic.compareProdutDates
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentRegistProdutsBinding
import com.decode.microtic.showInfo
import com.decode.microtic.timePickerdialog
import com.decode.microtic.ui.viewmodels.RegistDevicesViewModel
import populate


class RegistProdutsFragment : Fragment() {

    lateinit var viewModel : RegistDevicesViewModel
    var cardVisibilityController = true
    lateinit var binding: FragmentRegistProdutsBinding
    var qrcode : String? = ""
     var bundleDevice : Devices? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_regist_produts, container, false)
        binding= FragmentRegistProdutsBinding.bind(view)
        viewModel = ViewModelProvider(this).get(RegistDevicesViewModel::class.java)


        viewModel.liveData.observe(viewLifecycleOwner) {
            // var array = ArrayList()
            var array : MutableList<String> = ArrayList()
            array.add("--Selecione Categoria--")
            it.forEach {
                array.add(it)
            }
            binding.spinnerDocType.populate(requireActivity(),array)
        }
        var bundle = requireActivity().intent.getStringExtra("qrCode")
        qrcode = bundle.toString()
        if (!qrcode.isNullOrEmpty()){
            binding.edtQrNumber.setText(qrcode)
        }
        editDevice()

        binding.lytEmissionDate.setOnClickListener {
            timePickerdialog(binding.edtDataAquisicao,requireActivity())
        }

        binding.lytExpireDate.setOnClickListener {
            timePickerdialog(binding.edtGarantiaData,requireActivity())
        }

        binding.edtDataAquisicao.setOnClickListener {
            timePickerdialog(binding.edtDataAquisicao,requireActivity())
        }

        binding.edtGarantiaData.setOnClickListener {
            timePickerdialog(binding.edtGarantiaData,requireActivity())
        }
        binding.btnContinue.setOnClickListener {
            proceedToTheNextCard()
            // finish()
        }

        return binding.root
    }


    fun proceedToTheNextCard(){
        binding.btnContinue.setOnClickListener {
            if (cardVisibilityController){
                if (
                    !binding.edtQrNumber.text.isNullOrEmpty() &&
                    !binding.edtNomeProduto.text.isNullOrEmpty() &&
                    !binding.edtMarca.text.isNullOrEmpty() &&
                    !binding.edtModelo.text.isNullOrEmpty() &&
                    !binding.edtPrice.text.isNullOrEmpty()
                ){
                    binding.cardPersonaldataSection.visibility= View.GONE
                    binding.cardDocumentSection.visibility= View.VISIBLE
                    cardVisibilityController=false
                }else{
                    requireActivity().showInfo("Fill all fields")
                }
            }else{
                if (
                    !binding.edtDescricao.text.isNullOrEmpty() &&
                    !binding.edtSerie.text.isNullOrEmpty() &&
                    !binding.edtDataAquisicao.text.isNullOrEmpty() &&
                    !binding.edtGarantiaData.text.isNullOrEmpty()
                ){

                    if(!binding.spinnerDocType.selectedItem.toString().equals("--Selecione Categoria--") || !binding.spinnerCondicao.selectedItem.toString().equals("--Selecione Estado--") ){

                        var dateTest = requireActivity().compareProdutDates(
                            binding.edtDataAquisicao.text.toString(),
                            binding.edtGarantiaData.text.toString(),
                        )

                        if (dateTest==true){
                        addData(
                            binding.edtQrNumber.text.toString(),
                            binding.edtMarca.text.toString(),
                            binding.edtModelo.text.toString(),
                            binding.edtPrice.text.toString(),
                            binding.edtDescricao.text.toString(),
                            binding.spinnerDocType.selectedItem.toString(),
                            binding.edtDataAquisicao.text.toString(),
                            binding.edtGarantiaData.text.toString(),
                            binding.edtSerie.text.toString(),
                            binding.spinnerCondicao.selectedItem.toString(),
                            binding.edtNomeProduto.text.toString()
                        )}

                        }else {
                        requireActivity().showInfo("Selecione as opções")
                    }
                }else{
                    requireActivity().showInfo("Fill all fields")
                }
            }
        }
    }

    private fun addData(
        Qr: String,
        marca: String,
        modelo: String,
        price: String,
        descricao: String,
        tipo: String,
        dataDeAquisicao: String,
        dataGarantia: String,
        serie: String,
        condicao: String,
        nomeProduto: String
    ) {
        if (bundleDevice==null){
            viewModel.insertData(Qr,marca,modelo,price,descricao,tipo,dataDeAquisicao,dataGarantia, serie, condicao,nomeProduto)
            requireActivity().showInfo("Activo ${nomeProduto} adicionado com sucesso")
            findNavController().navigate(R.id.choosePhotoFrgment)
        }else{
            viewModel.update(bundleDevice!!.id!!,Qr,marca,modelo,price,descricao,tipo,dataDeAquisicao,dataGarantia, serie, condicao, bundleDevice!!.photoUrls,nomeProduto)
            requireActivity().showInfo("Activo ${nomeProduto} atulizado com sucesso")
            requireActivity().finish()
        }

    }


    fun editDevice (){
       bundleDevice = requireActivity().intent.getParcelableExtra<Devices>("device")
        if (bundleDevice!=null){
            var device : Devices = bundleDevice as Devices

            if (device!=null){
                binding.edtMarca.setText( device.marca)
                binding.edtModelo.setText( device.modelo)
                binding.edtSerie.setText( device.serie)
                binding.edtDescricao.setText( device.decricao)
                binding.edtPrice.setText( device.priceDeAquisicao)
                binding.edtNomeProduto.setText( device.nomeProduto)
                binding.edtDataAquisicao.setText(device.dataDeAquisicao)
                binding.edtGarantiaData.setText(device.dataDeGarantia)
                binding.edtQrNumber.setText(device.qrCode)
            }
        }

    }
}