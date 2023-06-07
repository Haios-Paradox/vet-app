package com.example.vetuserapp.model.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vetuserapp.databinding.ItemChatRowBinding
import com.example.vetuserapp.model.data.Chat

class ChatAdapter(private val messages: List<Chat>) :
    RecyclerView.Adapter<MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        with(holder){
            with(binding){
                Glide.with(ivSender).load(messages[position].avatar).circleCrop().centerCrop().into(ivSender)
                tvSenderName.text = messages[position].name
                tvSenderContent.text = messages[position].message
            }
        }
    }


}

class MessageViewHolder(
    val binding : ItemChatRowBinding
): RecyclerView.ViewHolder(binding.root)