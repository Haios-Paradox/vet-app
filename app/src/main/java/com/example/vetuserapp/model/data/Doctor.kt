package com.example.vetuserapp.model.data

data class Doctor(
    val avatar: String ?=null,
    val name : String?=null,
    val specialist: String? =null,
    val email: String?=null,
    val phone: String?=null,
    val experience: String?=null,
    val description: String?=null,
    val fee: Double?=null,
    var limit: Int?=0,
    var available : Boolean?=false,
    val queue: List<String>?= emptyList(),
)
