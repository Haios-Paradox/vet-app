package com.example.vetuserapp.controller.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.data.Specialist
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.repositories.AppointmentRepository
import com.example.vetuserapp.model.repositories.AuthRepository
import com.example.vetuserapp.model.repositories.DoctorRepository
import com.example.vetuserapp.model.repositories.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class HomeViewModel : ViewModel() {

    private val _appointmentList = MutableLiveData<List<Appointment>>()
    val appointmentList: LiveData<List<Appointment>> = _appointmentList

    private val _specialistList = MutableLiveData<List<Specialist>>()
    val specialistList: LiveData<List<Specialist>> = _specialistList

    private val _userData = MutableLiveData<DocumentSnapshot>()
    val userData: LiveData<DocumentSnapshot> = _userData

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    val loading = MutableLiveData<Boolean>()

    init{
        getUserAppointment()
        getUserData()
        getSpecialist()
    }

    fun storeImage(bitmap: Bitmap, quality: Int) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedBitmap =
            BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
        _imageBitmap.value = compressedBitmap
    }

    private fun getUserAppointment(){
        viewModelScope.launch {
            try{
                loading.value = true
                val result = AppointmentRepository.getAllUserAppointmentsHistory().getOrThrow()
                if (result != null) {
                    _appointmentList.value = result.map { it.toObject() }
                }
                loading.value = false
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }

        }

    }
    private fun getUserData(){
        viewModelScope.launch {
            try{
                loading.value = true
                _userData.value = UserRepository.getUserData().getOrThrow()
                loading.value = false
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }
    }

    fun updateUserData(userData: User){
        viewModelScope.launch {
            try{
                loading.value = true
                UserRepository.createOrUpdateUserData(userData)
                getUserData()
                loading.value = false
                _message.value = "Update Successful"
            }catch(e:Exception) {
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }
    }

    fun updateUserData(user:User, file:Bitmap){
        viewModelScope.launch {
            try{
                loading.value = true
                UserRepository.createOrUpdateUserData(user,file)
                getUserData()
                loading.value = false
                _message.value = "Update Successful"
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }

    }

    private fun getSpecialist(){
        viewModelScope.launch {
            try{
                loading.value = true
                val specialist = DoctorRepository.getSpecialist().getOrThrow()
                _specialistList.value = specialist?.map{it.toObject()}
                loading.value = false
            }catch(e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }

    }

    fun logout() {
        AuthRepository.logout()
    }

}