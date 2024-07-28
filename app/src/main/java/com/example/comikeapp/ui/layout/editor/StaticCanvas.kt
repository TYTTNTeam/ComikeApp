package com.example.comikeapp.ui.layout.editor

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import com.example.comikeapp.data.viewmodel.editor.PathStyle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun StaticCanvas(viewModel: DrawingViewModel) {
    var point by remember { mutableStateOf(Offset.Zero) } // point位置追跡のためのState
    val points = remember { mutableListOf<Offset>() } // 新しく描かれた path を表示するためのPoints State
    var path by remember { mutableStateOf(Path()) } // 新しく描かれている一画State

    val pathStyle by viewModel.pathStyle.observeAsState()
    val isZoomable by viewModel.isZoomable.observeAsState()

    val paths by viewModel.paths.observeAsState()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        point = offset
                        points.add(point)
                    },
                    onDrag = { _, dragAmount ->
                        if(!isZoomable!!){
                            point += dragAmount
                            points.add(point)

                            path = Path()
                            points.forEachIndexed { index, point ->
                                if (index == 0) {
                                    path.moveTo(point.x, point.y)
                                } else {
                                    path.lineTo(point.x, point.y)
                                }
                            }
                        }else{
                            points.clear()
                            path = Path()
                        }
                    },
                    onDragEnd = {
                        if(!isZoomable!!){
                            viewModel.addPath(Pair(path, pathStyle!!.copy()))
                        }

                        points.clear()
                        path = Path()
                    }
                )
            },
        ) {
        paths?.forEach { pair ->
            drawPath(
                path = pair.first,
                style = pair.second
            )
        }

        drawPath(
            path = path,
            style = pathStyle!!
        )

    }
}

internal fun DrawScope.drawPath(
    path: Path,
    style: PathStyle
) {
    drawPath(
        path = path,
        color = style.color,
        alpha = style.alpha,
        style = Stroke(width = style.width)
    )
}