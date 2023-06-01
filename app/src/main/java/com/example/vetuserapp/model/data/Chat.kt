package com.example.vetuserapp.model.data

data class Chat(
    val chatId: String?= null,
    val senderType: Int?= null, //Patient =0, Doctor =1
    val name: String?= null,
    val avatar: String ?= null,
    val message: String ?= null,
    val sender: String?= null,
    var content: String?= null, //ANY MEDIA CONTENT ATTACHED
    val timestamp: Long?= null,
)