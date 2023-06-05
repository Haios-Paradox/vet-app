package com.example.vetuserapp.model.repositories

import com.example.vetuserapp.model.data.User
import com.example.vetuserapp.model.util.References
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    fun getUser(): String? {
        return auth.uid
    }

    fun login(email: String, password: String,
              onSuccess: (AuthResult) -> Unit,
              onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun register(
        user: User, email: String, password: String,
        onResult: (Void?,Exception?) -> Unit,
    ){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener{newUser->
                user.id = newUser.user?.uid
                db.collection(References.USER_COL).document(newUser.user!!.uid)
                    .set(user)
                    .addOnSuccessListener{
                        onResult(it,null)
                    }
                    .addOnFailureListener{
                        onResult(null,it)
                    }
            }
            .addOnFailureListener{
                onResult(null,it)
            }
    }
}