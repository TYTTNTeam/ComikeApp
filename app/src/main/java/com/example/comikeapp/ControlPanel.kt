package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comikeapp.ui.theme.ComikeAppTheme

@Composable
fun ControlPanel(modifier: Modifier = Modifier) {
    var penColor by remember { mutableStateOf(Color.Green) }
    var colorPaletteOpen by remember { mutableStateOf(false) }
    var penConfigurationOpen by remember { mutableStateOf(false) }
    var eraserFunction by remember { mutableStateOf(true) }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    var thickness by remember { mutableFloatStateOf(0.5f) }
    var intensity by remember { mutableFloatStateOf(1f) }


    Column {

        if (colorPaletteOpen) {
            ColorPalette(
                penColor = penColor,
                onColorSelected = { color ->
                    penColor = color
                    colorPaletteOpen = false
                },
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
        }

        if (penConfigurationOpen) {

            PenConfiguration(
                thickness = thickness,
                intensity = intensity,
                onThicknessChange = { thickness = it },
                onIntensityChange = { intensity = it }
            )
        }

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

                Button(
                    onClick = {
                        colorPaletteOpen = !colorPaletteOpen
                        penConfigurationOpen = false
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .size(50.dp),
                    colors = ButtonDefaults.buttonColors(penColor)
                ) {}

                IconButton(
                    onClick = { /* ToDo */ }
                ) {}

                IconButton(
                    onClick = { /* ToDo */ }
                ) {
                }

                IconButton(
                    onClick = {
                        penConfigurationOpen = !penConfigurationOpen
                        colorPaletteOpen = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tune,//Discover Tune
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, heightDp = 600, widthDp = 320)
@Composable
fun penPreview() {
    ComikeAppTheme(darkTheme = false) {
        Box(contentAlignment = Alignment.BottomCenter) {
            ControlPanel()
        }
    }
}