package com.example.vetuserapp.view.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vetuserapp.R
import com.example.vetuserapp.databinding.FragmentProfileBinding
import com.example.vetuserapp.databinding.FragmentSpecialistBinding
import com.example.vetuserapp.model.data.Specialist
import com.example.vetuserapp.model.util.SpecialistAdapter
import com.example.vetuserapp.view.main.HomeViewModel
import com.google.firebase.firestore.ktx.toObject

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
            val specialist = data.map{it.toObject<Specialist>()}
            adapter = SpecialistAdapter(specialist)
            binding.rvSpecialist.adapter = adapter
        }

        return binding.root
    }


}