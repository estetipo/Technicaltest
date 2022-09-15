package com.carlos.satori.technical_test

import android.app.Application
import android.content.Intent
import com.carlos.satori.technical_test.data.firebase.FirebaseSetup
import com.carlos.satori.technical_test.service.LocationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App :Application(){
    //Initialize app as hiltandroidapp to set up dagger
    companion object {
        lateinit var firebaseInstance: FirebaseSetup
    }

    // Initialize firebase connection
    override fun onCreate() {
        super.onCreate()
        firebaseInstance = FirebaseSetup()
        firebaseInstance.initInstance()
    }

}