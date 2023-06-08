package com.example.vetuserapp.view.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vetuserapp.R
import com.example.vetuserapp.controller.main.HomeViewModel
import com.example.vetuserapp.databinding.FragmentSpecialistBinding
import com.example.vetuserapp.model.data.Specialist
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.util.SpecialistAdapter
import com.example.vetuserapp.view.doctors.ui.DoctorsActivity
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
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java).also {
            it.loading.observe(requireActivity()) {
                if (it)
                    binding.progressBar9.visibility = View.GONE
                else
                    binding.progressBar9.visibility = View.VISIBLE
            }
        }

        homeViewModel.specialistList.observe(requireActivity()){data ->
            val specialist = data
            adapter = SpecialistAdapter(specialist)
            adapter.setOnItemClickCallback(object : SpecialistAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Specialist) {
                    specialtyIntent(data)
                }
            })
            binding.rvSpecialist.adapter = adapter
        }

        return binding.root
    }

    private fun specialtyIntent(data:Specialist) {
        val userData = homeViewModel.userData.value?.toObject<User>()
        if(
            userData?.avatar == null ||
            userData.desc.isNullOrEmpty()||
            userData.phone.isNullOrEmpty()||
            userData.name.isNullOrEmpty()||
            userData.dob.isNullOrEmpty()
        ) {
            Toast.makeText(
                requireActivity(),
                "Please Complete Your Profile First",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.profileFragment)
        }
        else
            startActivity(
                Intent(requireActivity(), DoctorsActivity::class.java)
                    .putExtra(DoctorsActivity.SPECIALIST, data.name)
            )
    }


}