package com.example.comikeapp.ui.layout.editor

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.comikeapp.R
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel

@Composable
fun DrawingCanvas (
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    background: ImageBitmap,
    penProperties: PenProperties
){
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth(penProperties.thickness * 20)
        drawingData.updateColor(penProperties.color)
    }

    ScalableCanvas(modifier = modifier, drawingData = drawingData) { scale, offset ->
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
                contentDescription = "test",
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)
            )
            StaticCanvas(viewModel = drawingData)
        }
    }
}
