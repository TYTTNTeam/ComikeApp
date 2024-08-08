package com.example.comikeapp.ui.layout.main

import androidx.compose.runtime.Composable
import com.example.comikeapp.ui.layout.editor.MemoEditor
import com.example.comikeapp.ui.layout.menu.MapList
import com.example.comikeapp.ui.layout.map.MapView

@Composable
fun Content(
    screenID: Int,
    selectedMapId: Int,
    onShowMemoEditor: (Boolean) -> Unit,
    onMapIdChange: (Int) -> Unit // 修正：引数名変更
) {
    when (screenID) {
        0 -> MapView(
            currentMapId = selectedMapId, // 追加：現在の地図IDを渡す
            onMapIdChange = onMapIdChange, // 追加：地図ID変更イベントを渡す
            onShowMemoEditor = onShowMemoEditor // 既存：編集モードイベントを渡す
        )
        1 -> MapList()
        -1 -> MemoEditor(
            mapId = selectedMapId, // 選択された地図IDを渡す
            onClose = { onShowMemoEditor(false) }
        )
    }
}