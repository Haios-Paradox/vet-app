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
import com.example.vetuserapp.model.data.Doctor
import com.example.vetuserapp.model.util.DoctorAdapter
import com.google.firebase.firestore.ktx.toObject

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
        doctorsViewModel = ViewModelProvider(requireActivity())[DoctorsViewModel::class.java].also {
            it.loading.observe(requireActivity()) {
                if (!it)
                    binding.progressBar5.visibility = View.GONE
                else
                    binding.progressBar5.visibility = View.VISIBLE
            }
        }
        doctorsViewModel.doctorList.observe(requireActivity()){data ->
            val doctors = data.map{it.toObject<Doctor>()}
            val filtered = doctors.filter { it?.queue?.size!! + it.finished_queue?.size!! < it.limit!! && it.available == true }
            adapter = DoctorAdapter(filtered as List<Doctor>)
            adapter.setOnItemClickCallback(object : DoctorAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Doctor) {
                    doctorsViewModel.selectedDoctor = data
                    findNavController().navigate(R.id.doctorDetailFragment)
                }
            })
            binding.rvDoctorList.adapter = adapter
        }
        return binding.root
    }

}