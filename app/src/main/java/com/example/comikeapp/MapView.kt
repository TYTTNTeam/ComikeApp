package com.example.comikeapp

import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comikeapp.ui.theme.ComikeAppTheme
import java.lang.reflect.Modifier

@Composable
fun MapView() {
    Box(
        modifier = androidx.compose.ui.Modifier
            //.fillMaxSize()
            //.weight(1f)
            .padding(horizontal = 8.dp)
        //.clickable { onClick(0) },
        // contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()

                //.align(Alignment.BottomCenter)
        )
        Icon(
            Icons.Filled.Check, contentDescription = "Localized description",
            modifier = androidx.compose.ui.Modifier
                .size(48.dp)
                .padding(bottom = 30.dp,end = 30.dp)

        )
    }
}
@Preview(showBackground = true, heightDp = 600, widthDp = 320)
@Composable
fun MapViewPreview() {
    ComikeAppTheme(darkTheme = false) {
        MapView()
    }
}