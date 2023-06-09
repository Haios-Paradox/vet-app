package com.example.vetuserapp.view.diagnosis.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetuserapp.R
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

            diagnosisViewModel.message.observe(this){
                Toast.makeText(this,it?:"There was an error",Toast.LENGTH_SHORT).show()
            }
        }

        binding.bottomNavigationView2.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_checkup -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView2.id
                    ).navigate(R.id.checkUpFragment)
                    true
                }
                R.id.navigation_chat ->{
                    Navigation.findNavController(
                        this, binding.fragmentContainerView2.id
                    ).navigate(R.id.chatFragment)
                    true
                }
                R.id.navigation_prescription -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView2.id
                    ).navigate(R.id.prescriptionFragment)
                    true
                }
                else -> false
            }
        }
        setContentView(binding.root)
    }

    companion object{
        const val APPOINTMENT = "appointment"
    }
}