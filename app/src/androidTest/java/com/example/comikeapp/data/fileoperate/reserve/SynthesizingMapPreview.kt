package com.example.comikeapp.data.fileoperate.reserve

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comikeapp.R
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import com.example.comikeapp.ui.layout.editor.DrawingCanvas
import com.example.comikeapp.ui.layout.editor.PenProperties
import com.example.comikeapp.ui.theme.ComikeAppTheme
import java.io.File

@Preview(widthDp = 350, heightDp = 800)
@Composable
fun SynthesizingMapPreview() {
    val appContext = LocalContext.current

    var drawing by remember { mutableStateOf<DrawingViewModel?>(null) }
    var map by remember { mutableStateOf<MapList?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var readImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var saving by remember { mutableStateOf(false) }

    val testViewmodelInstance: DrawingViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        testViewmodelInstance.setCanvasSizePx(Offset(5000f, 5000f))

        drawing = testViewmodelInstance
        map = MapList(0, "test map name", "test/path.png")
        imageBitmap = vectorDrawableToImageBitmap(R.drawable.button_pen_change, appContext)
    }

    LaunchedEffect(key1 = saving) {
        if (saving) {
            val target = File(appContext.dataDir, "test.png")

            val s = SynthesizingMap(imageBitmap!!, drawing!!)
            s.access(target.toPath())

            val r = ReadingImage()
            r.access(target.toPath())
            readImage = r.getData()

            saving = false
        }
    }

    ComikeAppTheme {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            imageBitmap?.let {
                DrawingCanvas(
                    modifier = Modifier.height(400.dp),
                    drawingData = drawing!!,
                    background = imageBitmap!!,
                    penProperties = PenProperties(Color.Unspecified, 2f, 2f, 0)
                )
                Image(
                    modifier = Modifier
                        .height(400.dp)
                        .clickable { saving = true },
                    bitmap = if(readImage != null) readImage!! else imageBitmap!!,
                    contentDescription = ""
                )
            }
        }
    }
}

private fun vectorDrawableToImageBitmap(@DrawableRes drawableRes: Int, context: Context): ImageBitmap {
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    val bitmap = Bitmap.createBitmap(
        1000,
        1300,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        color = android.graphics.Color.YELLOW
    }
    canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap.asImageBitmap()
}
