package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
        ) { drawing, map ->
            if (map != null && drawing != null) {
                LaunchedEffect(key1 = map) {
                    mapName = map.mapName!!
                }

                DrawingCanvas(
                    modifier = Modifier.fillMaxSize(),
                    imagePath = map.imagePath!!,
                    drawingData = drawing,
                    penProperties = penProperties
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size(100.dp),
                    color = MaterialTheme.colorScheme.onBackground
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
}
