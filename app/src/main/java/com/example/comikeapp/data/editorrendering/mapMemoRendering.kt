package com.example.comikeapp.data.editorrendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale

fun DrawScope.mapMemoRendering(
    paths: List<Pair<Path, PathStyle>>,
    image: ImageBitmap,
    imageScale: Float
) {
    scale(imageScale, Offset.Zero) {
        drawImage(image = image)
    }
    paths.forEach { pair ->
        drawPath(
            path = pair.first,
            style = pair.second
        )
    }
}
