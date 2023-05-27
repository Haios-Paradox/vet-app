package com.example.vetuserapp.model.data

data class Appointment(
    val id :String?=null,
    val patientId : String?=null,
    val patientName: String?=null,
    val doctorId:String?=null,
    val doctorName:String?=null,
    val photo : String?=null,
    val description: String?=null,
    val prescription: Prescription?=null,
    val timestamp : Long?=null,
    val complete : Boolean?=false
)