package com.example.vetuserapp.view.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetuserapp.R
import com.example.vetuserapp.controller.main.HomeViewModel
import com.example.vetuserapp.databinding.ActivityMainBinding
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.view.auth.ui.AuthActivity
import com.google.firebase.firestore.ktx.toObject

class MainActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        homeViewModel = ViewModelProvider(this).get(
            HomeViewModel::class.java)
        homeViewModel.error.observe(this){
            Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
        }

        homeViewModel.userData.observe(this){
            val userData = it.toObject<User>()
            if(
                userData?.avatar == null ||
                userData.desc==null||
                userData.phone==null||
                userData.name==null||
                userData.dob==null
            ){
                Navigation.findNavController(
                    this, binding.fragmentContainerView4.id
                ).navigate(R.id.profileFragment)
                Toast.makeText(this,"Please Complete Your Profile First~",Toast.LENGTH_SHORT).show()
            }
        }



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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                homeViewModel.logout()
                startActivity(
                    Intent(this, AuthActivity::class.java).apply {
                        addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    }
                )
            }

        }
        return true
    }

}