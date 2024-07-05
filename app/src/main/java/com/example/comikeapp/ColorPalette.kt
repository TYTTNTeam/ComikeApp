package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPalette(penColor: Color ,onColorSelected: (Color) -> Unit,onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .width(73.dp)
            .height(265.dp)
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ColorRadioButton(
                color = Color.Green,
                check = penColor == Color.Green,
                onColorSelected = { color ->
                    onColorSelected(color)
                    onClose()
                }
            )
            ColorRadioButton(
                color = Color.Magenta,
                check = penColor == Color.Magenta,
                onColorSelected = { color ->
                    onColorSelected(color)
                    onClose()
                }
            )
            ColorRadioButton(
                color = Color.Blue,
                check = penColor == Color.Blue,
                onColorSelected = { color ->
                    onColorSelected(color)
                    onClose()
                }
            )
            ColorRadioButton(
                color = Color.Red,
                check = penColor == Color.Red,
                onColorSelected = { color ->
                    onColorSelected(color)
                    onClose()
                }
            )
        }
    }
}