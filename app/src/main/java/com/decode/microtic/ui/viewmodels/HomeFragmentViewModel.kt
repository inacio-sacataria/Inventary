package com.decode.microtic.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.microtic.data.models.Devices
import com.decode.microtic.data.repositories.AlocacoesRepositorios
import com.decode.microtic.data.repositories.DeviceRepositories
import kotlinx.coroutines.launch

class HomeFragmentViewModel: ViewModel(){
    var repositories = DeviceRepositories()
    var alocRepo= AlocacoesRepositorios()
    var _devices = MutableLiveData<MutableList<Devices>>()

    init {
        getAllData()
    }
    fun getAllData(){
        viewModelScope.launch {
             var list = repositories.getAllData()
            _devices.postValue(list)
        }
    }

    fun verifyQR(text: String): Devices? {
        var device : Devices? = null
        viewModelScope.launch{
            for (devices in _devices.value!!){
                if (devices.qrCode==text){
                    device =devices
                }
            }
        }
        return device
    }

    fun dealocate(device: Devices) {
        viewModelScope.launch{
            device.responsavel =""
            device.alocado= false
            repositories.update(device)
            alocRepo.removeAlocation(device)
        }
    }

    fun delete(device: Devices) {
        viewModelScope.launch {
            repositories.delete(device)
        }
    }
}