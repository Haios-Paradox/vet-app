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
import com.bumptech.glide.Glide
import com.example.vetuserapp.controller.diagnosis.DiagnosisViewModel
import com.example.vetuserapp.databinding.FragmentCheckUpBinding

class CheckUpFragment : Fragment() {
    private lateinit var binding : FragmentCheckUpBinding
    private lateinit var diagnosisViewModel: DiagnosisViewModel
    private val cameraPermission = android.Manifest.permission.CAMERA
    private val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val mediaPermission = android.Manifest.permission.ACCESS_MEDIA_LOCATION
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCheckUpBinding.inflate(inflater,container,false)
        diagnosisViewModel = ViewModelProvider(requireActivity()).get(DiagnosisViewModel::class.java).also {
            it.loading.observe(requireActivity()) {
                if (!it)
                    binding.progressBar2.visibility = View.GONE
                else
                    binding.progressBar2.visibility = View.VISIBLE
            }
        }

        diagnosisViewModel.appointment.observe(requireActivity()){appointment ->
            Glide.with(binding.ivPetr).load(appointment.photo).into(binding.ivPetr)
            binding.edProblemCheck.setText(appointment.description)
            binding.edPetName.setText(appointment.patientName)
            if(appointment.payment!=null)
                Glide.with(binding.ivPaymenr).load(appointment.payment).into(binding.ivPaymenr)
            binding.btnSaveAppointment.setOnClickListener {
                appointment.patientName = binding.edPetName.text.toString()
                appointment.description = binding.edProblemCheck.text.toString()
                diagnosisViewModel.updateAppointment(appointment)
            }

            if(appointment.paid == true){
                binding.tvFeeD.text = "Complete"
            }else if(appointment.payment!=null){
                binding.tvFeeD.text = "Review"
            }
            else
                binding.tvFeeD.text = "Please Pay"
        }

        diagnosisViewModel.doctor.observe(requireActivity()){
            binding.tvFeeCuz.setText(it.fee.toString())
        }

        diagnosisViewModel.payBitmap.observe(requireActivity()){
            if(it!=null)
                Glide.with(binding.ivPaymenr).load(it).into(binding.ivPaymenr)
        }

        diagnosisViewModel.queue.observe(requireActivity()){
            if(it>1){
                binding.tvStatusCheck.setText("Status: In Queue, $it Remaining")
            }else if(it==-1){
                binding.tvStatusCheck.setText("Status: Finished")
                binding.cvPayment.visibility = View.GONE
            }else{
                binding.tvStatusCheck.setText("Status: Ongoing")
            }
        }

        binding.btnPay.setOnClickListener {
            dispatchTakePictureIntent()
        }

        return binding.root
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            diagnosisViewModel.storePayment(imageBitmap,100)
        }
    }
    private val documentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the result of the file picker intent here
        // The selected image can be loaded from the URI using a ContentResolver
        val contentResolver = requireActivity().contentResolver
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        diagnosisViewModel.storePayment(imageBitmap, 100)
    }

    private fun dispatchTakePictureIntent() {
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