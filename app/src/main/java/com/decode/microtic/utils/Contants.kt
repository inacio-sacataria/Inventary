package com.decode.microtic.utils

import com.decode.microtic.data.models.Alocacoes
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.models.Devices

object Contants {
    var categoriaClicked: Categorias? = Categorias(categoria = "eletrodomesticos", descricao = "dd")
    var allDevices = mutableListOf<Devices>()
    var allAlocacoes = mutableListOf<Alocacoes>()
    var workers = arrayOf("Raul Matsinhe", "Gabriel Nhachengo", "Alcina Mauricio", "Jessica Cossa", "Luzepio Nhachengo","Inacio Sacataria")
}