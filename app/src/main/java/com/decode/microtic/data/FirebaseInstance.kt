package com.decode.microtic.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseInstance {
    private  var instance : FirebaseDatabase? = null

    fun getInstance(): FirebaseDatabase? {
        if (instance==null){
            instance = Firebase.database
        }
        return instance!!
    }
}


object AuthInstance {
    private var instance : FirebaseAuth? = null

    fun getInstance(): FirebaseAuth?{
        if (instance==null){
            instance = FirebaseAuth.getInstance()
        }
        return instance
    }
}