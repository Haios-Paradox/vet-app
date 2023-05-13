package com.example.vetuserapp.model.data

import java.sql.Timestamp

data class Appointment(
    val patientId: String? = null,
    val doctorId : String? = null,
    val timeCreated : Timestamp? = null,
)
