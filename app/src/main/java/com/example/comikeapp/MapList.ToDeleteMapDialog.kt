package com.example.comikeapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.theme.ComikeAppTheme


@Composable
fun MapList(name: String) {
    Text(text = "Map Name: $name")
    ComikeAppTheme {
        DeleteMapDialog("地図1",
                        onYes= {},
                        onNo = {})
    }
}

@Preview(showBackground = true,widthDp = 320)
@Composable
fun MapListPreview() {
    MapList("Android")
}

