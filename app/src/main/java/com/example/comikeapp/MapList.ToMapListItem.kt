package com.example.comikeapp

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.layout.menu.MapListItem
import com.example.comikeapp.ui.theme.ComikeAppTheme

@Composable
fun ToMapList() {
    ComikeAppTheme(darkTheme = false) {
        MapListItem(mapName = "   地図地図",
            onNameChange = {},
            onDelete = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ToMapListPreview() {
    ComikeAppTheme(darkTheme = false) {
        Column {
            MapListItem(mapName = "地図地図",
                onNameChange = {},
                onDelete = {})
            MapListItem(mapName = "地図2",
                onNameChange = {},
                onDelete = {})
            MapListItem(mapName = "地図3",
                onNameChange = {},
                onDelete = {})
            MapListItem(mapName = "地図4",
                onNameChange = {},
                onDelete = {})
        }
    }
}