package com.carlos.satori.technical_test.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.carlos.satori.technical_test.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*


@Module
@InstallIn(SingletonComponent::class)
class LocationService:Service() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    //Service declare as singleton to avoid overlaping
    companion object {
        var isRunning = false
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startLocation(this)
        mHandler = Handler()
        mRunnable = Runnable { startLocation(this) }
        mHandler.postDelayed(mRunnable, 5000)
        //Notification channel if using android  or higher
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_movie)
            .setContentTitle("Servicio de ubicación")
            .setContentText("Este es un servicio dde ubicación")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
        isRunning = true
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        mHandler.removeCallbacks(mRunnable)
        isRunning = false
    }

    @SuppressLint("MissingPermission")
    fun startLocation(context: Context) {
        if (checkPermissionLocation() && isLocationEnabled(context)) {

            lateinit var locationCallbackVar: LocationCallback
            var currentLocation: Location? = null


            var locationRequestVar: LocationRequest = LocationRequest.create().apply {
                interval = 30000
                fastestInterval = 30000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            //We use a brodcaster to send the info
            locationCallbackVar = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    Intent().also { intent ->
                        intent.setAction("android.intent.action.NEW_LOCATION")
                        intent.putExtra("latitude", p0.lastLocation?.latitude.toString())
                        intent.putExtra("longitude", p0.lastLocation?.longitude.toString())
                        sendBroadcast(intent)
                    }
                }
            }


            var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.requestLocationUpdates(
                locationRequestVar,
                locationCallbackVar,
                Looper.getMainLooper()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun checkPermissionLocation(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}