package com.example.vetuserapp.view.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetuserapp.R
import com.example.vetuserapp.controller.main.HomeViewModel
import com.example.vetuserapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this).get(
            HomeViewModel::class.java)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_profile -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView4.id
                    ).navigate(R.id.profileFragment)
                    true
                }
                R.id.navigation_doctors ->{
                    Navigation.findNavController(
                        this, binding.fragmentContainerView4.id
                    ).navigate(R.id.specialistFragment)
                    true
                }
                R.id.navigation_history -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView4.id
                    ).navigate(R.id.historyFragment)
                    true
                }
                else -> false
            }
        }
    }
}