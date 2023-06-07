package com.example.vetuserapp.model.repositories

import com.example.vetuserapp.model.util.References
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object DoctorRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val doctorRef = db.collection(References.DOCTOR_COL)
    private val specRef = db.collection(References.SPEC_COL)

    suspend fun getDoctor(id:String) : Result<DocumentSnapshot>{
        return try{
            val snapshot = doctorRef.document(id).get().await()
            Result.success(snapshot)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getDoctors(): Result<QuerySnapshot?> {
        return try {
            val snapshot = doctorRef.get().await()
            Result.success(snapshot)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    fun getDoctors(
        specialist:String,
        onDoctorsChanged: (QuerySnapshot?) -> Unit
    ): Result<ListenerRegistration?> {
        return try {
            val regis = doctorRef.whereEqualTo("specialist",specialist).addSnapshotListener { value, error ->
                onDoctorsChanged(value)
            }
            Result.success(regis)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSpecialist(): Result<QuerySnapshot?> {
        return try{
            val specialist = specRef.get().await()
            Result.success(specialist)
        }catch (e:Exception){
            Result.failure(e)
        }

    }
}