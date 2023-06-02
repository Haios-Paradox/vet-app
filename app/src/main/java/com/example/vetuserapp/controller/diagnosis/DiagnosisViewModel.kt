package com.example.vetuserapp.controller.diagnosis

import androidx.lifecycle.*
import com.example.vetuserapp.model.data.*
import com.example.vetuserapp.model.repositories.AppointmentRepository
import com.example.vetuserapp.model.repositories.ChatRepository
import com.example.vetuserapp.model.repositories.DoctorRepository
import com.example.vetuserapp.model.repositories.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class DiagnosisViewModel(private val appointmentId: String): ViewModel(){
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    private val _chatData = MutableLiveData<List<Chat>>()
    val chatData: LiveData<List<Chat>> = _chatData

    private val _doctorData = MutableLiveData<Doctor>()
    val doctorData:LiveData<Doctor> = _doctorData

    private val _prescription = MutableLiveData<Prescription>()
    val prescription: LiveData<Prescription> = _prescription

    private val _appointment = MutableLiveData<Appointment>()
    val appointment: LiveData<Appointment> = _appointment

    private val _queue = MutableLiveData<Int>()
    val queue: LiveData<Int> = _queue

    init{
        loadChats(appointmentId)
    }

    fun getUser(){
        viewModelScope.launch {
            try{
                _user.value = UserRepository.getUserData().getOrThrow()!!.toObject()
            }catch (e:Exception){
                _error.value = e
            }
        }
    }

    fun getDoctor(id:String){
        viewModelScope.launch {
            try{
                DoctorRepository.getDoctor(id).getOrThrow()
            }catch (e:Exception){
                _error.value = e
            }
        }
    }

    fun loadAppointment(id:String){
        viewModelScope.launch {
            try{
                val appointment = AppointmentRepository.getAppointment(appointmentId).getOrThrow()
                val numQueue = AppointmentRepository.getUserQueue(appointmentId,id).getOrThrow()
                _appointment.value = appointment.toObject()
                _queue.value = numQueue
            }catch (e:Exception){
                _error.value = e
            }
        }

    }

    fun loadChats(appointmentId: String) {
        ChatRepository.getMessages(appointmentId) {
            _chatData.value = it
        }
    }

    fun sendChat(message: Chat){
        viewModelScope.launch {
            try {
                ChatRepository.sendMessage(appointmentId,message)
            }catch (e: FirebaseFirestoreException) {
                _error.value = e
            }
        }
    }

    fun getQueue(appointmentId: String, doctorId:String){
        viewModelScope.launch {
            try{
                AppointmentRepository.getUserQueue(appointmentId, doctorId)
            }catch (e:Exception){
                _error.value = e
            }

        }

    }

}

class ViewModelFactory(private val appointmentId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiagnosisViewModel::class.java)) {
            return DiagnosisViewModel(appointmentId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
