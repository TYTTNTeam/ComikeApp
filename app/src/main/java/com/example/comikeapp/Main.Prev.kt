package com.example.comikeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.theme.ComikeAppTheme


@Preview(showBackground = true, heightDp = 500)
@Composable
fun GreetingPreview() {
    ComikeAppTheme {
        Top()
    }
}