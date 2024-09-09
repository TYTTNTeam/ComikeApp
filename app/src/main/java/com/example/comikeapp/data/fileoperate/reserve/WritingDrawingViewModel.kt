package com.example.comikeapp.data.fileoperate.reserve

import android.util.Log
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import java.io.IOException
import java.io.ObjectOutputStream
import java.nio.file.Path

class WritingDrawingViewModel(
    private val saveData: DrawingViewModel.DrawingViewModelSaveData
): Writing() {
    override fun access(absolutePath: Path): Boolean {
        val file = absolutePath.toFile()
        return try {
            ObjectOutputStream(file.outputStream()).use { it.writeObject(saveData) }
            accessedFile = absolutePath
            true
        }catch (e: IOException){
            Log.e("data.fileoperate.reserve", "WritingDrawingViewModel: Failed accessing.\n" +
                    "An exception was thrown while writing.", e)
            false
        }
    }
}