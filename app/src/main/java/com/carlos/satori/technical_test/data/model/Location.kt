package com.carlos.satori.technical_test.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.LatLng

@Entity(tableName = "location")
data class Location (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val lat: Double? = null,
    val lng: Double? = null,
    val date: String? = null
)