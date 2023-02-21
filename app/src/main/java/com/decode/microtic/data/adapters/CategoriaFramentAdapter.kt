package com.decode.microtic.data.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.decode.microtic.R
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.models.Devices
import com.decode.microtic.deviceClicked
import com.decode.microtic.showInfo
import com.decode.microtic.ui.activities.CategoriaActivity
import com.decode.microtic.ui.activities.InfoProductActivity

class CategoriaFramentAdapter (var categorias: MutableList<Categorias>, var context: Activity) : Adapter<CategoriaFramentAdapter.MyViewHolder> (){

    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){
        var categ : TextView
        var descricao : TextView
        var lyt : LinearLayout


        init {
            categ = item.findViewById(R.id.txtCategoriaFragment)
            descricao = item.findViewById(R.id.txtDescricaoCategoriaFrament)
            lyt = item.findViewById(R.id.btnLayoutCategoria)
        }
        fun fillData(categorias:Categorias){
            descricao.text = categorias.descricao
            categ.text = categorias.categoria
            lyt.setOnClickListener {
                context.showInfo("${categorias.categoria}")
                var intent = Intent(context, CategoriaActivity::class.java)
                context.intent.putExtra("categorias", categorias)
                context.findNavController(R.id.my_nav_host_fragment_category).navigate(R.id.categoriaProdutoFragment)
            }
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.categoria_item,parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fillData(categorias.get(position))
    }

    override fun getItemCount(): Int {
        return categorias.size
    }
}