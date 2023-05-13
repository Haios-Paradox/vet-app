package com.example.vetuserapp.view.doctors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.data.Queue
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

class DoctorsViewModel(private val repo:MainRepository) {

    private val _queueList = MutableLiveData<List<Queue>>()
    val queueList: LiveData<List<Queue>> = _queueList

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
        repo.getAllDoctorAppointment(
            id,
            onSuccess = {
                val appointments = it.sortedBy { it.getTimestamp("timestamp") }
                    .mapIndexed { index, document ->
                        Queue(document.id, index + 1) // Assign queue number based on index
                    }
                _queueList.value = appointments
            },
            onFailure = {
                _error.value = it
            }
        )
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