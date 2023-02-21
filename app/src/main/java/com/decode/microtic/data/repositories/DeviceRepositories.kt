package com.decode.microtic.data.repositories

import android.util.Log
import com.decode.microtic.data.FirebaseInstance
import com.decode.microtic.data.models.Devices
import com.decode.microtic.utils.Contants
import kotlinx.coroutines.tasks.await

class DeviceRepositories {
    var db = FirebaseInstance.getInstance()!!.reference
    var myRef = db.child("devices")

    suspend fun insertData(device : Devices){
        myRef.child(device.id!!).setValue(device);
    }

    suspend fun update(device: Devices){
        myRef.child(device.id!!).updateChildren(device.toMap())
    }

    suspend fun getAllData(): MutableList<Devices>{
        var mutableList = mutableListOf<Devices>()
        myRef.get().addOnSuccessListener {
            for (snap in it.children){
                var devices = snap.getValue(Devices::class.java)
                mutableList.add(devices!!)
            }
        }.addOnFailureListener {
            Log.d("devices","Error on get produts ${it.message}")
        }.await()
        Contants.allDevices = mutableList
        return mutableList
    }

     fun getAllDevices(): MutableList<Devices>{
        var mutableList = mutableListOf<Devices>()
        myRef.get().addOnSuccessListener {
            for (snap in it.children){
                var devices = snap.getValue(Devices::class.java)
                mutableList.add(devices!!)
            }
        }.addOnFailureListener {
            Log.d("devices","Error on get produts ${it.message}")
        }
        return mutableList
    }

    fun updateResposanvel(device: Devices) {
        myRef.child(device.id!!).setValue(device)
        Log.d("entreiii","update reponsavel")
        Log.d("entreiii","${device.id}")
    }

    fun updatePhotoUrl(device: Devices) {
        myRef.child(device.id!!).updateChildren(device.toMap())
    }

    suspend fun delete(device: Devices) {
        myRef.child(device.id!!).removeValue()
    }
}