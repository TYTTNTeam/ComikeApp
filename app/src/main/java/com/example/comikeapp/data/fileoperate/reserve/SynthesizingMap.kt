package com.example.comikeapp.data.fileoperate.reserve

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.ComposeView
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import com.example.comikeapp.ui.layout.editor.DrawingCanvas
import com.example.comikeapp.ui.layout.editor.PenProperties
import com.example.comikeapp.ui.theme.ComikeAppTheme
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.file.Path

class SynthesizingMap(
    private val image: ImageBitmap,
    private val drawing: DrawingViewModel,
    private val context: Context
): Writing() {
    override fun access(absolutePath: Path): Boolean {
        val width = image.width
        val height = image.height

        // ComposeViewを作成
        val composeView = ComposeView(context).apply {
            setContent {
                ComikeAppTheme {
                    DrawingCanvas(
                        modifier = Modifier.fillMaxSize(),
                        drawingData = drawing,
                        background = image,
                        penProperties = PenProperties(Color.Unspecified, 1f, 1f, 0)
                    )
                }
            }
        }

        // ComposeViewのmeasure及びレイアウトをする
        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)

        // Bitmapを作成し、Canvasに描画
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        composeView.draw(canvas)

        val file = absolutePath.toFile()

        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            accessedFile = absolutePath
            true
        }catch (e: FileNotFoundException) {
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