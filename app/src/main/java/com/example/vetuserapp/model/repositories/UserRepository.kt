package com.example.vetuserapp.model.repositories

import android.graphics.Bitmap
import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.util.References
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

object UserRepository{
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val userRef = db.collection(References.USER_COL)
    private val storage = Firebase.storage
    private val storageRef = storage.reference
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

    suspend fun createOrUpdateUserData(
        userData: User, file: Bitmap
    ) {
        val uid = auth.uid ?: throw (Exception("User Not Logged In"))
        val avatar = uploadImage(file,uid)
        userData.avatar = avatar
        db.collection(References.USER_COL).document(uid).set(userData).await()
    }

    suspend fun uploadImage(image: Bitmap, fileName: String): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val path = "UserData/Avatar/$fileName.jpg"
        val imageRef = storageRef.child(path)
        return try {
            imageRef.putBytes(data).await()
            val downloadUrl = imageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            throw e
        }
    }



}