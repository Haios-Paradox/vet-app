package com.example.vetuserapp.model.data

import java.sql.Date

data class User(
    val name: String?=null,
    val email: String?=null,
    val phone: String?=null,
    val avatar: String?=null,
    val gender: String?=null,
    val desc: String?=null,
    val dob: Date?=null,
)
