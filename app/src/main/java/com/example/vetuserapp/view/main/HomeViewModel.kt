package com.example.vetuserapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.model.data.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class HomeViewModel(private val repo: MainRepository) : ViewModel() {

    private val _appointmentList = MutableLiveData<List<DocumentSnapshot>>()
    val appointmentList: LiveData<List<DocumentSnapshot>> = _appointmentList

    private val _specialistList = MutableLiveData<QuerySnapshot>()
    val specialistList: LiveData<QuerySnapshot> = _specialistList

    private val _userData = MutableLiveData<DocumentSnapshot>()
    val userData: LiveData<DocumentSnapshot> = _userData

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    init{
        getUserAppointment()
        getUserData()
    }

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

    fun getSpecialist(){
        repo.getSpecialistDoctorList(
            onSuccess = {
                _specialistList.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }

}