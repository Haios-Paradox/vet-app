package com.example.vetuserapp.model.controller

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.vetuserapp.data.AppointmentRepository
import com.example.vetuserapp.data.ChatRepository
import com.example.vetuserapp.data.DoctorRepository
import com.example.vetuserapp.data.UserRepository
import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.data.Chat
import com.example.vetuserapp.model.data.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class MainRepository(
    private val appointment: AppointmentRepository,
    private val doctor: DoctorRepository,
    private val user: UserRepository,
    private val chat: ChatRepository,
    private val auth : FirebaseAuth,
) {

    val uid = auth.currentUser?.uid

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Login function...
     * @param email is self explanatory
     * @param password is also self explanatory
     * @param onSuccess returns auth result if successful
     * @param onFailure returns an exception if anything went wrong
     */
    fun login(
        email : String,
        password: String,
        onSuccess: (AuthResult) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    /**
     * Function to register.
     * @param email is self explanatory
     * @param password is also self explanatory
     * @param userData is the user data sent during registration
     * @param onSuccess returns the newly created user document
     * @param onFailure returns an exception if anything went wrong
     */
    fun register(
        email : String,
        password: String,
        userData: User,
        onSuccess: (Void) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener{
                user.createOrUpdateUserData(
                    userData,
                    onSuccess,
                    onFailure
                )
            }
            .addOnFailureListener(onFailure)
    }

    /**
     * Function to Update User Data
     * Because of some firebase witchcraft, the function to create and
     * update the user data is the exact same.
     * @param userData is the Data you want to create/update
     * @param onSuccess is the result of the update
     * @param onFailure is the exception if anything went wrong
     */
    fun updateUserData(
        userData: User,
        onSuccess: (Void) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        user.createOrUpdateUserData(userData,onSuccess,onFailure)
    }

    fun getUserData(
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        user.getUserData(onSuccess,onFailure)
    }

    /**
     * Get a List of Doctors based on Specialists
     * @param specialist is the type of it, like TUTLE for example
     * If no specialist is entered, then it will retrieve all doctors
     */
    fun getSpecialistDoctorList(
        specialist : String?=null,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if(specialist !=null)
            doctor.getDoctors(specialist,onSuccess,onFailure)
        else
            doctor.getDoctors(onSuccess,onFailure)
    }

    /**
     * Basically schedule an appointment... Get in line I guess
     * @param appoint is the details of the appointment
     * @param onSuccess is the result, which is just a document of it.
     * @param onFailure is if anything went wrong.
     */
    fun makeAppointment(
        appoint: Appointment,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        appointment.createAppointment(
            appoint,onSuccess,onFailure
        )
    }

    /**
     * Get all user's appointments. Finished, Ongoing, and in Queue.
     * @param onSuccess is used to get the data
     * @param onFailure is used if there's an error
     */
    fun getAllUserAppointment(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        appointment.getAllUserAppointments(
            uid,onSuccess,onFailure
        )
    }

    /**
     * Get all doctor's appointment.
     * @param doctorId is the doctor that you wanna get the appointments.
     * @param onSuccess is the result
     * @param onFailure is if there's any error
     */
    fun getAllDoctorAppointment(
        doctorId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        appointment.getAllDoctorAppointments(
            doctorId,onSuccess,onFailure
        )
    }

    fun loadChat(
        appointId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        chat.loadChatMessages(
            appointId, onSuccess, onFailure
        )
    }

    fun observeChat(
        appointId: String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        chat.observeIncomingMessages(
            appointId,onSuccess,onFailure
        )
    }

    fun sendMessage(
        user:User,
        appointId: String,
        message:String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        chat.sendMessageToFirestore(
            user, message,appointId, onSuccess, onFailure
        )
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(context: Context): MainRepository {
            return instance ?: synchronized(MainRepository::class.java) {

                val auth = FirebaseAuth.getInstance()

                if (instance == null) {
                    val appointment = AppointmentRepository(context,auth)
                    val doctor = DoctorRepository(context,auth)
                    val user = UserRepository(context,auth)
                    val chat = ChatRepository(context,auth)
                    instance = MainRepository(appointment,doctor,user, chat, auth)
                }
                return instance as MainRepository
            }
        }
    }
}