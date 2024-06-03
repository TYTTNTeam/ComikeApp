package com.example.comikeapp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


@Composable
fun RotatableMap() {
    val painter = painterResource(id = R.drawable.button_add_map)
    val zoomState = rememberZoomState(contentSize = painter.intrinsicSize)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.zoomable(zoomState)
    )
}
