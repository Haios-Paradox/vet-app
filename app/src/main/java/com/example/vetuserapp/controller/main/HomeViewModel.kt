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
        getSpecialist()
    }

    fun getUserAppointment(){
        viewModelScope.launch {
            try{
                val result = AppointmentRepository.getAllUserAppointmentsHistory().getOrThrow()
                if (result != null) {
                    _appointmentList.value = result.map { it.toObject() }
                }
            }catch (e:Exception){
                _error.value = e
            }

        }

    }
    fun getUserData(){
        viewModelScope.launch {
            try{
                _userData.value = UserRepository.getUserData().getOrThrow()
            }catch (e:Exception){
                _error.value = e
            }
        }
    }

    fun updateUserData(userData: User){
        viewModelScope.launch {
            try{
                UserRepository.createOrUpdateUserData(userData).getOrThrow()
            }catch(e:Exception) {
                _error.value = e
            }
        }
    }

    fun getSpecialist(){
        viewModelScope.launch {
            try{
                val specialist = DoctorRepository.getSpecialist().getOrThrow()
                _specialistList.value = specialist?.map{it.toObject()}
            }catch(e:Exception){
                _error.value = e
            }
        }

    }

}