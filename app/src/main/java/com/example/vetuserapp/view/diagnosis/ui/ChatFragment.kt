package com.example.vetuserapp.view.diagnosis.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vetuserapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetuserapp.databinding.FragmentChatBinding
import com.example.vetuserapp.model.data.Chat
import com.example.vetuserapp.model.util.ChatAdapter
import java.util.*

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private lateinit var adapter : ChatAdapter
    private val cameraPermission = android.Manifest.permission.CAMERA
    private val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val mediaPermission = android.Manifest.permission.ACCESS_MEDIA_LOCATION
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        diagnosisViewModel = ViewModelProvider(requireActivity()).get(DiagnosisViewModel::class.java)
        val rvLayout = LinearLayoutManager(requireActivity())
        rvLayout.reverseLayout = true
        binding.rvChat.layoutManager = rvLayout

        diagnosisViewModel.chatData.observe(requireActivity()){
            adapter = ChatAdapter(it.sortedBy { it.timestamp }.reversed())
            binding.rvChat.adapter = adapter
        }

        diagnosisViewModel.imageBitmap.observe(requireActivity()){
            Glide.with(binding.ivAttach).load(it).into(binding.ivAttach)
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
                    Date().time
                    )
                diagnosisViewModel.sendChat(chat)
                binding.textSend.setText("")
            }
            binding.btnAttach.setOnClickListener {
                dispatchTakePictureIntent()
            }
        }

        return binding.root
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            diagnosisViewModel.storeImage(imageBitmap,100)
        }
    }
    private val documentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the result of the file picker intent here
        // The selected image can be loaded from the URI using a ContentResolver
        val contentResolver = requireActivity().contentResolver
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        diagnosisViewModel.storeImage(imageBitmap, 100)
    }

    fun dispatchTakePictureIntent() {
        val pickImage = "Pick Image"
        val takePhoto = "Take Photo"

        val options = arrayOf<CharSequence>(pickImage, takePhoto)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select Image Source")
        builder.setItems(options) { _, item ->
            when (options[item]) {
                pickImage -> {
                    if (ContextCompat.checkSelfPermission(requireActivity(), storagePermission) != PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(requireActivity(),mediaPermission)) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(storagePermission,mediaPermission),
                            4
                        )
                    } else {
                        // Permission granted, launch file picker intent
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*" // only allow image file types
                        documentLauncher.launch(intent.type)
                    }
                }
                takePhoto -> {
                    if (ContextCompat.checkSelfPermission(
                            requireActivity(),
                            cameraPermission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Permission not granted, request it
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(cameraPermission),
                            6
                        )
                    } else {
                        // Permission granted, launch camera intent
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        resultLauncher.launch(intent)
                    }
                }
            }
        }
        builder.show()
    }

}