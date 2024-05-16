package com.example.comikeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
        MapListItem(mapName = "地図地図",
            onNameChange = {},
            onDelete = {})
    }
}