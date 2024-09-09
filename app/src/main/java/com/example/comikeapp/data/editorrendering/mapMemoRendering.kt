package com.example.comikeapp.data.editorrendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path

fun Canvas.mapMemoRendering(
    paths: List<Pair<Path, PathStyle>>,
    image: ImageBitmap,
    imageScale: Float
) {
    scale(imageScale, imageScale)
    drawImage(image = image, topLeftOffset = Offset.Zero, paint = Paint())

    val reciprocal = 1 / imageScale
    scale(reciprocal, reciprocal)
    paths.forEach { pair ->
        drawPath(
            path = pair.first,
            pathStyle = pair.second
        )
    }
}
