package com.example.vetuserapp.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(private val repo: MainRepository) : ViewModel(){

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _loggedInUser = MutableLiveData<FirebaseUser?>()
    val loggedInUser: MutableLiveData<FirebaseUser?> = _loggedInUser

    init{
        autoLogin()
    }

    private fun autoLogin() {
        _loggedInUser.value = repo.getUser()
    }

    fun login(email:String, pass:String){
        repo.login(
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
        repo.register(
            email,pass,userData,
            onSuccess = {
                autoLogin()
            },
            onFailure = {
                _error.value = it
            }
        )
    }
}