package com.example.comikeapp.data.editorrendering

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun Canvas.drawPath(
    path: Path,
    style: PathStyle
) {
    drawPath(
        path = path,
        paint = Paint().apply {
            color = style.color
            alpha = style.alpha
            strokeWidth = style.width
            strokeJoin = StrokeJoin.Round
        }
    )
}
