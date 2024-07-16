package com.example.comikeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class PenSettings(
    val color: Color,
    val thickness: Float,
    val intensity: Float
)

private val buttonSize = 50.dp
private val iconSize = 50.dp
private val iconPadding = 10.dp


@Composable
fun ControlPanel(onChange: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Row(
        modifier = Modifier
            .width(screenWidth.dp)
            .height(73.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {},
            enabled = false,
            modifier = Modifier
                .size(buttonSize),
        ) {}

        Spacer(
            modifier = Modifier
                .size(180.dp)
        )

        IconButton(
            onClick = onChange,
            modifier = Modifier
                .padding(iconPadding)
                .size(iconSize)
        ) {
            Image(
                painter = painterResource(R.drawable.button_pen_change),
                contentDescription = "消しゴム",
            )
        }
    }
}

@Composable
fun ControlPanel(
    penSettings: PenSettings,
    onPenSettingsChange: (PenSettings) -> Unit,
    onChange: () -> Unit
) {
    var colorPaletteOpen by remember { mutableStateOf(false) }
    var penConfigurationOpen by remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp


    Column {

        if (colorPaletteOpen) {
            ColorPalette(
                penColor = penSettings.color,
                onColorSelected = { newColor ->
                    onPenSettingsChange(
                        penSettings.copy(color = newColor)
                    )
                    colorPaletteOpen = false
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        if (penConfigurationOpen) {
            PenConfiguration(
                thickness = penSettings.thickness,
                intensity = penSettings.intensity,
                onThicknessChange = { thickness ->
                    onPenSettingsChange(penSettings.copy(thickness = thickness))
                },
                onIntensityChange = { intensity ->
                    onPenSettingsChange(penSettings.copy(intensity = intensity))
                }
            )
        }

        Row(
            modifier = Modifier
                .width(screenWidth.dp)
                .height(73.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    colorPaletteOpen = !colorPaletteOpen
                    penConfigurationOpen = false
                },
                shape = CircleShape,
                modifier = Modifier
                    .size(buttonSize),
                colors = ButtonDefaults.buttonColors(penSettings.color)
            ) {}

            Spacer(modifier = Modifier.size(120.dp))

            IconButton(
                onClick = {
                    penConfigurationOpen = !penConfigurationOpen
                    colorPaletteOpen = false
                },
                modifier = Modifier
                    .padding(iconPadding)
                    .size(buttonSize)
            ) {
                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = "",
                    modifier = Modifier
                        .size(iconSize),
                    tint = MaterialTheme.colorScheme.background
                )
            }

            IconButton(
                onClick = {
                    onChange()
                    colorPaletteOpen = false
                    penConfigurationOpen = false
                },
                modifier = Modifier
                    .padding(iconPadding)
                    .size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "",
                    modifier = Modifier
                        .size(iconSize),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}