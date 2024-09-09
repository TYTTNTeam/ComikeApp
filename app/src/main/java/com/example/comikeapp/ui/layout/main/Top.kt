package com.example.comikeapp.ui.layout.main

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun Top() {
    var screenID by remember { mutableStateOf(0) }
    var selectedMapId by remember { mutableStateOf<Int?>(null) }
    val isEditorVisible = screenID == -1
    val context = LocalContext.current

    LaunchedEffect(selectedMapId) {

        selectedMapId?.let {
            val sharedPreferences =
                context.getSharedPreferences("topInitialise", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("selectedMapId", it)
            editor.apply()
        }
    }

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("topInitialise", Context.MODE_PRIVATE)
        selectedMapId = sharedPreferences.getInt("selectedMapId", 0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = if (isEditorVisible) 0.dp else BottomBarHeightDp)
    ) {
        selectedMapId?.let {
            Content(
                screenID = screenID,
                selectedMapId = it,
                onShowMemoEditor = { isVisible ->
                    screenID = if (isVisible) -1 else 0
                },
                onMapIdChange = { mapId -> // 追加：地図ID変更時の処理
                    selectedMapId = mapId
                }
            )
        }

    }

    if (!isEditorVisible) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomBar { index ->
                screenID = index
            }
        }
    }
}
