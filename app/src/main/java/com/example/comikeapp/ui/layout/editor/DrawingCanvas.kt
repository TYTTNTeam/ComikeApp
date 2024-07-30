package com.example.comikeapp.ui.layout.editor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    background: ImageBitmap,
    penProperties: PenProperties
) {
    var scalableSize by remember { mutableStateOf(Offset.Zero) }

    val scroll = rememberScrollState()

    val density = LocalDensity.current

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth(penProperties.thickness * 20)
        drawingData.updateColor(penProperties.color)
    }

    ScalableCanvas(
        modifier = modifier
            .onSizeChanged { size ->
                scalableSize = Offset(
                    x = size.width * 1f,
                    y = size.width * 1f * (background.height.toFloat() / background.width)
                )
                Log.d("test", "onSizeChanged: ${size.width}, ${size.height}\n$scalableSize") // TODO
            }
            .offset(),
        drawingData = drawingData,
        scalableSize = scalableSize
    ) { scale, offset ->
        Box(
            modifier = Modifier
                .width(with(density) { scalableSize.x.toDp() })
                .height(with(density) { scalableSize.y.toDp() })
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
