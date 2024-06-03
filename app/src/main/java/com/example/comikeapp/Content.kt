package com.example.comikeapp

import androidx.compose.runtime.Composable

@Composable
fun Content(screenID: Int) {
    when (screenID) {
        0 -> MapView()
        1 -> MapList()
    }
}