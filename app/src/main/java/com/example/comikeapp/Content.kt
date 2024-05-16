package com.example.comikeapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Content(screenID: Int) {
    Log.d("aa","Content: $screenID")
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(1500.dp)
        .background(Color.Blue))
}