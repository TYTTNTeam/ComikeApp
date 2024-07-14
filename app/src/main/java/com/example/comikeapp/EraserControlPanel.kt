package com.example.comikeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ControlPanel(onChange: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Box(
        modifier = Modifier
            .width(screenWidth.dp)
            .height(73.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .size(50.dp),
            ) {}

            IconButton(
                onClick = {},
                enabled = false
            ) {}

            IconButton(
                onClick = {},
                enabled = false
            ) {
            }

            IconButton(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
            ) {}
            IconButton(
                onClick = { onChange() },
                modifier = Modifier
                    .padding(10.dp)
                    .size(60.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.button_pen_change),
                    contentDescription = "消しゴム",
                )
            }
        }
    }
}