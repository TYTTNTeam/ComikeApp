package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import kotlin.math.pow

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    background: ImageBitmap,
    penProperties: PenProperties
) {
    var scalableSize by remember { mutableStateOf(Offset.Zero) }
    var defaultScale by remember { mutableFloatStateOf(1f)}

    val density = LocalDensity.current

    val minScalableSizePx = 5000f
    val backgroundAspect = background.height.toFloat() / background.width

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth((penProperties.thickness + 1).pow(5f) * minScalableSizePx * 0.0002f)
        drawingData.updateColor(penProperties.color)
    }

    LaunchedEffect(key1 = background) {
        scalableSize = if(backgroundAspect < 1){
            Offset(
                x = minScalableSizePx * (1 / backgroundAspect),
                y = minScalableSizePx
            )
        }else {
            Offset(
                x = minScalableSizePx,
                y = minScalableSizePx * backgroundAspect
            )
        }
    }

    ScalableInput(
        modifier = modifier
            .onSizeChanged { size ->
                val parentAspect = size.height.toFloat() / size.width

                defaultScale = if (backgroundAspect < parentAspect) {
                    size.width / minScalableSizePx
                } else {
                    size.height / minScalableSizePx
                }
            },
        drawingData = drawingData,
        scalableSize = scalableSize,
        defaultScale = defaultScale
    ) { scale, offset ->
        val scroll = rememberScrollState()
        StaticCanvas(
            modifier = Modifier
                .horizontalScroll(scroll, false)
                .verticalScroll(scroll, false)
                .width(with(density) { scalableSize.x.toDp() })
                .height(with(density) { scalableSize.y.toDp() })
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                ),
            viewModel = drawingData,
            image = background
        )
    }
}
