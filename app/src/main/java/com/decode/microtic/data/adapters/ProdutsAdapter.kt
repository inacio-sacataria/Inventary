package com.decode.microtic.data.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decode.microtic.R
import com.decode.microtic.data.models.Devices
import com.decode.microtic.deviceClicked
import com.decode.microtic.ui.activities.InfoProductActivity


class ProdutsAdapter(var devices: MutableList<Devices>, var context: Context): RecyclerView.Adapter<ProdutsAdapter.MyViewHolder>() {

    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){
         var marca : TextView
         var model : TextView
         var descricao : TextView
         var codigo : TextView
         var condicao : TextView
         var nome : TextView
         var foto : ImageView
         var lyt : ConstraintLayout

        init {
            marca = item.findViewById(R.id.txtMarca)
            nome = item.findViewById(R.id.txtNome)
            model = item.findViewById(R.id.txtModel)
            descricao = item.findViewById(R.id.txtDescricao)
            condicao = item.findViewById(R.id.txtEstado)
            codigo = item.findViewById(R.id.txtQrCode)
            foto = item.findViewById(R.id.imgProdutList)
            lyt = item.findViewById(R.id.layoutProdut)
        }
        fun fillData(devices: Devices){
            marca.text = devices.marca
            model.text = devices.modelo
            descricao.text = devices.decricao
            codigo.text = devices.qrCode
            condicao.text = devices.condicoes
            nome.text = devices.nomeProduto

            if(devices.photoUrls.size>0)
            Glide
                .with(context)
                .load(devices.photoUrls.get(0))
                .centerCrop()
                .placeholder(R.drawable.undraw_unboxing_pbmf)
                .into(foto)

            lyt.setOnClickListener {
                deviceClicked = devices
                context.startActivity(Intent(context,InfoProductActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.produtitem,parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fillData(devices.get(position))
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}