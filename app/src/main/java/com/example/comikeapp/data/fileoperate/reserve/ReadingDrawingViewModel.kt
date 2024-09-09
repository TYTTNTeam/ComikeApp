package com.example.comikeapp.data.fileoperate.reserve

import android.util.Log
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel.DrawingViewModelSaveData
import java.io.IOException
import java.io.ObjectInputStream
import java.nio.file.Path

class ReadingDrawingViewModel: Reading() {
    private var result: DrawingViewModelSaveData? = null

    override fun getData(): DrawingViewModelSaveData? {
        return result
    }

    override fun access(absolutePath: Path): Boolean {
        val file = absolutePath.toFile()

        return try {
            result = ObjectInputStream(file.inputStream()).use { it.readObject() as DrawingViewModelSaveData }
            accessedFile = absolutePath
            true
        }catch (e: IOException) {
            Log.e("data.fileoperate.reserve", "ReadingDrawingViewModel: Failed accessing.\n" +
                    "An exception was thrown while reading.", e)
            false
        }catch (e: ClassCastException) {
            Log.e("data.fileoperate.reserve", "ReadingDrawingViewModel: Failed accessing.\n" +
                    "The class was not the DrawingViewModel class that was stored in the file.", e)
            false
        }
    }
}