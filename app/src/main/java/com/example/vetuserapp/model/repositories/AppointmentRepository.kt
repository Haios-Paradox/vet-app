package com.example.vetuserapp.model.repositories

import com.example.vetuserapp.model.data.Appointment
import com.example.vetuserapp.model.util.References
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AppointmentRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    val appointRef = db.collection(References.APPOINT_COL)

    fun getAllUserAppointmentsHistory(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val uid = auth.uid
        if (uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        appointRef.whereEqualTo("patientId",uid).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }
    fun getDoctorQueue(
        doctorId : String?,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        appointRef.whereEqualTo("doctorId",doctorId).whereEqualTo("complete",false).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun createAppointment(
        appointment: Appointment,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){

        appointRef.add(appointment)
            .addOnSuccessListener{
                it.get().addOnSuccessListener(onSuccess)
            }
            .addOnFailureListener(onFailure)
    }


}