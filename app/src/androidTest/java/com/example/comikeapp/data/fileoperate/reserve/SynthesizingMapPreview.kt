package com.example.comikeapp.data.fileoperate.reserve

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comikeapp.R
import com.example.comikeapp.data.editorrendering.PathStyle
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import com.example.comikeapp.ui.layout.editor.DrawingCanvas
import com.example.comikeapp.ui.layout.editor.PenProperties
import com.example.comikeapp.ui.layout.editor.vectorDrawableToImageBitmap
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

    val testViewmodelInstance: DrawingViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        val p = Path().apply {
            moveTo(0f, 0f)
            lineTo(5000f, 5000f)
        }

        testViewmodelInstance.addPath(Pair(p, PathStyle(color = Color.White)))

        drawing = testViewmodelInstance
        map = MapList(0, "test map name", "test/path.png")
        imageBitmap = vectorDrawableToImageBitmap(R.drawable.button_pen_change, appContext)
    }

    LaunchedEffect(key1 = imageBitmap) {
        imageBitmap?.let {
            val target = File(appContext.dataDir, "test.png")

            val s = SynthesizingMap(imageBitmap!!, drawing!!)
            s.access(target.toPath())

            val r = ReadingImage()
            r.access(target.toPath())
            readImage = r.getData()
        }
    }

    ComikeAppTheme {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            imageBitmap?.let {
                DrawingCanvas(drawingData = drawing!!, background = imageBitmap!!, penProperties = PenProperties(
                    Color.Unspecified, 1f, 1f, 0))
            }
            readImage?.let {
                Image(bitmap = readImage!!, contentDescription = "")
            }
        }
    }
}
