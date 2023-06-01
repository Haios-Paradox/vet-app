package com.example.vetuserapp.view.diagnosis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vetuserapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetuserapp.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private lateinit var diagnosisViewModel: DiagnosisViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        diagnosisViewModel = ViewModelProvider(requireActivity()).get(DiagnosisViewModel::class.java)

        return binding.root
    }

}