package com.example.comikeapp.data.fileoperate.reserve

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.nio.file.Path

class ReadingImage : Reading() {
    private var result : ImageBitmap? = null
    override fun getData(): ImageBitmap? {
        return result
    }

    override fun access(absolutePath: Path): Boolean {
        val file = File(absolutePath.toString())
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        return if(bitmap != null) {
            this.result = bitmap.asImageBitmap()
            accessedFile = absolutePath
            true
        } else {
            accessedFile = null
            Log.e("ReadingImage", "画像パス: $absolutePath")
            false
        }
    }
}