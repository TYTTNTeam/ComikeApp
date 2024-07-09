package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    penColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.5f))
            .width(265.dp)
            .height(73.dp)

    ) {
        ColorRadioButton(color = Color.Green,
            check = penColor == Color.Green,
            onColorSelected = { color ->
                onColorSelected(color)
            }
        )
        ColorRadioButton(color = Color.Magenta,
            check = penColor == Color.Magenta,
            onColorSelected = { color ->
                onColorSelected(color)
            }
        )
        ColorRadioButton(color = Color.Blue,
            check = penColor == Color.Blue,
            onColorSelected = { color ->
                onColorSelected(color)
            }
        )
        ColorRadioButton(color = Color.Red,
            check = penColor == Color.Red,
            onColorSelected = { color ->
                onColorSelected(color)
            }
        )
    }
}