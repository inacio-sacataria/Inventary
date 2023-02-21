package com.decode.microtic.data.repositories

import android.util.Log
import com.decode.microtic.data.FirebaseInstance
import com.decode.microtic.data.models.Alocacoes
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.models.Devices
import kotlinx.coroutines.tasks.await
import java.util.*

class AlocacoesRepositorios {
    var db = FirebaseInstance.getInstance()!!.reference
    var myRef = db.child("alocacoes")

    suspend fun insertData(aloc: Alocacoes){
        var alocacoes = aloc.apply {
            this.id = UUID.randomUUID().toString()
        }

        myRef.child(alocacoes.id!!).setValue(alocacoes);
    }

    suspend fun update(aloc: Alocacoes){
        myRef.child(aloc.id!!).updateChildren(aloc.toMap())
    }

    suspend fun getAllData(): MutableList<Alocacoes>{
        var mutableList = mutableListOf<Alocacoes>()
        myRef.get().addOnSuccessListener {
            for (snap in it.children){
                var alocacoes = snap.getValue(Alocacoes::class.java)
                Log.d("alocacoes","${alocacoes!!.reponsavel}")
                mutableList.add(alocacoes!!)
            }
        }.addOnFailureListener {
            Log.d("alocacoes","Error on get alocacoes ${it.message}")
        }.await()
        return mutableList
    }


    suspend fun removeAlocation(deviceAloc: Devices){
        var mutableList = getAllData()
        mutableList.forEach {
            var idDevices = it.devices!!.id
            if (idDevices==deviceAloc.id){
                myRef.child(it.id!!).removeValue().addOnSuccessListener {
                    Log.d("alocacoes","alocacao apagada")
                }
            }
        }
    }
}