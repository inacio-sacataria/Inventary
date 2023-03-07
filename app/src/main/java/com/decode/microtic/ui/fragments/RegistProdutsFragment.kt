package com.decode.microtic.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.decode.microtic.*
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentRegistProdutsBinding
import com.decode.microtic.ui.viewmodels.RegistDevicesViewModel
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import populate


class RegistProdutsFragment : Fragment() {

    lateinit var viewModel: RegistDevicesViewModel
    var cardVisibilityController = true
    lateinit var binding: FragmentRegistProdutsBinding
    var qrcode: String? = ""
    var bundleDevice: Devices? = null
    var startDate = ""
    var daysCount = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_regist_produts, container, false)
        binding = FragmentRegistProdutsBinding.bind(view)
        viewModel = ViewModelProvider(this).get(RegistDevicesViewModel::class.java)


        var bundle = requireActivity().intent.getStringExtra("qrCode")
        bundleDevice = requireActivity().intent.getParcelableExtra<Devices>("device")

        viewModel.liveData.observe(viewLifecycleOwner) {
            // var array = ArrayList()
            var array: MutableList<String> = ArrayList()
            if (bundleDevice == null) {
                array.add("--Selecione Categoria--")
                it.forEach {
                    array.add(it)
                }
            } else {
                var categoria = bundleDevice!!.tipo
                array.add(categoria!!)
                it.forEach {
                    if (!categoria.equals(it))
                        array.add(it)
                }
                var estadoList: MutableList<String> = ArrayList()
                var estado = bundleDevice!!.condicoes
                estadoList.add(estado!!)
                if (estado.equals("Operacional"))
                    estadoList.add("Avariado")
                else
                    estadoList.add("Operacional")

                binding.spinnerCondicao.populate(requireActivity(), estadoList)
            }
            binding.spinnerDocType.populate(requireActivity(), array)
        }

        qrcode = bundle.toString()
        if (!qrcode.isNullOrEmpty()) {
            binding.edtQrNumber.setText(qrcode)
        }
        editDevice()

        binding.lytEmissionDate.setOnClickListener {
            timePickerdialog(binding.edtDataAquisicao, requireActivity())
            startDate = binding.edtDataAquisicao.text.toString()
            if (daysCount!=0 && !startDate.isNullOrEmpty()){
                binding.edtGarantiaData.text = requireContext().calculateEndDate(startDate, daysCount)
            }

        }

        binding.lytExpireDate.setOnClickListener {
            timePickerdialog(binding.edtGarantiaData, requireActivity())
        }

        binding.edtDataAquisicao.setOnClickListener {
            timePickerdialog(binding.edtDataAquisicao, requireActivity())
            startDate = binding.edtDataAquisicao.text.toString()
            if (daysCount!=0 && !startDate.isNullOrEmpty()){
                binding.edtGarantiaData.text = requireContext().calculateEndDate(startDate, daysCount)
            }

        }

        binding.edtGarantiaData.setOnClickListener {
            timePickerdialog(binding.edtGarantiaData, requireActivity())
        }
        binding.btnContinue.setOnClickListener {
            proceedToTheNextCard()
            // finish()
        }

        binding.spinnerDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when(position){
                   1 -> {
                       daysCount= 30
                       binding.edtGarantiaData.visibility = View.GONE
                   }
                    2 -> {
                        daysCount= 90
                        binding.edtGarantiaData.visibility = View.GONE
                    }
                    3 -> {
                        daysCount= 180
                        binding.edtGarantiaData.visibility = View.GONE
                    }
                    4 -> {
                        daysCount= 365
                        binding.edtGarantiaData.visibility = View.GONE
                    }
                    5 -> {
                        daysCount= 730
                        binding.edtGarantiaData.visibility =  View.GONE
                    }

                    6 -> {
                       binding.edtGarantiaData.visibility =  View.VISIBLE
                    }
                }

                if (daysCount!=0 && !startDate.isNullOrEmpty()){
                    binding.edtGarantiaData.text = requireContext().calculateEndDate(startDate,daysCount)
                }else{
                    Toast.makeText(requireContext(), "Escolha a data de aquisicao", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun proceedToTheNextCard() {
        binding.btnContinue.setOnClickListener {
            if (cardVisibilityController) {
                if (
                    !binding.edtQrNumber.text.isNullOrEmpty() &&
                    !binding.edtNomeProduto.text.isNullOrEmpty() &&
                    !binding.edtPrice.text.isNullOrEmpty()
                ) {
                    binding.cardPersonaldataSection.visibility = View.GONE
                    binding.cardDocumentSection.visibility = View.VISIBLE
                    cardVisibilityController = false
                } else {
                    requireActivity().showInfo("Fill all fields")
                }
            } else {
                if (
                    !binding.edtDescricao.text.isNullOrEmpty() &&
                    !binding.edtDataAquisicao.text.isNullOrEmpty() &&
                    !binding.edtGarantiaData.text.isNullOrEmpty()
                ) {

                    if (!binding.spinnerDocType.selectedItem.toString()
                            .equals("--Selecione Categoria--") || !binding.spinnerCondicao.selectedItem.toString()
                            .equals("--Selecione Estado--")
                    ) {

                        var dateTest = requireActivity().compareProdutDates(
                            binding.edtDataAquisicao.text.toString(),
                            binding.edtGarantiaData.text.toString(),
                        )

                        if (dateTest == true) {
                            var marca: String = if (!binding.edtMarca.text.toString()
                                    .isNullOrEmpty()
                            ) binding.edtMarca.text.toString() else "Sem Marca"
                            var modelo: String = if (!binding.edtModelo.text.toString()
                                    .isNullOrEmpty()
                            ) binding.edtModelo.text.toString() else "Sem Modelo"
                            var serie: String = if (!binding.edtSerie.text.toString()
                                    .isNullOrEmpty()
                            ) binding.edtSerie.text.toString() else "Sem Serie"

                            if (daysCount!=0 && !startDate.isNullOrEmpty()){
                                binding.edtGarantiaData.text = requireContext().calculateEndDate(startDate,daysCount)
                            }
                            addData(
                                binding.edtQrNumber.text.toString(),
                                marca,
                                modelo,
                                binding.edtPrice.text.toString(),
                                binding.edtDescricao.text.toString(),
                                binding.spinnerDocType.selectedItem.toString(),
                                binding.edtDataAquisicao.text.toString(),
                                binding.edtGarantiaData.text.toString(),
                                serie,
                                binding.spinnerCondicao.selectedItem.toString(),
                                binding.edtNomeProduto.text.toString()
                            )
                        }

                    } else {
                        requireActivity().showInfo("Selecione as opções")
                    }
                } else {
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
        if (bundleDevice == null) {
            viewModel.insertData(
                Qr,
                marca,
                modelo,
                price,
                descricao,
                tipo,
                dataDeAquisicao,
                dataGarantia,
                serie,
                condicao,
                nomeProduto
            )
            requireActivity().showInfo("Activo ${nomeProduto} adicionado com sucesso")
            findNavController().navigate(R.id.choosePhotoFrgment)
        } else {
            var device = Devices(
                id = bundleDevice!!.id!!,
                qrCode = Qr,
                marca = marca,
                modelo = modelo,
                priceDeAquisicao = price,
                decricao = descricao,
                tipo = tipo,
                dataDeAquisicao = dataDeAquisicao,
                dataDeGarantia = dataGarantia,
                serie = serie,
                photoUrls = bundleDevice!!.photoUrls,
                alocado = bundleDevice!!.alocado,
                responsavel = bundleDevice!!.responsavel,
                condicoes = condicao,
                nomeProduto = nomeProduto
            )
            viewModel.update(device)
            requireActivity().showInfo("Activo ${nomeProduto} atulizado com sucesso")
            requireActivity().finish()
        }

    }


    fun editDevice() {
        if (bundleDevice != null) {
            var device: Devices = bundleDevice as Devices
            if (device != null) {
                binding.edtMarca.setText(device.marca)
                binding.edtModelo.setText(device.modelo)
                binding.edtSerie.setText(device.serie)
                binding.edtDescricao.setText(device.decricao)
                binding.edtPrice.setText(device.priceDeAquisicao)
                binding.edtNomeProduto.setText(device.nomeProduto)
                binding.edtDataAquisicao.setText(device.dataDeAquisicao)
                binding.edtGarantiaData.setText(device.dataDeGarantia)
                binding.edtQrNumber.setText(device.qrCode)
            }
        }
    }
}