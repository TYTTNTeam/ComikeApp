package com.example.comikeapp

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun Top() {
    var screenID by remember { mutableIntStateOf(0) }
    Column{
        Content(screenID =  screenID)
        BottomBar { index ->
            Log.d("aaa", "Top index: $index")
            screenID = index
        }
        }
    }


