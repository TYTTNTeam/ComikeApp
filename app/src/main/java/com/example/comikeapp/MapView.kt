package com.example.comikeapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun MapView() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedMapId by remember{ mutableStateOf<Int?>(null)}
    Box {
        Button(
            onClick = { showDialog = true }, // ボタンがクリックされたときにダイアログを表示する
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .zIndex(1f)
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.button_change_map_shortcut),
                contentDescription = "BottomBarIcon"
            )
        }
        if (showDialog) {
            ChangList(
                onChangMapId = {mapId -> selectedMapId = mapId},
                onNo = { showDialog = false } // ダイアログを閉じる
            )
        }
        selectedMapId?.let{  mapId ->
            RotatableMap(mapId)
        }
    }
}