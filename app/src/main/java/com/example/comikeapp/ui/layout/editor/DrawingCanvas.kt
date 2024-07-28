package com.example.comikeapp.ui.layout.editor

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import java.util.Locale

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    drawingData: DrawingViewModel,
    imagePath: String,
    penProperties: PenProperties
) {
    LaunchedEffect(key1 = penProperties) {
        drawingData.updateAlpha(penProperties.intensity)
        drawingData.updateWidth(penProperties.thickness * 20)
        drawingData.updateColor(penProperties.color)
    }

    val zoomState = rememberZoomState()
    val isZoomable by drawingData.isZoomable.observeAsState()

    isZoomable?.let {
        Box(
            modifier = modifier
                .zoomable(zoomState = zoomState, zoomEnabled = isZoomable!!, enableOneFingerZoom = true)
        ) {
            StaticCanvas(viewModel = drawingData)
        }
    }
}