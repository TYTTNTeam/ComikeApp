package com.example.comikeapp

import android.content.Context
import android.util.Log
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class MapImageCleaner(context: Context) : MapImageOperateable(context) {
    fun clean(paths: List<String>): Boolean{
        return try {
            val foundFiles = Files.list(this.mapImagesDirectory.toPath())
                .filter { Files.isRegularFile(it) }
                .map(Path::toFile)
                .collect(Collectors.toList())
            foundFiles.forEach { found ->
                if(!paths.contains(found.path.toString())) {
                    found.delete()
                }
            }
            true
        }catch (e: Exception){
            Log.e("MapImageCleaner", "Error: Failed clean up image file of user map: ", e)
            false
        }
    }
}