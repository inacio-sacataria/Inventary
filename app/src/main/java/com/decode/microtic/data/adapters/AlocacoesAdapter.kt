package com.decode.microtic.data.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decode.microtic.R
import com.decode.microtic.data.models.Alocacoes
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.databinding.AlocationItemBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.activities.CategoriaActivity

class AlocacoesAdapter (var alocacoes : MutableList<Alocacoes>, var context: Activity) : androidx.recyclerview.widget.RecyclerView.Adapter<AlocacoesAdapter.MyViewHolder>(){

    inner class MyViewHolder(val binding: AlocationItemBinding): RecyclerView.ViewHolder(binding.root){

        fun fillData(aloc: Alocacoes){
            binding.txtPeriodoAlocacap.setText("Descrição: ${aloc.periodoDeAlocacoes}")
            binding.txtInicioAlacao.setText("Data inicio de Alocacao: ${aloc.dataDeAlocacao}")
            binding.txtFimAlocacao.setText("Data final de Alocacao : ${aloc.fimDeAlocao}")

            binding.txtBrand.text = aloc.devices!!.marca
            binding.txtModel.text= aloc.devices!!.modelo
            binding.txtSerie.text = aloc.devices!!.serie
            binding.txtDateAquisition.text = aloc.devices!!.dataDeAquisicao
            binding.txtPrice.text = aloc.devices!!.priceDeAquisicao

            Glide
                .with(context)
                .load(aloc.devices!!.photoUrls.get(0))
                .centerCrop()
                .placeholder(R.drawable.undraw_unboxing_pbmf)
                .into(binding.imgProdutList)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        var binding = AlocationItemBinding.inflate(layoutInflater ,parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fillData(alocacoes.get(position))
    }

    override fun getItemCount(): Int {
        return alocacoes.size
    }}