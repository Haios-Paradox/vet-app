package com.example.vetuserapp.view.doctors.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.databinding.ActivityDoctorsBinding
import com.example.vetuserapp.model.util.ViewModelFactory
import com.example.vetuserapp.view.doctors.DoctorsViewModel

class DoctorsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDoctorsBinding
    private lateinit var doctorsViewModel: DoctorsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doctorsViewModel = ViewModelProvider(this,ViewModelFactory.getInstance(this)).get(DoctorsViewModel::class.java)
    }
}