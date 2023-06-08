package com.example.vetuserapp.controller.doctors

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetuserapp.model.data.Doctor
import com.example.vetuserapp.model.repositories.AppointmentRepository
import com.example.vetuserapp.model.repositories.DoctorRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class DoctorsViewModel: ViewModel(){

    private val _appointment = MutableLiveData<List<Doctor>>()
    val appointment: LiveData<List<Doctor>> = _appointment

    private val _newAppointment = MutableLiveData<DocumentReference>()
    val newAppointment: LiveData<DocumentReference> = _newAppointment

    private val _doctorList = MutableLiveData<List<DocumentSnapshot>>()
    val doctorList: LiveData<List<DocumentSnapshot>> = _doctorList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    var selectedDoctor:Doctor?=null

    val loading = MutableLiveData<Boolean>()

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
                loading.value = true
                _doctorList.value = DoctorRepository.getDoctors().getOrThrow()?.documents
                loading.value = false
            }catch (e:Exception){
                _message.value = e.cause?.message?:e.message?:"There was an error"
                loading.value = false
            }
        }
    }


    fun getDoctors(specialty:String){
        viewModelScope.launch {
            try{
                DoctorRepository.getDoctors(
                    specialty,
                    onDoctorsChanged = {
                        loading.value = true
                        _doctorList.value = it?.documents
                        loading.value = false
                    }
                )
            }catch (e:Exception){
                loading.value = false
                _message.value = e.cause?.message?:e.message?:"There was an error"
            }
        }

    }

    fun createAppointment(petname:String, desc:String){
        viewModelScope.launch {
            try{
                loading.value = true
                val doctorData = selectedDoctor
                _newAppointment.value = AppointmentRepository.createAppointment(doctorData!!,selectedDoctor!!.id!!,imageBitmap.value!!,desc, petname).getOrThrow()
                _imageBitmap.value = null
                loading.value = false
                _message.value = "Create Appointment Success"
            }
            catch (e:Exception){
                loading.value = false
                _message.value = e.cause?.message?:e.message?:"There was an error"
            }
        }

    }
}