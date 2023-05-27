package com.example.vetuserapp.view.doctors.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.doctors.DoctorsViewModel
import com.example.vetuserapp.databinding.ActivityDoctorsBinding

class DoctorsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDoctorsBinding
    private lateinit var doctorsViewModel: DoctorsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doctorsViewModel = ViewModelProvider(this).get(
            DoctorsViewModel::class.java)
    }
}