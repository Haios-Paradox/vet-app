package com.example.vetuserapp.view.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vetuserapp.controller.main.HomeViewModel
import com.example.vetuserapp.databinding.FragmentSpecialistBinding
import com.example.vetuserapp.model.data.Specialist
import com.example.vetuserapp.model.util.SpecialistAdapter
import com.example.vetuserapp.view.doctors.ui.DoctorsActivity

class SpecialistFragment : Fragment() {
    private lateinit var binding : FragmentSpecialistBinding
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var adapter : SpecialistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSpecialistBinding.inflate(inflater,container,false)
        binding.rvSpecialist.layoutManager = GridLayoutManager(requireActivity(),2)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        homeViewModel.specialistList.observe(requireActivity()){data ->
            val specialist = data
            adapter = SpecialistAdapter(specialist)
            adapter.setOnItemClickCallback(object : SpecialistAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Specialist) {
                    startActivity(
                        Intent(requireActivity(), DoctorsActivity::class.java)
                            .putExtra(DoctorsActivity.SPECIALIST, data.name)
                    )
                }
            })
            binding.rvSpecialist.adapter = adapter
        }

        return binding.root
    }


}