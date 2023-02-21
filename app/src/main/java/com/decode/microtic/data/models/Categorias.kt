package com.decode.microtic.data.models

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.decode.microtic.data.repositories.DeviceRepositories
import com.decode.microtic.utils.Contants
import com.google.firebase.database.Exclude
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categorias (
    var id : String? = "",
    var categoria : String? = "",
    var descricao : String? = "",
        ): Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "categoria" to categoria,
            "descricao" to descricao,
        )
}


data class CategoriaUseCase(
    var categoria: Categorias,
//    var devices: MutableLiveData<MutableList<Devices>> =  MutableLiveData<MutableList<Devices>>()
) {
    private lateinit var allDevices: MutableList<Devices>

    suspend fun getDeviceList(): MutableList<Devices>{
        var allDevices = Contants.allDevices
        var mydevice = mutableListOf<Devices>()
        Log.d("categoriaproduto", "Filtrando categoria eletrodomesticos")
        allDevices.forEach {
            if (it.tipo.equals(categoria.categoria, true)) {
                Log.d("categoriaproduto", "Dispositivo encontrado\nCategoria : ${categoria.categoria}\nDispositivo ${it.marca} ,\nModelo: ${it.modelo}")
                mydevice.add(it)
            }else{
                Log.d("categoriaproduto", "fora ${it.marca}")
            }
        }
        return  mydevice;
    }
}}