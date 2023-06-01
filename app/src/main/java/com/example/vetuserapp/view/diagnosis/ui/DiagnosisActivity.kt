package com.example.vetuserapp.view.diagnosis.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetuserapp.controller.diagnosis.ViewModelFactory
import com.example.vetuserapp.databinding.ActivityDiagnosisBinding

class DiagnosisActivity : AppCompatActivity() {
    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var binding : ActivityDiagnosisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appointmentId = intent.getStringExtra(APPOINTMENT)
        if(appointmentId==null)finish()
        else{
            binding = ActivityDiagnosisBinding.inflate(layoutInflater)
            diagnosisViewModel = ViewModelProvider(this,ViewModelFactory(appointmentId))[DiagnosisViewModel::class.java].also { vm->
                vm.appointment.observe(this){
                    vm.getDoctor(it.doctorId!!)
                }
            }
        }
        setContentView(binding.root)
    }

    companion object{
        const val APPOINTMENT = "appointment"
    }
}