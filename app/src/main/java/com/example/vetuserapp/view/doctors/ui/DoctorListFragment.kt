package com.example.vetuserapp.view.doctors.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetuserapp.databinding.FragmentDoctorListBinding
import com.example.vetuserapp.model.data.Doctor
import com.example.vetuserapp.model.util.DoctorAdapter
import com.example.vetuserapp.view.doctors.DoctorsViewModel
import com.google.firebase.firestore.ktx.toObject

class DoctorListFragment : Fragment() {

    private lateinit var binding : FragmentDoctorListBinding
    private lateinit var doctorsViewModel: DoctorsViewModel
    private lateinit var adapter : DoctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDoctorListBinding.inflate(layoutInflater,container, false)
        binding.rvDoctorList.layoutManager = LinearLayoutManager(requireActivity())
        doctorsViewModel = ViewModelProvider(requireActivity()).get(DoctorsViewModel::class.java)
        doctorsViewModel.doctorList.observe(requireActivity()){data ->
            val doctors = data.map{it.toObject<Doctor>()}
            //put adapters here~
        }
        return binding.root
    }

}