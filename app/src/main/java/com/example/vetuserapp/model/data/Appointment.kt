package com.example.vetuserapp.model.data

import java.sql.Timestamp

data class Appointment(
    val patientId: String? = null,
    val patientName: String? = null,
    val doctorId : String? = null,
    val doctorName : String? = null,
    val timeCreated : Timestamp? = null,
    val perscription: Perscription? = null,
    val status: String? = null,
    val queue: Int? = null
)