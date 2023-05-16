package com.example.vetuserapp.model.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vetuserapp.databinding.ItemSpecialistCardBinding
import com.example.vetuserapp.model.data.Specialist

class SpecialistAdapter(private val specialist: List<Specialist>): RecyclerView.Adapter<ViewHolderSpecialist>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSpecialist {
        val binding = ItemSpecialistCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolderSpecialist(binding)
    }

    override fun getItemCount(): Int {
        return specialist.count()
    }

    override fun onBindViewHolder(holder: ViewHolderSpecialist, position: Int) {
        with(holder){
            with(binding){
                tvSpecialty.text = specialist[position].name
                Glide.with(ivSpecialty)
                    .load(specialist[position].picture)
                    .into(ivSpecialty)
            }
        }
    }
}

class ViewHolderSpecialist(
    val binding : ItemSpecialistCardBinding
    ):RecyclerView.ViewHolder(binding.root)