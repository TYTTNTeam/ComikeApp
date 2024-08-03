package com.example.comikeapp.ui.layout.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comikeapp.R
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import kotlinx.coroutines.delay


@Composable
fun DataSynchronizer(
    mapId: Int,
    saving: Boolean,
    onSavingChange: (Boolean) -> Unit,
    content: @Composable (DrawingViewModel?, MapList?, ImageBitmap?) -> Unit
) {
    var drawing by remember { mutableStateOf<DrawingViewModel?>(null) }
    var map by remember { mutableStateOf<MapList?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val testViewmodelInstance: DrawingViewModel = viewModel()

    val appContext = LocalContext.current

    LaunchedEffect(key1 = saving) {
        if(saving){
            drawing = testViewmodelInstance
            map = MapList(0, "test map name", "test/path.png")
            imageBitmap = vectorDrawableToImageBitmap(R.drawable.button_pen_change, appContext)
            onSavingChange(false)
        }
    }

    content(drawing, map, imageBitmap)
    // TODO 未実装
}

internal fun vectorDrawableToImageBitmap(@DrawableRes drawableRes: Int, context: Context): ImageBitmap {
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    val bitmap = Bitmap.createBitmap(
        1000,
        1300,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        color = Color.YELLOW
    }
    canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap.asImageBitmap()
}
