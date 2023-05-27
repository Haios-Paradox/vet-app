package com.example.vetuserapp.view.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.main.HomeViewModel
import com.example.vetuserapp.databinding.FragmentProfileBinding
import com.example.vetuserapp.model.data.User
import com.google.firebase.firestore.ktx.toObject

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var homeViewModel : HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        homeViewModel.userData.observe(requireActivity()){
            val userData = it.toObject<User>()
            binding.edProfileName.setText(userData?.name?:"N/A")
            binding.edProfileEmail.setText(userData?.email?:"N/A")
            binding.edProfileDob.setText(userData?.dob?.toString()?:"N/A")
            binding.edHomePhone.setText(userData?.phone?:"N/A")
        }

        return binding.root
    }

}