package com.example.vetuserapp.model.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vetuserapp.databinding.ItemAppointmentRowBinding
import com.example.vetuserapp.model.data.Appointment
import java.text.SimpleDateFormat
import java.util.*

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
                tvAppoDate.text = convertTimestampToString(appointment[position].timestamp!!)
                tvNameDoctor.text = appointment[position].doctorName.toString()
                tvStatus.text = appointment[position].complete.toString()
                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(appointment[position])
                }
            }
        }
    }
    fun convertTimestampToString(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return sdf.format(calendar.time)
    }
}

class ViewHolderAppointment(val binding : ItemAppointmentRowBinding): RecyclerView.ViewHolder(binding.root)