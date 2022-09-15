package com.carlos.satori.technical_test.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Images(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val url: String? = null
)