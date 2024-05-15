package com.example.comikeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ToMapList() {
    MapListItem(mapName = "   地図地図",
        onNameChange = {},
        onDelete = {})
    }

@Preview(showBackground = true)
@Composable
fun ToMapListPreview() {
    MapListItem(mapName = "地図地図",
        onNameChange = {},
        onDelete = {})
    }