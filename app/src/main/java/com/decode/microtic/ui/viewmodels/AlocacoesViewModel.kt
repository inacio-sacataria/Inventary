package com.decode.microtic.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.microtic.data.models.Alocacoes
import com.decode.microtic.data.models.AlocacoesUseCase
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.models.Devices
import com.decode.microtic.data.repositories.AlocacoesRepositorios
import com.decode.microtic.data.repositories.DeviceRepositories
import com.decode.microtic.utils.Contants
import kotlinx.coroutines.launch

class AlocacoesViewModel : ViewModel() {
    var alocacoesList = MutableLiveData<MutableList<Alocacoes>>()
    lateinit var alocacoes: AlocacoesUseCase
    var repositorio = AlocacoesRepositorios()
    var devicesRepo = DeviceRepositories()



    fun getDevicesByAlocation(aloc:  String){
        viewModelScope.launch {
            alocacoes = AlocacoesUseCase(aloc!!)
            alocacoesList.value = alocacoes.getDeviceList()
        }
    }
    init {
        geAlocations()
    }

    fun geAlocations(){
        viewModelScope.launch {
            Contants.allAlocacoes = repositorio.getAllData()
        }
    }

    fun createAlocacoes(responsavel: String, periodo: String, dataAlocacao: String , dataFinal: String , devices: Devices){
        viewModelScope.launch {
            var alocacoes = Alocacoes(reponsavel =responsavel, periodoDeAlocacoes = periodo, dataDeAlocacao = dataAlocacao, fimDeAlocao = dataFinal, devices = devices)
            devices.responsavel= responsavel
            devices.alocado= true;
            repositorio.insertData(alocacoes)
            devicesRepo.update(devices)
        }
    }
}