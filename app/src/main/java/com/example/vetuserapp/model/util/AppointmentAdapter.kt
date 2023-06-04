package com.example.vetuserapp.model.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vetuserapp.databinding.ItemAppointmentRowBinding
import com.example.vetuserapp.model.data.Appointment

class AppointmentAdapter(private val appointment: List<Appointment>): RecyclerView.Adapter<ViewHolderAppointment>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    interface OnItemClickCallback{
        fun onItemClicked(data: Appointment)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAppointment {
        val binding = ItemAppointmentRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolderAppointment(binding)
    }

    override fun getItemCount(): Int {
        return appointment.count()
    }

    override fun onBindViewHolder(holder: ViewHolderAppointment, position: Int) {
        with(holder){
            with(binding){
                tvAppoDate.text = appointment[position].timestamp.toString()
                tvNameDoctor.text = appointment[position].doctorName.toString()
                tvStatus.text = appointment[position].complete.toString()
                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(appointment[position])
                }
            }
        }
    }
}

class ViewHolderAppointment(val binding : ItemAppointmentRowBinding): RecyclerView.ViewHolder(binding.root)