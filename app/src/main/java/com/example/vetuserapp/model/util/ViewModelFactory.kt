package com.example.vetuserapp.model.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.view.auth.AuthViewModel
import com.example.vetuserapp.view.diagnosis.DiagnosisViewModel
import com.example.vetuserapp.view.doctors.DoctorsViewModel
import com.example.vetuserapp.view.main.HomeViewModel


class ViewModelFactory private constructor(private val mainRepository: MainRepository) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    MainRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(mainRepository) as T
            }
            modelClass.isAssignableFrom(DiagnosisViewModel::class.java) -> {
                DiagnosisViewModel(mainRepository) as T
            }
            modelClass.isAssignableFrom(DoctorsViewModel::class.java) -> {
                DoctorsViewModel(mainRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(mainRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}