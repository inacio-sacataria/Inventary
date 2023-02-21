package com.decode.microtic.data.repositories

import android.util.Log
import com.decode.microtic.data.FirebaseInstance
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.models.Devices
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CategoriaRepositorio() {
    var db = FirebaseInstance.getInstance()!!.reference
    var myRef = db.child("categorias")

    suspend fun insertData(categ: Categorias){
        var categorias = categ.apply {
            this.id = UUID.randomUUID().toString()
        }

        myRef.child(categorias.id!!).setValue(categorias);
    }

    suspend fun update(categorias: Categorias){
        myRef.child(categorias.id!!).updateChildren(categorias.toMap())
    }

    suspend fun getAllData(): MutableList<Categorias>{
        var mutableList = mutableListOf<Categorias>()
        myRef.get().addOnSuccessListener {
            for (snap in it.children){
                var categorias = snap.getValue(Categorias::class.java)
                Log.d("categorias","${categorias!!.descricao}")
                mutableList.add(categorias!!)
            }
        }.addOnFailureListener {
            Log.d("categorias","Error on get categorias ${it.message}")
        }.await()
        return mutableList
    }

    suspend fun getAllCategoriaString(): MutableList<String>{
        var mutableList = mutableListOf<String>()
        myRef.get().addOnSuccessListener {
            for (snap in it.children){
                var categorias = snap.getValue(Categorias::class.java)
                Log.d("categorias","${categorias!!.descricao}")
                mutableList.add(categorias.categoria!!)
            }
        }.addOnFailureListener {
            Log.d("categorias","Error on get categorias ${it.message}")
        }.await()
        return mutableList
    }
}