package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
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
    Row {
        Icon(
            imageVector = iconImage,
            contentDescription = iconDescription,
            modifier = modifier
                .size(40.dp),
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Slider(
            value = value,
            onValueChange = {
                onValue(it)
            },
            modifier = sliderModifier
                .weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onSecondary,
                activeTrackColor = MaterialTheme.colorScheme.onSecondary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.35f)
            )
        )
    }
}