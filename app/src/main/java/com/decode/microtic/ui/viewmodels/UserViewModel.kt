package com.decode.microtic.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.microtic.data.models.Users
import com.decode.microtic.data.repositories.UserRepositories
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    var repositories = UserRepositories();
    var user = MutableLiveData<Users>();

    fun create(nome: String, email: String, password: String) {
        viewModelScope.launch {
            repositories.createUser(nome = nome, email = email, password = password, role = "user")
        }
    }

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            viewModelScope.launch {
                user.value = repositories.getCurrenUser()
            }
        }
    }

    fun login(email: String, password: String) {
        repositories.login(email,password)
    }
}
