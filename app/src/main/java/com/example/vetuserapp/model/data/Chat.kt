package com.example.vetuserapp.model.data

import java.sql.Timestamp

data class Chat(
    val senderType: Int?=null, //Patient =0, Doctor =1
    val name: String?=null,
    val avatar: String ?= null,
    val message: String ?= null,
    val timestamp: Timestamp ?= null,
)