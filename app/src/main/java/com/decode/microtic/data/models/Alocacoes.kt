package com.decode.microtic.data.models

import android.os.Parcelable
import android.util.Log
import com.decode.microtic.utils.Contants
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Alocacoes (
    var id: String? = "",
    var reponsavel: String? = "",
    var periodoDeAlocacoes: String? ="",
    var dataDeAlocacao : String? = "",
    var fimDeAlocao : String? = "",
    var devices: Devices? = null,
        ): Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "reponsavel" to reponsavel,
            "periodoDeAlocacoes" to periodoDeAlocacoes,
            "dataDeAlocacao" to dataDeAlocacao,
            "fimDeAlocao" to fimDeAlocao,
            "devices" to devices
        )
    }
}

data class AlocacoesUseCase(
    var responsavel:  String,
) {
    private lateinit var allAlocacoes : MutableList<Alocacoes>

    suspend fun getDeviceList(): MutableList<Alocacoes>{
        allAlocacoes = Contants.allAlocacoes
        var aloc= mutableListOf<Alocacoes>()
        Log.d("categoriaproduto", "Filtrando categoria eletrodomesticos")
        allAlocacoes.forEach {
            if (it.reponsavel.equals(responsavel, true)) {
                Log.d("categoriaproduto", "Dispositivo encontrado\nCategoria : ${responsavel}\nDispositivo ${it.devices!!.marca} ,\nModelo: ${it.devices!!.modelo}")
                aloc.add(it)
            }else{
                Log.d("categoriaproduto", "fora ${it.reponsavel}")
            }
        }
        return  aloc;
    }
}