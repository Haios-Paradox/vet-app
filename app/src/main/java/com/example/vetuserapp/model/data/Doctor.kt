package com.example.vetuserapp.model.data

data class Doctor(
    val avatar: String ?=null,
    val name : String?=null,
    val specialist: String? =null,
    val queue: List<Appointment> ?= null,
    val currentNumber: Int? = null,
    val nextNumber: Int? = null
)
