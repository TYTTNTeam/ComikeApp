package com.example.comikeapp.data.fileoperate.reserve

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
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
        val canvasSizePx = drawing.canvasSizePx.value!!

        // Bitmapを作成し、Canvasに描画
        val bitmap = Bitmap.createBitmap(canvasSizePx.x.toInt(), canvasSizePx.y.toInt(), Bitmap.Config.ARGB_8888)
        Canvas(bitmap.asImageBitmap()).mapMemoRendering(
            drawing.paths.value!!.toList(),
            image,
            canvasSizePx.x / image.width
        )

        val scale: Float

        // 長編が一定サイズを超えないようにしている。
        val maxSizePx = 2400
        val aspect = canvasSizePx.y / canvasSizePx.x
        scale = if(aspect < 1) {
            maxSizePx / canvasSizePx.x
        }else{
            maxSizePx / canvasSizePx.y
        }

        val scaled = bitmap.scale(
            (canvasSizePx.x * scale).toInt(),
            (canvasSizePx.y * scale).toInt()
        )

        val file = absolutePath.toFile()

        try {
            FileOutputStream(file).use { out ->
                scaled.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            accessedFile = absolutePath
            return true
        } catch (e: FileNotFoundException) {
            Log.e(
                "data.fileoperate.reserve",
                "SynthesizingMap: Failed accessing.\n" +
                        "Cannot access file.",
                e
            )
            return false
        }
    }
}