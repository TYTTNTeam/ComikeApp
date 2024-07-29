package com.example.comikeapp.ui.layout.editor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.comikeapp.ui.theme.ComikeAppTheme

@Preview(heightDp = 800, widthDp = 350)
@Composable
fun MemoEditorPreview() {
    ComikeAppTheme(darkTheme = false) {
        MemoEditor(Modifier, 0, {})
    }
}