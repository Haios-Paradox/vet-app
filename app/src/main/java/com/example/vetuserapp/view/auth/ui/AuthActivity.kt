package com.example.vetuserapp.view.auth.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetuserapp.R
import com.example.vetuserapp.databinding.ActivityAuthBinding
import com.example.vetuserapp.model.util.ViewModelFactory
import com.example.vetuserapp.view.auth.AuthViewModel
import com.example.vetuserapp.view.main.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        authViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AuthViewModel::class.java]

        authViewModel.loggedInUser.observe(this){
            if(it==null)
                Navigation.findNavController(
                    this, binding.fragmentContainerView.id
                ).navigate(R.id.loginFragment)
            else
                startActivity(
                    Intent(this, MainActivity::class.java).apply {
                        addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    }
                )
        }

        setContentView(binding.root)
    }
}