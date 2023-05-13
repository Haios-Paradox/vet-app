package com.example.vetuserapp.data

import android.content.Context
import com.example.vetuserapp.model.data.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase

class UserRepository(context: Context, auth: FirebaseAuth) {
    private val ref = auth.currentUser?.let { References(it.uid) }

    fun getUserData(
        onSuccess : (DocumentSnapshot) -> Unit,
        onFailure : (Exception) -> Unit
    ){
        if(ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        else{
            ref.userDataRef.get()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure)
        }
    }

    fun createOrUpdateUserData(
        userData: User,
        onSuccess: (Void) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if(ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }
        ref.userDataRef.set(userData)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

}