package com.example.vetuserapp.view.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.auth.AuthViewModel
import com.example.vetuserapp.databinding.FragmentForgotBinding

class ForgotFragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : FragmentForgotBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding = FragmentForgotBinding.inflate(inflater,container,false)
        binding.button2.setOnClickListener {
            authViewModel.resetPass(
                binding.editTextTextEmailAddress.text.toString()
            )
        }
        return binding.root
    }
}