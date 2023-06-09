package com.example.vetuserapp.model.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vetuserapp.databinding.ItemDoctorRowBinding
import com.example.vetuserapp.model.data.Doctor

class DoctorAdapter(private val doctors: List<Doctor>): RecyclerView.Adapter<ViewHolderDoctor>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }



    interface OnItemClickCallback{
        fun onItemClicked(data: Doctor)
    }
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
                val doctor = doctors[position]
                tvDoctorName.text = doctor.name
                tvDoctorSpecialist.text =doctor.specialist
                Glide.with(ivRowDoctorImage)
                    .load(doctor.avatar)
                    .into(ivRowDoctorImage)
                tvItemQueue.text = (doctor.queue?.size?:0).toString()
                binding.root.setOnClickListener {
                    onItemClickCallback.onItemClicked(doctor)
                }
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