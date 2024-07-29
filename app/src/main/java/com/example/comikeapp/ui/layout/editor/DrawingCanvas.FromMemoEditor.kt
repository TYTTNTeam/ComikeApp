package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawingCanvas(
    modifier: Modifier,
    imagePath: String,
    drawingData: DrawingViewModel,
    penProperties: PenProperties
) {
    Box(modifier = modifier.background(Color.Yellow)) {
        Text(text = "$imagePath : $drawingData : $penProperties")
    }
}