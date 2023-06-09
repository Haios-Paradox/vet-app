package com.example.vetuserapp.model.data

data class Appointment(
    var id :String?=null,
    val patientId : String?=null,
    var patientName: String?=null,
    val doctorId:String?=null,
    val doctorName:String?=null,
    var photo : String?=null,
    var description: String?=null,
    val analysis:String?=null,
    val treatment:String?=null,
    val timestamp : Long?=null,
    val complete : Boolean?=false,
    val paid : Boolean?=false,
    var payment : String?=null
)