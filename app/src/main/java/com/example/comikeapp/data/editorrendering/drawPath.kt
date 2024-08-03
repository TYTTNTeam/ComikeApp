package com.example.comikeapp.data.editorrendering

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin

fun Canvas.drawPath(
    path: Path,
    pathStyle: PathStyle
) {
    drawPath(
        path = path,
        paint = Paint().apply {
            color = pathStyle.color
            alpha = pathStyle.alpha
            strokeWidth = pathStyle.width
            style = PaintingStyle.Stroke
            strokeJoin = StrokeJoin.Round
        }
    )
}
