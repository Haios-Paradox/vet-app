package com.example.vetuserapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.model.data.User
import com.google.firebase.firestore.DocumentSnapshot

class HomeViewModel(private val repo: MainRepository) {

    private val _appointmentList = MutableLiveData<List<DocumentSnapshot>>()
    val appointmentList: LiveData<List<DocumentSnapshot>> = _appointmentList

    private val _userData = MutableLiveData<DocumentSnapshot>()
    val userData: LiveData<DocumentSnapshot> = _userData

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getUserAppointment(){
        repo.getAllUserAppointment(
            onSuccess = {
                _appointmentList.value = it.documents
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun getUserData(){
        repo.getUserData(
            onSuccess = {
                _userData.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun updateUserData(userData: User){
        repo.updateUserData(
            userData,
            onSuccess = {
                getUserData()
            },
            onFailure = {
                _error.value = it
            }
        )
    }

}