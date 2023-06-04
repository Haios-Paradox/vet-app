package com.example.vetuserapp.view.doctors.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.doctors.DoctorsViewModel
import com.example.vetuserapp.databinding.ActivityDoctorsBinding

class DoctorsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDoctorsBinding
    private lateinit var doctorsViewModel: DoctorsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val specialist = intent.getStringExtra(SPECIALIST)
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doctorsViewModel = ViewModelProvider(this).get(
            DoctorsViewModel::class.java)
        if(specialist!=null && specialist!="")
            doctorsViewModel.getDoctors(specialist)
        else
            doctorsViewModel.getDoctors()

        doctorsViewModel.error.observe(this){
            Toast.makeText(this,it.cause?.message?:"There was an error",Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        const val SPECIALIST = "specialist"
    }
}