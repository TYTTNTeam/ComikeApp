package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MemoEditor(
    modifier: Modifier = Modifier,
    mapId: Int,
    onClose: () -> Unit
) {
    var penProperties by remember { mutableStateOf(PenProperties(
        color = ColorPaletteDefaultColor,
        thickness = 0.5f,
        intensity = 1f,
        mode = 0
    )) }
    var saving by remember { mutableStateOf(true) }
    var awaitingSavingToClose by remember { mutableStateOf(false) }
    var mapName by remember { mutableStateOf("読み込み中...") }

    Box(modifier = modifier) {
        DataSynchronizer(
            mapId = mapId,
            saving = saving,
            onSavingChange = { newSaving ->
                saving = newSaving
                if(!newSaving && awaitingSavingToClose) onClose()
            }
        ) { drawing, map, image ->
            if (map != null && drawing != null && image != null) {
                LaunchedEffect(key1 = map) {
                    mapName = map.mapName
                }

                DrawingCanvas(
                    modifier = Modifier.fillMaxSize(),
                    background = image,
                    drawingData = drawing,
                    penProperties = penProperties
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TitleBar(
                modifier = Modifier
                    .height(66.dp)
                    .fillMaxWidth(),
                title = mapName,
                onCloseRequest = {
                    saving = true
                    awaitingSavingToClose = true
                }
            )

            if(penProperties.mode == 0){
                ControlPanel(
                    onChange = {penProperties = penProperties.copy(mode = 1)},
                    penSettings = penProperties,
                    onPenSettingsChange = { penProperties = it }
                )
            }else{
                ControlPanel(onChange = {penProperties = penProperties.copy(mode = 0)})
            }
        }
    }

    if(saving) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
