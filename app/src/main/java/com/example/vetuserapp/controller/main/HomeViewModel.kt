package com.example.vetuserapp.controller.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.data.Specialist
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.repositories.AppointmentRepository
import com.example.vetuserapp.model.repositories.DoctorRepository
import com.example.vetuserapp.model.repositories.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _appointmentList = MutableLiveData<List<Appointment>>()
    val appointmentList: LiveData<List<Appointment>> = _appointmentList

    private val _specialistList = MutableLiveData<List<Specialist>>()
    val specialistList: LiveData<List<Specialist>> = _specialistList

    private val _userData = MutableLiveData<DocumentSnapshot>()
    val userData: LiveData<DocumentSnapshot> = _userData

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    init{
        getUserAppointment()
        getUserData()
    }

    fun getUserAppointment(){
        AppointmentRepository.getAllUserAppointmentsHistory(
            onSuccess = {query ->
                _appointmentList.value = query.map { it.toObject() }
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun getUserData(){
        UserRepository.getUserData(
            onSuccess = {
                _userData.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun updateUserData(userData: User){
        viewModelScope.launch {
            try{
                UserRepository.createOrUpdateUserData(userData)
            }catch(e:Exception) {
                _error.value = e
            }
        }
    }

    fun getSpecialist(){
        DoctorRepository.getSpecialist(
            onSuccess = {query ->
                _specialistList.value = query.map{it.toObject()}
            },
            onFailure = {
                _error.value = it
            }
        )
    }

}