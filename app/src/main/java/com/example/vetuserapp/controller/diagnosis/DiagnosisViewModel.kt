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
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

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

    val loading = MutableLiveData<Boolean>()

    init{
        loadChats(appointmentId)
        loadAppointment()
        getUser()
    }

    private fun getUser(){
        viewModelScope.launch {
            try{
                loading.value = true
                _user.value = UserRepository.getUserData().getOrThrow()!!.toObject()
                loading.value = false
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }
    }

    fun getDoctor(id:String){
        viewModelScope.launch {
            try{
                loading.value = true
                DoctorRepository.getDoctor(id).getOrThrow()
                loading.value = false
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }
    }

    private fun loadAppointment(){
        viewModelScope.launch{
            try{
                loading.value = true
                val regis = AppointmentRepository.getAppointment(
                    appointmentId,
                    onUpdate = {
                        loading.value = true
                        _appointment.value = it.toObject()
                        getQueue(appointmentId,appointment.value!!.doctorId!!)
                        loading.value = false
                    }
                )
            }catch (e:Exception){
                loading.value = false
                _message.value = e.cause?.message?:e.message?:"There was an error"
            }
        }
    }

    private fun loadChats(appointmentId: String) {
        ChatRepository.getMessages(appointmentId) {
            loading.value = true
            val sortedChat = it.sortedBy { it.timestamp }.asReversed()
            _chatData.value = sortedChat
            loading.value = false
        }
    }

    fun sendChat(message: Chat){
        viewModelScope.launch {
            try {
                loading.value = true
                if(imageBitmap.value==null)
                    ChatRepository.sendMessage(appointmentId,message)
                else
                    ChatRepository.sendMessage(appointmentId,message, imageBitmap.value!!)
                _imageBitmap.value = null
                loading.value = false
            }catch (e: FirebaseFirestoreException) {
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value
            }
        }
    }

    fun getQueue(appointmentId: String, doctorId:String){
        viewModelScope.launch {
            try{
                loading.value = true
                val result = AppointmentRepository.getUserQueue(appointmentId, doctorId).getOrThrow()
                _queue.value = result
                loading.value = false
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
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
