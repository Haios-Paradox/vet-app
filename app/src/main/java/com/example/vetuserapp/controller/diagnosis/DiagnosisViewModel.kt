package com.example.vetuserapp.controller.diagnosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetuserapp.model.data.Chat
import com.example.vetuserapp.model.data.Prescription
import com.example.vetuserapp.model.repositories.ChatRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch

class DiagnosisViewModel: ViewModel(){
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _chatData = MutableLiveData<List<Chat>>()
    val chatData: LiveData<List<Chat>> = _chatData

    private val _prescription = MutableLiveData<Prescription>()
    val prescription: LiveData<Prescription> = _prescription

    fun loadChats(appointmentId: String) {
        ChatRepository.getMessages(appointmentId) {
            _chatData.value = it
        }
    }

    fun sendChat(appointmentId: String, message: Chat){
        viewModelScope.launch {
            try {
                ChatRepository.sendMessage(appointmentId,message)
            }catch (e: FirebaseFirestoreException) {
                _error.value = e
            }
        }
    }


}