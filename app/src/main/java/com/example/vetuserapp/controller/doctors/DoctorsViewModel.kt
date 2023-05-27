package com.example.vetuserapp.controller.doctors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.data.Doctor
import com.example.vetuserapp.model.repositories.AppointmentRepository
import com.example.vetuserapp.model.repositories.DoctorRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

class DoctorsViewModel: ViewModel(){

    private val _appointment = MutableLiveData<List<Doctor>>()
    val appointment: LiveData<List<Doctor>> = _appointment

    private val _doctorList = MutableLiveData<List<DocumentSnapshot>>()
    val doctorList: LiveData<List<DocumentSnapshot>> = _doctorList

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getDoctors(){
        DoctorRepository.getDoctors(
            onSuccess = {query ->
                _doctorList.value = query.map { it.toObject() }
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun getDoctors(specialty:String){
        DoctorRepository.getDoctors(
            specialty,
            onSuccess = {query ->
                _doctorList.value = query.map { it.toObject() }
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun createAppointment(appointment: Appointment){
        AppointmentRepository.createAppointment(
            appointment,
            onSuccess = {

            },
            onFailure = {
                _error.value = it
            }
        )
    }
}