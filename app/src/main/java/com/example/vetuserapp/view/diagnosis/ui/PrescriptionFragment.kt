package com.example.vetuserapp.view.diagnosis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetuserapp.databinding.FragmentPrescriptionBinding

class PrescriptionFragment : Fragment() {
    private lateinit var binding : FragmentPrescriptionBinding
    private lateinit var diagnosisViewModel: DiagnosisViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPrescriptionBinding.inflate(inflater,container,false)
        diagnosisViewModel = ViewModelProvider(requireActivity())[DiagnosisViewModel::class.java]

        diagnosisViewModel.prescription.observe(requireActivity()){
            binding.tvAnalysis.text = it.analysis
            binding.tvDoctorNamePrescription.text = it.name
            binding.tvTreatment.text = it.treatment
        }

        return binding.root
    }

}