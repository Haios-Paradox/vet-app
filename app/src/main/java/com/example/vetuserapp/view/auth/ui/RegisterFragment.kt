package com.example.vetuserapp.view.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.auth.AuthViewModel
import com.example.vetuserapp.databinding.FragmentRegisterBinding
import com.example.vetuserapp.model.data.User

class RegisterFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java].also {
            it.loading.observe(requireActivity()) {
                if (!it)
                    binding.progressBar.visibility = View.GONE
                else
                    binding.progressBar.visibility = View.VISIBLE
            }
        }

        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextRegisterEmail.text.toString()
            val pass = binding.editTextRegisterPassword.text.toString()
            val name = binding.editTextRegisterFullName.text.toString()
            val phone = binding.editTextRegisterMobile.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty())
                authViewModel.register(
                    email,pass,
                    User("",name,email,phone)
                )
            else
                Toast.makeText(requireActivity(),"Please Fill In All Fields", Toast.LENGTH_SHORT).show()
        }





        return binding.root
    }

}