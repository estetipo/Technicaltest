package com.carlos.satori.technical_test.data.firebase

import android.util.Log
import com.carlos.satori.technical_test.App.Companion.firebaseInstance
import com.carlos.satori.technical_test.data.model.Location

class FirebaseDatabase {
    //Method to add a locatoin to the firebase storage
    fun addLocation(location: Location, onSuccess: () -> Unit, onFailiure: (String) -> Unit) {
        firebaseInstance.getDb().collection("locations")
            .add(location)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailiure(e.message.toString())
            }
    }

    //Method to fetch the locations from firebase
    fun getLocations(onSuccess : (List<Location>) -> Unit, onFailiure: (String) -> Unit) {
        firebaseInstance.getDb().collection("locations")
            .get()
            .addOnSuccessListener { result ->
                val locations = mutableListOf<Location>()
                for (document in result) {
                    val location = document.toObject(Location::class.java)
                    locations.add(location)
                }
                onSuccess(locations)
            }
            .addOnFailureListener { exception ->
                onFailiure(exception.message.toString())
            }
    }
}