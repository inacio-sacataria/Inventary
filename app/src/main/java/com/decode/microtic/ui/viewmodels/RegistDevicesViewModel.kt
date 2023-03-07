package com.decode.microtic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.microtic.data.models.Devices
import com.decode.microtic.data.repositories.CategoriaRepositorio
import com.decode.microtic.data.repositories.DeviceRepositories
import com.decode.microtic.deviceClicked
import com.decode.microtic.deviceLive
import kotlinx.coroutines.launch
import java.util.*



class RegistDevicesViewModel : ViewModel() {
    var repositories = DeviceRepositories()
    var repoCtaegoria = CategoriaRepositorio()

    var liveData = MutableLiveData<MutableList<String>>()

    init {
        viewModelScope.launch {
            liveData.value = repoCtaegoria.getAllCategoriaString()
        }
    }

     fun insertData(
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
        viewModelScope.launch {
            var device = Devices(
                id= UUID.randomUUID().toString(),
                qrCode = Qr,
                marca = marca,
                modelo = modelo,
                priceDeAquisicao = price,
                decricao = descricao,
                tipo = tipo,
                dataDeAquisicao = dataDeAquisicao,
                dataDeGarantia = dataGarantia,
                serie = serie,
                condicoes = condicao,
                nomeProduto = nomeProduto
            )

            deviceLive.value = device

            repositories.insertData(device)
        }
    }

    fun update(
       device: Devices
    ) {
        viewModelScope.launch {
            deviceClicked = device
            Log.d("deviceUpdate", device.marca.toString())
            repositories.update(device)
        }
    }

    fun addPhotoUrl(listaDeFotos: MutableList<String>){
        viewModelScope.launch {
            deviceLive.value?.apply {
                this.photoUrls = listaDeFotos
                repositories.update(this)
            }
        }
    }

    fun updateAlocation(device: Devices) {
        viewModelScope.launch {
            deviceClicked = device
            Log.d("deviceUpdate", device.marca.toString())
            repositories.updateResposanvel(device)
        }
    }

    fun updatePhotoUrl(device: Devices) {
        viewModelScope.launch {
                repositories.updatePhotoUrl(device)
            }
        }
    }
