package com.example.comikeapp.ui.layout.main

import androidx.compose.runtime.Composable
import com.example.comikeapp.ui.layout.editor.MemoEditor
import com.example.comikeapp.ui.layout.menu.MapList
import com.example.comikeapp.ui.layout.map.MapView

@Composable
fun Content(screenID: Int, selectedMapId: Int, onShowMemoEditor: (Boolean) -> Unit, onMapIdSelected: (Int) -> Unit) {
    when (screenID) {
        0 -> MapView(onShowMemoEditor = onShowMemoEditor, onMapIdSelected = onMapIdSelected)
        1 -> MapList()
        -1 -> MemoEditor(
            mapId = selectedMapId, // Topから受け取ったmapIdを渡す
            onClose = { onShowMemoEditor(false) } // MemoEditorを閉じる
        )
    }
}