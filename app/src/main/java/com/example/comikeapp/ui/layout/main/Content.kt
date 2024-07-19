package com.example.comikeapp.ui.layout.main

import androidx.compose.runtime.Composable
import com.example.comikeapp.ui.layout.menu.MapList
import com.example.comikeapp.ui.layout.map.MapView

@Composable
fun Content(screenID: Int) {
    when (screenID) {
        0 -> MapView()
        1 -> MapList()
    }
}