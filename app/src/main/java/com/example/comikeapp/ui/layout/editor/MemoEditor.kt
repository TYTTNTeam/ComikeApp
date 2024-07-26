package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var mapName by remember { mutableStateOf("読み込み中...") }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TitleBar(
                modifier = Modifier.height(66.dp).fillMaxWidth(),
                title = mapName,
                onCloseRequest = {
                    // TODO
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
