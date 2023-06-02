package com.example.vetuserapp.view.diagnosis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetuserapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetuserapp.databinding.FragmentChatBinding
import com.example.vetuserapp.model.data.Chat
import com.example.vetuserapp.model.util.ChatAdapter
import java.util.*

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var adapter : ChatAdapter
    val currentDate = Date()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        diagnosisViewModel = ViewModelProvider(requireActivity()).get(DiagnosisViewModel::class.java)
        binding.rvChat.layoutManager = LinearLayoutManager(requireActivity())
        diagnosisViewModel.chatData.observe(requireActivity()){
            adapter = ChatAdapter(it)
            binding.rvChat.adapter = adapter
        }

        diagnosisViewModel.user.observe(requireActivity()){user->
            binding.btnSend.setOnClickListener {
                val chat = Chat(
                    null,
                    0,
                    user.name,
                    user.avatar,
                    binding.textSend.text.toString(),
                    null,
                    null,
                    currentDate.time
                    )
                diagnosisViewModel.sendChat(chat)
            }
        }

        return binding.root
    }

}