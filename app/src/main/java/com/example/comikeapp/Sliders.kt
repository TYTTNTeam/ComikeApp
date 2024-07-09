package com.example.comikeapp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable fun IconSlider(
    modifier: Modifier = Modifier,
    sliderModifier : Modifier = Modifier,
    value: Float,
    onValue: (Float) -> Unit,
    iconImage: ImageVector,
    iconDescription: String
) {
    var sliderValue by remember { mutableFloatStateOf(value)}
    Row {
        Icon(
            imageVector = iconImage,
            contentDescription = iconDescription,
            modifier = modifier
                .size(40.dp),
            tint = MaterialTheme.colorScheme.background
        )
        Slider(
            value = value,
            onValueChange = {
                sliderValue = it
                onValue(it)
            },
            modifier = sliderModifier
                .weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.background,
                activeTrackColor = MaterialTheme.colorScheme.background,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
            )
        )
    }
}