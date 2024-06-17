package com.example.comikeapp

import android.content.Context
import androidx.room.Room

object MapListDatabaseProvider {
    private var instance: MapListDatabase? = null
    fun getDatabase(context: Context): MapListDatabase {
        return instance ?: synchronized(this) {
            val newInstance = Room.databaseBuilder(
                context.applicationContext, MapListDatabase::class.java, "app-database"
            ).fallbackToDestructiveMigration().build()// TODO バージョンアップ時にデータベースがリセットされる可能性があります。要修正
            instance = newInstance
            newInstance
        }
    }
}