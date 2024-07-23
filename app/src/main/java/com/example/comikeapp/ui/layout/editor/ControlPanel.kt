package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comikeapp.R

data class PenProperties(
    val color: Color,
    val thickness: Float,
    val intensity: Float
)

private val itemModifier = Modifier.size(50.dp)
private val rowHeight = 73.dp
private val rowSpace = Arrangement.SpaceEvenly
private val rowAlignment = Alignment.CenterVertically

@Composable
fun ControlPanel(onChange: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .background(MaterialTheme.colorScheme.secondary),
        horizontalArrangement = rowSpace,
        verticalAlignment = rowAlignment
    ) {

        Spacer(
            modifier = itemModifier
        )

        Spacer(
            modifier = Modifier
                .size(120.dp)
        )

        Spacer(
            modifier = itemModifier
        )

        IconButton(
            onClick = onChange,
            modifier = itemModifier,
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
    penSettings: PenProperties,
    onPenSettingsChange: ( PenProperties ) -> Unit,
    onChange: () -> Unit
) {
    var colorPaletteOpen by remember { mutableStateOf(false) }
    var penConfigurationOpen by remember { mutableStateOf(false) }

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
                .fillMaxWidth()
                .height(rowHeight)
                .background(MaterialTheme.colorScheme.secondary),
            horizontalArrangement = rowSpace,
            verticalAlignment = rowAlignment
        ) {

            Button(
                onClick = {
                    colorPaletteOpen = !colorPaletteOpen
                    penConfigurationOpen = false
                },
                shape = CircleShape,
                modifier = itemModifier,
                colors = ButtonDefaults.buttonColors(penSettings.color)
            ) {}

            Spacer(modifier = Modifier.size(120.dp))

            IconButton(
                onClick = {
                    penConfigurationOpen = !penConfigurationOpen
                    colorPaletteOpen = false
                },
                modifier = itemModifier
            ) {
                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = "ペンの設定",
                    modifier = itemModifier,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            IconButton(
                onClick = {
                    onChange()
                    colorPaletteOpen = false
                    penConfigurationOpen = false
                },
                modifier = itemModifier
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "ペン",
                    modifier = itemModifier,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}