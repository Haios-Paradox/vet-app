package com.example.vetuserapp.view.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.R
import com.example.vetuserapp.databinding.FragmentProfileBinding
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.view.main.HomeViewModel
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
            binding.textViewShowFullName.text = userData?.name?:"N/A"
            binding.textViewShowEmail.text = "N/A"
            binding.textViewShowMobile.text= userData?.phone?:"N/A"
            //Put the rest here
        }

        return binding.root
    }

}