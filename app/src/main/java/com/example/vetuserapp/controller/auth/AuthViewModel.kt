package com.example.vetuserapp.controller.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.repositories.AuthRepository

class AuthViewModel() : ViewModel(){

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _loggedInUser = MutableLiveData<String?>()
    val loggedInUser: MutableLiveData<String?> = _loggedInUser

    init{
        autoLogin()
    }

    private fun autoLogin() {
        _loggedInUser.value = AuthRepository.getUser()
    }

    fun login(email:String, pass:String){
        AuthRepository.login(
            email,pass,
            onSuccess = {
                autoLogin()
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun register(email:String, pass:String, userData: User){
        AuthRepository.register(userData,email,pass){_, exception ->
            if(exception==null)
                login(email,pass)
            else
                _error.value = exception
        }
    }
}