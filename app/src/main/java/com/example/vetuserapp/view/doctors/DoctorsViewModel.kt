package com.example.vetuserapp.view.doctors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.model.data.Appointment
import com.google.firebase.firestore.DocumentSnapshot

class DoctorsViewModel(private val repo:MainRepository) : ViewModel(){

    private val _appointment = MutableLiveData<DocumentSnapshot>()
    val appointment: LiveData<DocumentSnapshot> = _appointment

    private val _doctorList = MutableLiveData<List<DocumentSnapshot>>()
    val doctorList: LiveData<List<DocumentSnapshot>> = _doctorList

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getDoctors(specialist:String?){
        repo.getSpecialistDoctorList(
            specialist,
            onSuccess = {
                _doctorList.value = it.documents
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun checkQueue(id:String){
        //TODO: Implement Queue Check
    }

    fun createAppointment(appointment: Appointment){
        repo.makeAppointment(
            appointment,
            onSuccess = {
                _appointment.value = it
            },
            onFailure = {
                _error.value = it
            }
        )
    }
}