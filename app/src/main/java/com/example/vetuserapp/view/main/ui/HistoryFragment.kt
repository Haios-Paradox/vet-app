package com.example.vetuserapp.view.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetuserapp.controller.main.HomeViewModel
import com.example.vetuserapp.databinding.FragmentHistoryBinding
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.util.AppointmentAdapter
import com.example.vetuserapp.view.diagnosis.ui.DiagnosisActivity

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
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java).also {
            it.loading.observe(requireActivity()) {
                if (!it)
                    binding.progressBar6.visibility = View.GONE
                else
                    binding.progressBar6.visibility = View.VISIBLE
            }
        }

        homeViewModel.appointmentList.observe(requireActivity()){
            val appointments = it
            adapter = AppointmentAdapter(appointments as List<Appointment>)
            adapter.setOnItemClickCallback(object : AppointmentAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Appointment) {
                    startActivity(
                        Intent(requireActivity(), DiagnosisActivity::class.java)
                            .putExtra(DiagnosisActivity.APPOINTMENT, data.id)
                    )
                }

            })
            binding.rvHistory.adapter = adapter
        }

        return binding.root
    }

}