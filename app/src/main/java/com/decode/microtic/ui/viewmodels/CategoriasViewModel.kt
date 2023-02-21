package com.decode.microtic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.repositories.CategoriaRepositorio
import kotlinx.coroutines.launch

class CategoriasViewModel : ViewModel(){
    private var repositories : CategoriaRepositorio = CategoriaRepositorio()
    var _categorias = MutableLiveData<MutableList<Categorias>>()

    init {
        getCategoria()
    }

    fun getCategoria(){
        viewModelScope.launch {
            var list = repositories.getAllData()
            _categorias.postValue(list)
        }
    }

    fun addCategoria(categ: String, descricao: String){
       viewModelScope.launch {
           var categoria = Categorias(categoria = categ, descricao = descricao)
           repositories.insertData(categoria)
       }
    }
}