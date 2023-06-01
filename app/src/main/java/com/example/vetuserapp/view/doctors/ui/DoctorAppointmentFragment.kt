package com.example.vetuserapp.view.doctors.ui

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
import com.example.vetuserapp.controller.doctors.DoctorsViewModel
import com.example.vetuserapp.databinding.FragmentDoctorAppointmentBinding
import com.example.vetuserapp.view.diagnosis.ui.DiagnosisActivity

class DoctorAppointmentFragment : Fragment() {

    private lateinit var binding : FragmentDoctorAppointmentBinding
    private lateinit var doctorsViewModel: DoctorsViewModel
    private val cameraPermission = android.Manifest.permission.CAMERA
    private val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val mediaPermission = android.Manifest.permission.ACCESS_MEDIA_LOCATION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDoctorAppointmentBinding.inflate(layoutInflater,container, false)
        doctorsViewModel = ViewModelProvider(requireActivity())[DoctorsViewModel::class.java]

        doctorsViewModel.newAppointment.observe(requireActivity()){
            startActivity(
                Intent(requireActivity(), DiagnosisActivity::class.java)
                    .putExtra(DiagnosisActivity.APPOINTMENT, it.id)
            )
        }
        binding.btnBook.setOnClickListener {
            bookAppointment()
        }
        binding.ivPetImage.setOnClickListener {
            dispatchTakePictureIntent()
        }
        return binding.root
    }

    private fun bookAppointment() {
        doctorsViewModel.createAppointment(
            binding.edAppoDescription.text.toString()
        )

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            doctorsViewModel.storeImage(imageBitmap,100)
        }
    }
    private val documentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the result of the file picker intent here
        // The selected image can be loaded from the URI using a ContentResolver
        val contentResolver = requireActivity().contentResolver
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        doctorsViewModel.storeImage(imageBitmap, 100)
    }

    fun dispatchTakePictureIntent() {
        val pickImage = "Pick Image"
        val takePhoto = "Take Photo"

        val options = arrayOf<CharSequence>(pickImage, takePhoto)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select Image Source")
        builder.setItems(options) { dialog, item ->
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