package com.example.comikeapp.ui.layout.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.comikeapp.data.maplist.MapList
import kotlinx.coroutines.delay


@Composable
fun DataSynchronizer(
    modifier: Modifier = Modifier,
    mapId: Int,
    saving: Boolean,
    onSavingChange: (Boolean) -> Unit,
    content: @Composable (DrawingViewModel?, MapList?) -> Unit
) {
    var drawing by remember { mutableStateOf<DrawingViewModel?>(null) }
    var map by remember { mutableStateOf<MapList?>(null) }

    LaunchedEffect(key1 = saving) {
        if(saving){
            delay(1000)
            drawing = DrawingViewModel()
            map = MapList(0, "test map name", "test/path.png")
            onSavingChange(false)
        }
    }

    content(drawing, map)
    // TODO 未実装
}