package com.example.vetuserapp.view.doctors.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vetuserapp.R
import com.example.vetuserapp.controller.doctors.DoctorsViewModel
import com.example.vetuserapp.databinding.FragmentDoctorDetailBinding
import com.example.vetuserapp.model.data.Doctor
import java.text.NumberFormat

class DoctorDetailFragment : Fragment() {

    private lateinit var binding : FragmentDoctorDetailBinding
    private lateinit var doctorsViewModel: DoctorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        binding = FragmentDoctorDetailBinding.inflate(layoutInflater,container, false)
        doctorsViewModel = ViewModelProvider(requireActivity())[DoctorsViewModel::class.java].also {
            it.loading.observe(requireActivity()) {
                if (!it)
                    binding.progressBar4.visibility = View.GONE
                else
                    binding.progressBar4.visibility = View.VISIBLE
            }
        }
        val selection = doctorsViewModel.selectedDoctor
        val doctor = selection
        if(doctor!=null) setupView(doctor)

        return binding.root
    }

    private fun setupView(doctor: Doctor) {

        val queue = doctor.queue?.size?:0
        binding.tvDetailName.text = doctor.name
        binding.tvNameP.text = doctor.name
        binding.tvDescP.text = doctor.description
        binding.tvExpertiseP.text = doctor.experience
        binding.tvExpertiseD.text = doctor.specialist
        binding.tvEmailP.text = doctor.email
        binding.tvFeeD.text = doctor.fee?.toInt()?.formatThousand()?:0.0.toInt().formatThousand()
        binding.tvFeeCuz.text = queue.toString()
        binding.btnBookAppointment.setOnClickListener {
            findNavController().navigate(R.id.doctorAppointmentFragment)
        }
    }

    fun Int.formatThousand(): String {
        val nf = NumberFormat.getInstance()
        nf.maximumFractionDigits = 3
        nf.minimumFractionDigits = 0
        nf.isGroupingUsed = true
        return nf.format(this)
    }


}