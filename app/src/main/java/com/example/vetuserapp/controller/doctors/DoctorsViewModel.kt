package com.example.vetuserapp.controller.doctors

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetuserapp.model.data.Doctor
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.repositories.AppointmentRepository
import com.example.vetuserapp.model.repositories.DoctorRepository
import com.example.vetuserapp.model.repositories.UserRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class DoctorsViewModel: ViewModel(){

    private val _appointment = MutableLiveData<List<Doctor>>()
    val appointment: LiveData<List<Doctor>> = _appointment

    private val _newAppointment = MutableLiveData<DocumentReference>()
    val newAppointment: LiveData<DocumentReference> = _newAppointment

    private val _doctorList = MutableLiveData<List<DocumentSnapshot>>()
    val doctorList: LiveData<List<DocumentSnapshot>> = _doctorList

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    var selectedDoctor:DocumentSnapshot?=null

    fun storeImage(bitmap: Bitmap, quality: Int) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedBitmap =
            BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
        _imageBitmap.value = compressedBitmap
    }

    fun getDoctors() {
        viewModelScope.launch {
            try{
                _doctorList.value = DoctorRepository.getDoctors().getOrThrow()?.documents
            }catch (e:Exception){
                _error.value = e
            }
        }
    }


    fun getDoctors(specialty:String){
        viewModelScope.launch {
            try{
                _doctorList.value = DoctorRepository.getDoctors(specialty).getOrThrow()?.documents
            }catch (e:Exception){
                _error.value = e
            }
        }

    }

    fun createAppointment(desc:String){
        viewModelScope.launch {
            try{
                val userDoc = UserRepository.getUserData().getOrThrow()
                val userData = userDoc?.toObject<User>()
                val doctorData = selectedDoctor?.toObject<Doctor>()
                _newAppointment.value = AppointmentRepository.createAppointment(userData!!,doctorData!!,selectedDoctor!!.id,imageBitmap.value!!,desc).getOrThrow()
            }
            catch (e:Exception){
                _error.value = e
            }
        }

    }
}