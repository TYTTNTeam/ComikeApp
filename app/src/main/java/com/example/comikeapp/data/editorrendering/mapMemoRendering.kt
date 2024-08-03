package com.example.comikeapp.data.editorrendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale

fun Canvas.mapMemoRendering(
    paths: List<Pair<Path, PathStyle>>,
    image: ImageBitmap,
    imageScale: Float
) {
    scale(imageScale, imageScale)

    drawImage(image = image, topLeftOffset = Offset.Zero, paint = Paint())

    paths.forEach { pair ->
        drawPath(
            path = pair.first,
            style = pair.second
        )
    }
}
