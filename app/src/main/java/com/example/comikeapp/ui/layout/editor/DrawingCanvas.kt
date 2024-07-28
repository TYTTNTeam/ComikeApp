package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import com.example.comikeapp.R
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    imagePath: String,
    penProperties: PenProperties
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var toCenter by remember { mutableStateOf(Offset.Zero) }

    val isZoomable by drawingData.isZoomable.observeAsState()

    fun pinchOperation(change1: PointerInputChange, change2: PointerInputChange) {
        val distanceStart =
            (change1.previousPosition - change2.previousPosition).getDistance()
        val distanceEnd =
            (change1.position - change2.position).getDistance()

        val prev = scale

        val newScale = scale * (distanceEnd / distanceStart)
        scale =
            newScale.coerceIn(0.5f, 50f) // スケールの最小・最大値を設定

        val relativeScale = scale / prev

        val midpoint =
            (change1.position + change2.position) / 2f

        val movedAbsolute =
            offset + (midpoint - (change1.previousPosition + change2.previousPosition) / 2f)

        val movedCenterAbsolute = movedAbsolute + toCenter
        val midToMovedCenter = (movedCenterAbsolute - midpoint)

        offset = movedAbsolute + midToMovedCenter * (relativeScale - 1)
    }

    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth(penProperties.thickness * 20)
        drawingData.updateColor(penProperties.color)
    }

    isZoomable?.let {
        Box(
            modifier = modifier
                .onSizeChanged { size ->
                    toCenter = Offset(
                        x = size.width / 2f,
                        y = size.height / 2f
                    )
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val changes = event.changes

                            // ピンチ操作を検出
                            val pinchMode = changes.size >= 2

                            drawingData.setIsZoomable(pinchMode)

                            if (pinchMode) {
                                val change1 = changes[0]
                                val change2 = changes[1]

                                if (change1.pressed && change2.pressed) {
                                    pinchOperation(change1, change2)
                                }
                            }
                        }
                    }
                }
        ) {
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
                    painter = painterResource(id = R.drawable.button_pen_change),
                    contentDescription = "test",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow)
                )
                StaticCanvas(viewModel = drawingData)
            }
        }
    }
}