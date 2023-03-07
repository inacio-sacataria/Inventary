package com.decode.microtic.data.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.decode.microtic.R

class PhotosItemAdapter(var mutableList: MutableList<String>,var context: Context) : Adapter<PhotosItemAdapter.MyViewHolder>()  {

    inner class MyViewHolder(item : View): ViewHolder(item){
        var img = item.findViewById<ImageView>(R.id.fotoo)

        fun fillData(url:String){
            Glide.with(context).load(url).placeholder(R.drawable.undraw_unboxing_pbmf).into(img)
            Log.d("PhotoPickerUrl", "${url}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.photoitem,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.fillData(mutableList[position])
    }

    override fun getItemCount(): Int {
       return mutableList.size
    }
}