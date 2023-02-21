package com.decode.microtic.data.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.zxing.qrcode.encoder.QRCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class Devices(
    var id : String? = "",
    var nomeProduto : String?="",
    var qrCode: String?= "",
    var marca : String? = "",
    var modelo : String? = "",
    var priceDeAquisicao : String?= "",

    var decricao : String? = "",
    var serie: String? = "",
    var dataDeAquisicao : String? ="",
    var dataDeGarantia : String? ="",

    var tipo : String? = "",
    var photoUrls : List<String> = listOf(),

    var alocado : Boolean? = false,
    var responsavel : String?= "",
    var condicoes : String?= "Operacional"

): Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "qrCode" to qrCode,
            "nomeProduto" to nomeProduto,
            "marca" to marca,
            "priceDeAquisicao" to priceDeAquisicao,
            "decricao" to decricao,
            "serie" to serie,
            "dataDeAquisicao" to dataDeAquisicao,
            "dataDeGarantia" to dataDeGarantia,
            "tipo" to tipo,
            "condicoes" to condicoes,
            "photoUrls" to photoUrls,
            "alocado" to alocado,
            "responsavel" to responsavel
        )
    }
}
