package com.example.vetuserapp.model.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vetuserapp.databinding.ItemDoctorRowBinding
import com.example.vetuserapp.model.data.Doctor

class DoctorAdapter(private val doctors: List<Doctor>): RecyclerView.Adapter<ViewHolderDoctor>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDoctor {
        val binding = ItemDoctorRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolderDoctor(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderDoctor, position: Int) {
        with(holder){
            with(binding){
                tvDoctorName.text = doctors[position].name
                tvDoctorSpecialist.text = doctors[position].specialist
                Glide.with(ivRowDoctorImage)
                    .load(doctors[position].avatar)
                    .into(ivRowDoctorImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return doctors.count()
    }
}
class ViewHolderDoctor(
    val binding : ItemDoctorRowBinding
    ):RecyclerView.ViewHolder(binding.root)