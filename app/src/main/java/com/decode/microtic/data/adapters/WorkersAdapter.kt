package com.decode.microtic.data.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decode.microtic.R
import com.decode.microtic.data.models.Alocacoes
import com.decode.microtic.databinding.AlocationItemBinding
import com.decode.microtic.databinding.WorkersitemBinding
import com.decode.microtic.ui.activities.AlocacoesActivity
import com.decode.microtic.utils.Contants

class WorkersAdapter ( var context: Activity) : androidx.recyclerview.widget.RecyclerView.Adapter<WorkersAdapter.MyViewHolder>(){

    var workers = Contants.workers

    inner class MyViewHolder(val binding: WorkersitemBinding): RecyclerView.ViewHolder(binding.root){

        fun fillData(worker: String){
            binding.txtWorkerName.setText("${worker}")
            binding.btnLayoutWorker.setOnClickListener {
                var intent = Intent(context,AlocacoesActivity::class.java )
                intent.putExtra("worker",worker)
                context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        var binding = WorkersitemBinding.inflate(layoutInflater ,parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fillData(workers.get(position))
    }

    override fun getItemCount(): Int {
        return workers.size
    }}