package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun ControlPanel(modifier: Modifier = Modifier) {
    var penColor by remember { mutableStateOf(Color.Green) }
    var colorPaletteOpen by remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Box(
        modifier = Modifier
            .width(screenWidth.dp)
            .height(73.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
    ){
        Row {
            Button(
                onClick = { colorPaletteOpen = !colorPaletteOpen },
                modifier = Modifier.background(penColor)
            ) { }
        }
    }

        if (colorPaletteOpen) {
            ColorPalette(penColor = penColor,
                onColorSelected = { color ->
                    penColor = color
                    colorPaletteOpen = false
                }
            )
        }


}