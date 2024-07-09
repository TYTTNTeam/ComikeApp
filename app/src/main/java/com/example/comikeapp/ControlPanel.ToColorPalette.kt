package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ControlPanel() {
    var penColor by remember { mutableStateOf(Color.Green) }
    var colorPaletteOpen by remember { mutableStateOf(false) }
    Column {
        if (colorPaletteOpen) {
            ColorPalette(penColor = penColor,
                onColorSelected = { color ->
                    penColor = color
                    colorPaletteOpen = false
                }
            )
        }

        Button(
            onClick = { colorPaletteOpen = !colorPaletteOpen },
            modifier = Modifier.background(penColor)
        ) { }
    }

}