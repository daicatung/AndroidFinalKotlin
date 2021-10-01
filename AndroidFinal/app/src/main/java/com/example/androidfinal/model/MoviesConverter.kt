package com.example.androidfinal.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MoviesConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): MutableList<Cast> {
        val gSon = Gson()
        if (data == null) {
            return emptyList<Cast>().toMutableList()
        }
        val listType = object : TypeToken<List<Cast?>?>() {}.type
        return gSon.fromJson(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: MutableList<Cast?>?): String {
        val gSon = Gson()
        return gSon.toJson(myObjects)
    }
}