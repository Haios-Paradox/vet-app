package com.example.vetuserapp.view.diagnosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vetuserapp.model.controller.MainRepository
import com.example.vetuserapp.model.data.User
import com.google.firebase.firestore.DocumentSnapshot

class DiagnosisViewModel(private val repo:MainRepository) {
    private val _chats = MutableLiveData<List<DocumentSnapshot>>()
    val chats: LiveData<List<DocumentSnapshot>> = _chats

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun loadChat(appointId:String){
        repo.loadChat(
            appointId,
            onSuccess = {
                _chats.value = it.documents
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun observeChat(appointId: String){
        repo.observeChat(
            appointId,
            onSuccess = {
                addChatMessage(it)
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    fun sendChat(user: User, appoint:String, message: String){
        repo.sendMessage(
            user,appoint,message,
            onSuccess = {
                //Nothing For now, if the new chat does not show up immediately, then put it here
            },
            onFailure = {
                _error.value = it
            }
        )
    }

    private fun addChatMessage(documentSnapshot: DocumentSnapshot) {
        val currentChats = _chats.value ?: emptyList()
        val updatedChats = currentChats.toMutableList()
        updatedChats.add(documentSnapshot)
        _chats.value = updatedChats
    }
}