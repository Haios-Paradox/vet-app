package com.example.vetuserapp.model.repositories

import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.util.References
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object UserRepository{
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val userRef = db.collection(References.USER_COL)

    fun getUserData(
        onSuccess : (DocumentSnapshot) -> Unit,
        onFailure : (Exception) -> Unit
    ){
        val uid = auth.uid
        if (uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        else{
            userRef.document(uid).get()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure)
        }
    }

    suspend fun createOrUpdateUserData(
        userData: User,
    ){
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        db.collection(References.USER_COL).document(uid).set(userData).await()
    }

}