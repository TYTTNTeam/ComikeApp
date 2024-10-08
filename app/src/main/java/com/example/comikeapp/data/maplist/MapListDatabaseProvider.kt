package com.example.comikeapp.data.maplist

import android.content.Context
import androidx.room.Room

object MapListDatabaseProvider {
    private var instance: MapListDatabase? = null
    fun getDatabase(context: Context): MapListDatabase {
        return instance ?: synchronized(this) {
            val newInstance = Room.databaseBuilder(
                context.applicationContext, MapListDatabase::class.java, "app-database"
            ).build()
            instance = newInstance
            newInstance
        }
    }
}