package com.example.comikeapp

import android.content.res.Configuration
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.layout.menu.DeleteMapDialog
import com.example.comikeapp.ui.theme.ComikeAppTheme


@Composable
fun MapList(name: String) {
    Text(text = "Map Name: $name")
    DeleteMapDialog("地図1",
                    onYes= {Log.d("test", "onYes")},
                    onNo = {Log.d("test", "onNo")})
}

@Preview(showBackground = true,widthDp = 320, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MapListPreview() {
    ComikeAppTheme {
        MapList("Android")
    }
}

