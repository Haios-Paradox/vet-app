package com.example.vetuserapp.view.doctors.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetuserapp.R
import com.example.vetuserapp.controller.doctors.DoctorsViewModel
import com.example.vetuserapp.databinding.FragmentDoctorListBinding
import com.example.vetuserapp.model.util.DoctorAdapter
import com.google.firebase.firestore.DocumentSnapshot

class DoctorListFragment : Fragment() {

    private lateinit var binding : FragmentDoctorListBinding
    private lateinit var doctorsViewModel: DoctorsViewModel
    private lateinit var adapter : DoctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDoctorListBinding.inflate(layoutInflater,container, false)
        binding.rvDoctorList.layoutManager = LinearLayoutManager(requireActivity())
        doctorsViewModel = ViewModelProvider(requireActivity())[DoctorsViewModel::class.java]
        doctorsViewModel.doctorList.observe(requireActivity()){data ->
            val doctors = data
            adapter = DoctorAdapter(doctors)
            adapter.setOnItemClickCallback(object : DoctorAdapter.OnItemClickCallback {
                override fun onItemClicked(data: DocumentSnapshot) {
                    doctorsViewModel.selectedDoctor = data
                    findNavController().navigate(R.id.doctorDetailFragment)
                }
            })
            binding.rvDoctorList.adapter = adapter
        }
        return binding.root
    }

}