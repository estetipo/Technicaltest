package com.carlos.satori.technical_test.ui.fragments.maps

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlos.satori.technical_test.data.firebase.FirebaseDatabase
import com.carlos.satori.technical_test.data.firebase.FirebaseStorage
import com.carlos.satori.technical_test.data.model.Location
import com.carlos.satori.technical_test.data.repository.room.LocationRepository
import com.google.type.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.ln

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase()
    private val _listLocations: MutableLiveData<List<Location>?> = MutableLiveData()

    var currentLat:Double = 0.0
    var currentLng:Double = 0.0
    val listLocations = _listLocations

    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")

    fun getLocations() {
        firebaseDatabase.getLocations(
            onFailiure = {
                viewModelScope.launch {
                    locationRepository.get().collect() {
                        _listLocations.value = it
                    }
                }
            },
            onSuccess = {
                _listLocations.value = it.toMutableList()
                viewModelScope.launch {
                    locationRepository.deleteAll()
                    it.forEach { info->
                        locationRepository.upsert(info)
                    }
                }
            }
        )
    }

    fun addLocation(lat: Double, lng: Double, context: Context) {
        val location = Location(lat = lat, lng = lng, date = sdf.format(Date()))
        firebaseDatabase.addLocation(location,
            onFailiure = {
                Toast.makeText(context,"Ocurrió un error",Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                _listLocations.value = _listLocations.value?.plus(location)
            })
    }
}