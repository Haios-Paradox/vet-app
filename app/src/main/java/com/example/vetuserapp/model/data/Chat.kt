package com.example.vetuserapp.model.data

data class Chat(
    val chatId: String?= null,
    val senderType: Int?= null, //Patient =0, Doctor =1
    val name: String?= null, // Name of sender
    val avatar: String ?= null, //Photo of sender
    val message: String ?= null, // Message of Sender
    val sender: String?= null, // I have no idea, let's keep it null
    var content: String?= null, //ANY MEDIA CONTENT ATTACHED
    val timestamp: Long?= null, // Time sent
)