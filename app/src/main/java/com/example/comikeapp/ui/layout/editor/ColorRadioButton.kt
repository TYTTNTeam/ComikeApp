package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorRadioButton(
    color: Color,
    check: Boolean,
    onColorSelected: (Color) -> Unit,
) {
    val modifier = Modifier
        .padding(8.dp)
        .size(50.dp)
        .clip(RoundedCornerShape(50))
        .background(color = color)
        .clickable {
            onColorSelected(color)
        }
    if (check) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "penColor",
            tint = MaterialTheme.colorScheme.background,
            modifier = modifier
        )
    } else {
        Box(modifier = modifier)
    }
}