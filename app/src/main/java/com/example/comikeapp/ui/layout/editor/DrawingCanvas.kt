package com.example.comikeapp.ui.layout.editor

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comikeapp.R
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

    val isZoomable by drawingData.isZoomable.observeAsState()

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth(penProperties.thickness * 20)
        drawingData.updateColor(penProperties.color)

        drawingData.setIsZoomable(true)
    }

    isZoomable?.let {
        Box(
            modifier = modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .pointerInput(Unit){
                    coroutineScope {
                        launch {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val changes = event.changes
                                    val pinchMode = changes.size >= 2

                                    drawingData.setIsZoomable(pinchMode)

                                    // ピンチ操作を検出
                                    if (pinchMode) {
                                        val change1 = changes[0]
                                        val change2 = changes[1]

                                        if (change1.pressed && change2.pressed) {
                                            val distanceStart = (change1.previousPosition - change2.previousPosition).getDistance()
                                            val distanceEnd = (change1.position - change2.position).getDistance()

                                            val newScale = scale * (distanceEnd / distanceStart)
                                            scale = newScale.coerceIn(0.5f, 50f) // スケールの最小・最大値を設定

                                            val midpoint = (change1.position + change2.position) / 2f
                                            offset += midpoint - (change1.previousPosition + change2.previousPosition) / 2f
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_pen_change),
                contentDescription = "test",
                modifier = Modifier.fillMaxSize().background(Color.Yellow)
            )
            StaticCanvas(viewModel = drawingData)
        }
    }
}