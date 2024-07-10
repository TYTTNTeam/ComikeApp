package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun PenConfiguration(
    thickness:Float,
    intensity:Float,
    onThicknessChange: (Float) -> Unit,
    onIntensityChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    Box(
        modifier = modifier
            .width(screenWidth.dp)
            .height(127.dp)
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            IconSlider(
                value = thickness,
                onValue = { onThicknessChange(it)},
                iconImage = Icons.Filled.LineWeight, iconDescription = "太さ",
                modifier = Modifier
                    .padding(top = 10.dp, start = 5.dp),
                sliderModifier = Modifier
                    .padding(start = 5.dp,end = 10.dp,top = 5.dp)
            )
            IconSlider(
                value = intensity,
                onValue = { onIntensityChange(it) },
                iconImage = Icons.Filled.Opacity, iconDescription = "濃さ",
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp),
                sliderModifier = Modifier
                    .padding(start = 5.dp,end = 10.dp,top = 0.dp)
            )
        }
    }
}