package com.example.vetuserapp.model.repositories

import com.example.vetuserapp.model.util.References
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DoctorRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val doctorRef = db.collection(References.DOCTOR_COL)
    private val specRef = db.collection(References.SPEC_COL)
    fun getDoctors(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val uid = auth.uid
        if (uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        doctorRef.get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun getDoctors(
        specialist:String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val uid = auth.uid
        if (uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        doctorRef.whereEqualTo("specialist",specialist).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun getSpecialist(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        specRef.get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }
}