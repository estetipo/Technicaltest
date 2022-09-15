package com.carlos.satori.technical_test.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay

class FirebaseStorage {
    private val database = Firebase.storage
    private val storageRef = database.getReference("images")

    //Methods to fetch and save images
    suspend fun getImages(): List<String> {
        val list = mutableListOf<String>()
        storageRef.listAll().addOnSuccessListener{ result->
            result.items.forEach{ url ->
                url.downloadUrl.addOnSuccessListener {
                    list.add(it.toString())
                }
            }
        }
        delay(2000)
        return list
    }

    suspend fun saveImage(image: Uri, name: String): String {
        val imageRef = storageRef.child(name)
        var url = ""
        imageRef.putFile(image).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                url = it.toString()
            }
        }
        return url
    }
}