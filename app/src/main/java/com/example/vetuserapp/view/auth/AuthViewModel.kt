package com.example.vetuserapp.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.model.data.User

class AuthViewModel(private val repo: MainRepository) {

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun login(email:String, pass:String){
        repo.login(
            email,pass,
            onSuccess = {
                _result.value = it.user!=null
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun register(email:String, pass:String, userData: User){
        repo.register(
            email,pass,userData,
            onSuccess = {
                _result.value = true
            },
            onFailure = {
                _error.value = it
            }
        )
    }
}