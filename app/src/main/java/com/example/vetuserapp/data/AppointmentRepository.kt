package com.example.vetuserapp.data

import android.content.Context
import com.example.vetuserapp.model.data.Appointment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class AppointmentRepository(context: Context, auth: FirebaseAuth) {

    private val ref = auth.currentUser?.let { References(it.uid) }

    fun getAllUserAppointments(
        uid : String?,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null || uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        ref.appointColRef.whereEqualTo("patientId",uid).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }
    fun getAllDoctorAppointments(
        doctorId : String?,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        ref.appointColRef.whereEqualTo("doctorId",doctorId).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun createAppointment(
        appointment: Appointment,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        ref.appointColRef.add(appointment)
            .addOnSuccessListener{
                it.get().addOnSuccessListener(onSuccess)
            }
            .addOnFailureListener(onFailure)
    }


}