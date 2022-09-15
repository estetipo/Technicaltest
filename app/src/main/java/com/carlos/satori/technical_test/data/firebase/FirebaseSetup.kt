package com.carlos.satori.technical_test.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class FirebaseSetup {

    private var db = Firebase.firestore

    fun initInstance() {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }
        db.firestoreSettings = settings
        db = FirebaseFirestore.getInstance()
    }
    fun getDb(): FirebaseFirestore {
        return db
    }
}