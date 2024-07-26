package com.example.comikeapp.data.fileoperate.reserve

import android.util.Log
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class DuplicatingFile : Writing() {
    override fun access(absolutePath: Path): Boolean {
        val from: Path? = null
        return try {
            Files.copy(from, absolutePath, StandardCopyOption.REPLACE_EXISTING)
            true
        } catch (e: Exception) {
            Log.e("absolutePath", e.toString())
            false
        }
    }
}