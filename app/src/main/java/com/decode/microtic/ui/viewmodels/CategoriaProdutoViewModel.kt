package com.decode.microtic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.microtic.data.models.Categorias
import com.decode.microtic.data.models.Devices
import com.decode.microtic.data.repositories.CategoriaRepositorio
import com.decode.microtic.utils.Contants
import kotlinx.coroutines.launch

class CategoriaProdutoViewModel: ViewModel() {
    var categoriaList = MutableLiveData<MutableList<Devices>>()
    lateinit var category : Categorias.CategoriaUseCase

    init {
    }


    fun getDevicesByCategory(categorias: Categorias){
        viewModelScope.launch {
            category = Categorias.CategoriaUseCase(categorias)
            categoriaList.value = category.getDeviceList()
        }


    }
}
