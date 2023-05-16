package com.example.vetuserapp.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot

class DoctorRepository(context: Context, auth: FirebaseAuth) {
    private val ref = auth.currentUser?.let { References(it.uid) }


    fun getDoctors(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        ref.doctorColRef.get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun getDoctors(
        specialist:String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        ref.doctorColRef.whereEqualTo("specialist",specialist).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun getSpecialist(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        ref.specialistColRef.get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }
}