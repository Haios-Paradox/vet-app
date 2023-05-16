package com.example.vetuserapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetuserapp.databinding.FragmentHistoryBinding
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.util.AppointmentAdapter
import com.example.vetuserapp.view.main.HomeViewModel
import com.google.firebase.firestore.ktx.toObject

class HistoryFragment : Fragment() {
    private lateinit var binding : FragmentHistoryBinding
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var adapter : AppointmentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        binding.rvHistory.layoutManager = LinearLayoutManager(requireActivity())
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        homeViewModel.appointmentList.observe(requireActivity()){
            val appointments = it.map { it.toObject<Appointment>()}
            adapter = AppointmentAdapter(appointments as List<Appointment>)
            binding.rvHistory.adapter = adapter
        }

        return binding.root
    }

}