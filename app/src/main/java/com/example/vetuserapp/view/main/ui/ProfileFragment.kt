package com.example.vetuserapp.view.main.ui

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
import com.example.vetuserapp.controller.main.HomeViewModel
import com.example.vetuserapp.databinding.FragmentProfileBinding
import com.example.vetuserapp.model.data.User
import com.google.firebase.firestore.ktx.toObject

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var homeViewModel : HomeViewModel
    private val cameraPermission = android.Manifest.permission.CAMERA
    private val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val mediaPermission = android.Manifest.permission.ACCESS_MEDIA_LOCATION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(storagePermission,mediaPermission,cameraPermission),
            4
        )
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java).also {
            it.loading.observe(requireActivity()) {
                if (!it)
                    binding.progressBar8.visibility = View.GONE
                else
                    binding.progressBar8.visibility = View.VISIBLE
            }
        }

        homeViewModel.userData.observe(requireActivity()){
            val userData = it.toObject<User>()
            if(userData?.avatar?.isNotEmpty() == true)
                Glide.with(binding.imageView2).load(userData.avatar).into(binding.imageView2)
            binding.tvProfileName.text = (userData?.name?:"")
            binding.edProfileName.setText(userData?.name?:"")
            binding.edProfileEmail.setText(userData?.email?:"")
            binding.edProfileDob.setText(userData?.dob?:"")
            binding.edHomePhone.setText(userData?.phone?:"")
            binding.edProfileDesc.setText(userData?.desc?:"")
            setupFab(userData?.id!!)
        }
        binding.imageView2.setOnClickListener {
            dispatchTakePictureIntent()
        }

        homeViewModel.imageBitmap.observe(requireActivity()){
            if(it!=null)
                Glide.with(binding.imageView2).load(it).into(binding.imageView2)
        }

        return binding.root
    }

    private fun setupFab(userId: String){
        binding.fabEditProfile.setOnClickListener {
            val user =  User(
                userId,
                binding.edProfileName.text.toString(),
                binding.edProfileEmail.text.toString(),
                binding.edHomePhone.text.toString(),
                avatar = homeViewModel.userData.value?.toObject<User>()?.avatar,
                "",
                binding.edProfileDesc.text.toString(),
                binding.edProfileDob.text.toString()
            )
            if(homeViewModel.imageBitmap.value!=null)
                homeViewModel.updateUserData(user, homeViewModel.imageBitmap.value!!)
            else
                homeViewModel.updateUserData(user)
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            homeViewModel.storeImage(imageBitmap,100)
        }
    }
    private val documentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the result of the file picker intent here
        // The selected image can be loaded from the URI using a ContentResolver
        val contentResolver = requireActivity().contentResolver
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        homeViewModel.storeImage(imageBitmap, 100)
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