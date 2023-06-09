package com.example.vetuserapp.view.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.vetuserapp.R
import com.example.vetuserapp.controller.auth.AuthViewModel
import com.example.vetuserapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        binding.buttonLogin.setOnClickListener {
            val email = binding.editEmailLogin.text.toString()
            val pass = binding.editPassLogin.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty())
                authViewModel.login(email,pass)
            else
                Toast.makeText(requireActivity(),"Please Fill In All Fields", Toast.LENGTH_SHORT).show()
        }

        binding.tvRegister.setOnClickListener {
            it.findNavController().navigate(R.id.registerFragment)
        }
        binding.tvForgot.setOnClickListener {
            it.findNavController().navigate(R.id.forgotFragment)
        }


        return binding.root
    }

}