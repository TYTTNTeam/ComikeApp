package com.example.comikeapp.data.fileoperate.reserve

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.comikeapp.data.editorrendering.mapMemoRendering
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.file.Path

class SynthesizingMap(
    private val image: ImageBitmap,
    private val drawing: DrawingViewModel
) : Writing() {
    override fun access(absolutePath: Path): Boolean {
        val width = drawing.canvasSizePx.value!!.x.toInt()
        val height =  drawing.canvasSizePx.value!!.y.toInt()

        // Bitmapを作成し、Canvasに描画
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(bitmap.asImageBitmap()).mapMemoRendering(
            drawing.paths.value!!.toList(),
            image,
            drawing.canvasSizePx.value!!.x / image.width
        )

        val file = absolutePath.toFile()

        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            accessedFile = absolutePath
            true
        } catch (e: FileNotFoundException) {
            Log.e(
                "data.fileoperate.reserve",
                "SynthesizingMap: Failed accessing.\n" +
                        "File does not exist.",
                e
            )
            false
        }
    }
}