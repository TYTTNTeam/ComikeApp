package com.example.comikeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.theme.ComikeAppTheme


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComikeAppTheme {
        BottomBar(onChange = { index ->
            println("Selected index: $index")
        })
    }
}