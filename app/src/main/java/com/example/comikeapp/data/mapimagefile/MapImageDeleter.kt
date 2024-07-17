package com.example.comikeapp.data.mapimagefile

import android.content.Context
import java.io.File

class MapImageDeleter(context: Context) : MapImageOperateable(context) {
    fun deleteImageFile(imagePath: String) {
        val file = File(imagePath)

        if (file.exists()) {
            file.delete()
        }
    }
}