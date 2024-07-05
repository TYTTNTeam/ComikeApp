package com.example.comikeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.layout.main.Top
import com.example.comikeapp.ui.theme.ComikeAppTheme


@Preview(showBackground = true, heightDp = 600, widthDp = 320)
@Composable
fun GreetingPreview() {
    ComikeAppTheme(darkTheme = false) {
        Top()
    }
}