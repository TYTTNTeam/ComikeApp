package com.example.comikeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.theme.ComikeAppTheme


@Composable
fun MapList(name: String, modifier: Modifier = Modifier) {
    ComikeAppTheme {
        DeleteMapDialog("地図1",
                        onYes={},
                        mapList={})
    }
}

@Preview(showBackground = true)
@Composable
fun MapListPreview() {
    MapList("Android")
}

