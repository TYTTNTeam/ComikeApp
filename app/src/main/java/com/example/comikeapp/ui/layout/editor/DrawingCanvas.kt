package com.example.comikeapp.ui.layout.editor

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import java.util.Locale

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    imagePath: String,
    penProperties: PenProperties
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val zoomState = rememberZoomState()
    val isZoomable by drawingData.isZoomable.observeAsState()

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth(penProperties.thickness * 20)
        drawingData.updateColor(penProperties.color)
    }

    isZoomable?.let {
        Box(
            modifier = modifier
                .pointerInput(Unit){
                    coroutineScope {
                        launch {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val fingerCount = event.changes.count { it.pressed }
                                    drawingData.setIsZoomable(fingerCount >= 2)
                                    Log.d("test", fingerCount.toString()) // TODO
                                }
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        Log.d("test", "到達！")
                        scale *= zoom
                        offset = Offset(offset.x + pan.x, offset.y + pan.y)
                    }
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {
            StaticCanvas(viewModel = drawingData)
        }
    }
}