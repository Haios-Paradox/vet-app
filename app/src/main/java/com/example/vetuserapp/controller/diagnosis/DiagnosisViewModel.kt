package com.example.vetuserapp.controller.diagnosis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.data.Chat
import com.example.vetuserapp.model.data.Doctor
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.repositories.AppointmentRepository
import com.example.vetuserapp.model.repositories.ChatRepository
import com.example.vetuserapp.model.repositories.DoctorRepository
import com.example.vetuserapp.model.repositories.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class DiagnosisViewModel(private val appointmentId: String): ViewModel(){
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    private val _chatData = MutableLiveData<List<Chat>>()
    val chatData: LiveData<List<Chat>> = _chatData

    private val _doctorData = MutableLiveData<Doctor>()
    val doctorData:LiveData<Doctor> = _doctorData

    private val _appointment = MutableLiveData<Appointment>()
    val appointment: LiveData<Appointment> = _appointment

    private val _queue = MutableLiveData<Int>()
    val queue: LiveData<Int> = _queue

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    init{
        loadChats(appointmentId)
        loadAppointment()
        getUser()
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

    //TODO: Make the queue thingy realtime.
    //TODO: 1. Make a listener for appointment document
    //TODO: 2. Use that to show the Queue
//    fun loadAppointment(){
//        viewModelScope.launch {
//            try{
//                val docAppointment = AppointmentRepository.getAppointment(appointmentId).getOrThrow()
//                val appointment = docAppointment.toObject<Appointment>()
//                val numQueue = AppointmentRepository.getUserQueue(appointmentId,appointment!!.doctorId!!).getOrThrow()
//                _appointment.value = appointment!!
//                _queue.value = numQueue
//            }catch (e:Exception){
//                _error.value = e
//            }
//        }
//    }

    fun loadAppointment(){
        viewModelScope.launch{
            try{
                val regis = AppointmentRepository.getAppointment(
                    appointmentId,
                    onUpdate = {
                        _appointment.value = it.toObject()
                        getQueue(appointmentId,appointment.value!!.doctorId!!)
                    }
                )
            }catch (e:Exception){
                _error.value = e
            }
        }
    }

    fun loadChats(appointmentId: String) {
        ChatRepository.getMessages(appointmentId) {
            val sortedChat = it.sortedBy { it.timestamp }.asReversed()
            _chatData.value = sortedChat
        }
    }

    fun sendChat(message: Chat){
        viewModelScope.launch {
            try {
                if(imageBitmap.value==null)
                    ChatRepository.sendMessage(appointmentId,message)
                else
                    ChatRepository.sendMessage(appointmentId,message, imageBitmap.value!!)
                _imageBitmap.value = null
            }catch (e: FirebaseFirestoreException) {
                _error.value = e
            }
        }
    }

    fun getQueue(appointmentId: String, doctorId:String){
        viewModelScope.launch {
            try{
                val result = AppointmentRepository.getUserQueue(appointmentId, doctorId).getOrThrow()
                _queue.value = result
            }catch (e:Exception){
                _error.value = e
            }
        }
    }

    fun storeImage(bitmap: Bitmap, quality: Int) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedBitmap =
            BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
        _imageBitmap.value = compressedBitmap
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
