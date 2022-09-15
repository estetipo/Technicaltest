package com.carlos.satori.technical_test.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class DBTypeConverters {
    @TypeConverter
    fun listToJson(value: List<Int>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()
}