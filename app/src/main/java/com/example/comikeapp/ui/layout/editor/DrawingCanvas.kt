package com.example.comikeapp.ui.layout.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.example.comikeapp.R
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel

@Composable
fun DrawingCanvas (
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    imagePath: String,
    penProperties: PenProperties
){
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val appContext = LocalContext.current

    ScalableCanvas(modifier = modifier, drawingData = drawingData, penProperties = penProperties) {
        Image(
            painter = painterResource(id = R.drawable.button_pen_change),
            contentDescription = "test",
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow)
        )
    }
}

fun vectorDrawableToImageBitmap(@DrawableRes drawableRes: Int, context: Context): ImageBitmap {
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    val bitmap = Bitmap.createBitmap(
        100,
        100,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap.asImageBitmap()
}
