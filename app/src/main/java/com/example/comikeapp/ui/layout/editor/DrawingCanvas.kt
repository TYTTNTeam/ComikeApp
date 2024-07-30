package com.example.comikeapp.ui.layout.editor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel

@Composable
fun DrawingCanvas (
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    background: ImageBitmap,
    penProperties: PenProperties
){
    var scalableSize by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth(penProperties.thickness * 20)
        drawingData.updateColor(penProperties.color)
    }

    ScalableCanvas(
        modifier = modifier
            .onSizeChanged { size ->
                scalableSize = Offset(
                    x = size.width.toFloat(),
                    y = size.height.toFloat()
                )
                Log.d("test", "onSizeChanged: ${size.width}, ${size.height}\n$scalableSize")
            },
        drawingData = drawingData,
        scalableSize = scalableSize
    ) { scale, offset ->
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {
            Image(
                bitmap = background,
                contentDescription = "背景の地図",
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow) // TODO
            )
            StaticCanvas(viewModel = drawingData)
        }
    }
}
