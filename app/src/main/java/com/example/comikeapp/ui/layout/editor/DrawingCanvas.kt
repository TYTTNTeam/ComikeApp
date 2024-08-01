package com.example.comikeapp.ui.layout.editor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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

    val backgroundAspect = background.height.toFloat() / background.width

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth((penProperties.thickness + 1).pow(5f) * 0.5f)
        drawingData.updateColor(penProperties.color)
    }

    LaunchedEffect(key1 = background) {
        with(density) {
            scalableSize = if(backgroundAspect < 1){
                Offset(
                    x = (2000 * (1 / backgroundAspect)).dp.toPx(),
                    y = 2000.dp.toPx()
                )
            }else {
                Offset(
                    x = 2000.dp.toPx(),
                    y = (2000 * backgroundAspect).dp.toPx()
                )
            }
        }
    }

    ScalableInput(
        modifier = modifier
            .onSizeChanged { size ->
                val parentAspect = size.height.toFloat() / size.width

                with(density) {
                    defaultScale = if (backgroundAspect < parentAspect) {
                        size.width.toDp().value / 2000
                    } else {
                        size.height.toDp().value / 2000
                    }
                    Log.d("test", "${size.width}:${size.height}, ${size.width.toDp().value}:${size.height.toDp().value}\n") // TODO
                }
            }
            .background(Color.Green),// TODO
        drawingData = drawingData,
        scalableSize = scalableSize,
        defaultScale = defaultScale
    ) { scale, offset ->
        val scroll = rememberScrollState()
        Log.d("test", "$scale, $offset") // TODO
        Box(
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
                )
        ) {
            Image(
                bitmap = background,
                contentDescription = "背景の地図",
                modifier = Modifier
                    .fillMaxSize()
            )
            StaticCanvas(viewModel = drawingData)
        }
    }
}
