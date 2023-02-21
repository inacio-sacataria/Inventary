package com.decode.microtic.data.repositories

import android.util.Log
import com.decode.microtic.data.AuthInstance
import com.decode.microtic.data.FirebaseInstance
import com.decode.microtic.data.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.coroutines.tasks.await

class UserRepositories {
    var db = FirebaseInstance.getInstance()!!.reference
    var myRef = db.child("users")
    var auth = Firebase.auth

   suspend fun createUser(
                            nome : String,
                            email : String,
                            role : String,
                            password:String ){
        auth!!
            .createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                var users = Users(
                    id = it.user!!.uid,
                    nome =nome,
                    role = role,
                    email = email
                )

                myRef.child(users.id!!).setValue(users)
            }
    }

    suspend fun getCurrenUser() : Users?{
        var user : Users? = null

       if(auth.currentUser !=null) {
            myRef.child(auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    user = it.getValue(Users::class.java)!!
                    Log.d("userss", "User ${user!!.nome}")
            }.addOnFailureListener {
                Log.d("userss", "Failed to get current User")
            }.await()
        }
        return user
    }

    fun login(email: String, password: String) {
        auth!!.signInWithEmailAndPassword(email,password)
    }
}