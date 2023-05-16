package com.example.vetuserapp.model.util

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vetuserapp.databinding.ItemSpecialistCardBinding
import com.example.vetuserapp.model.data.Doctor

class DoctorAdapter(private val doctors: List<Doctor>): RecyclerView.Adapter<ViewHolderDoctor>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDoctor {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolderDoctor, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
//TODO: This part here, change the ItemSpecialistCard, which means make UI... aaaaaaa...
class ViewHolderDoctor(val binding : ItemSpecialistCardBinding):RecyclerView.ViewHolder(binding.root)