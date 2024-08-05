package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.example.comikeapp.data.editorrendering.drawPath
import com.example.comikeapp.data.editorrendering.mapMemoRendering
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel

@Composable
fun StaticCanvas(
    modifier: Modifier = Modifier,
    viewModel: DrawingViewModel,
    image: ImageBitmap,
    mode: Int
) {
    val points = remember { mutableListOf<Offset>() } // 新しく描かれた path を表示するためのPoints State
    var path by remember { mutableStateOf(Path()) } // 新しく描かれている一画State

    val pathStyle by viewModel.pathStyle.observeAsState()
    val isZoomable by viewModel.isZoomable.observeAsState()
    val canvasSizePx by viewModel.canvasSizePx.observeAsState()

    val paths by viewModel.paths.observeAsState()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(mode) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes[0]

                        points.add(change.position)

                        path = Path()
                        points.forEachIndexed { index, point ->
                            if (index == 0) {
                                path.moveTo(point.x, point.y)
                            } else {
                                path.lineTo(point.x, point.y)
                            }
                        }

                        if (change.pressed && mode == 1) {
                            viewModel.erasePath(Pair(change.previousPosition, change.position))
                        }

                        if (!change.pressed) {
                            if (!isZoomable!! && mode == 0) viewModel.addPath(
                                Pair(
                                    points.toList(),
                                    pathStyle!!.copy()
                                )
                            )
                            points.clear()
                            path = Path()
                        }
                        if (isZoomable!!) {
                            points.clear()
                            path = Path()
                        }
                    }
                }
            }
    ) {
        drawIntoCanvas { c ->
            c.apply {
                paths?.let {
                    mapMemoRendering(
                        paths = it,
                        image = image,
                        imageScale = canvasSizePx!!.x / image.width
                    )
                }

                if (mode == 0) {
                    drawPath(
                        path = path,
                        pathStyle = pathStyle!!
                    )
                }
            }
        }

    }
}