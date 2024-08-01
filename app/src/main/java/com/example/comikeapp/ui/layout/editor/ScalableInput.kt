package com.example.comikeapp.ui.layout.editor

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel

@Composable
fun ScalableInput(
    modifier: Modifier,
    drawingData: DrawingViewModel,
    scalableSize: Offset,// dpではなく、pixelで指定
    defaultScale: Float,
    scalable: @Composable() (BoxScope.(scale: Float, offset: Offset) -> Unit)
) {
    var scale by remember { mutableFloatStateOf(defaultScale) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(key1 = defaultScale) {
        scale = defaultScale
        offset = Offset(
            x = offset.x - (scalableSize.x * (1 - defaultScale)) / 2,
            y = offset.y - (scalableSize.y * (1 - defaultScale)) / 2
        )
        Log.d("test", "on default scale change: $defaultScale") // TODO
    }

    Box(
        modifier = modifier
            .pointerInput(scalableSize, defaultScale) {
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
                                val distanceStart =
                                    (change1.previousPosition - change2.previousPosition).getDistance()
                                val distanceEnd =
                                    (change1.position - change2.position).getDistance()

                                val prev = scale

                                val newScale = scale * (distanceEnd / distanceStart)
                                scale =
                                    newScale.coerceIn(defaultScale * 0.5f, defaultScale * 50f) // スケールの最小・最大値を設定

                                val relativeScale = scale / prev

                                val midpoint =
                                    (change1.position + change2.position) / 2f

                                val movedAbsolute =
                                    offset + (midpoint - (change1.previousPosition + change2.previousPosition) / 2f)

                                val movedCenterAbsolute = movedAbsolute + scalableSize / 2f
                                val midToMovedCenter = (movedCenterAbsolute - midpoint)

                                offset = movedAbsolute + midToMovedCenter * (relativeScale - 1)
                            }
                        }
                    }
                }
            }
    ) {
        scalable(scale, offset)
    }
}
