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

    suspend fun getUserData(): Result<DocumentSnapshot?> {
        val uid = auth.uid
        return if (uid == null) {
            Result.failure(Exception("User Not Logged In"))
        } else{
            try{
                val doc = userRef.document(uid).get().await()
                Result.success(doc)
            }catch (e:Exception){
                Result.failure(e)
            }
        }
    }

    suspend fun createOrUpdateUserData(
        userData: User,
    ):Result<Void>{
        val uid = auth.uid ?: return Result.failure(Exception("User Not Logged In"))
        return try{
            val result = db.collection(References.USER_COL).document(uid).set(userData).await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

}