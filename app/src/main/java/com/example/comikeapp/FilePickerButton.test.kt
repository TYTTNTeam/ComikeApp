package com.example.comikeapp

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun FilePickerButton() {
    var pickedImageUri by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        if (uri != null) {
            coroutineScope.launch {
                pickedImageUri = performAsyncTask(uri)
            }
        }
    }

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            launcher.launch(intent)
        }
    ) {
        Text("Select")
    }
    pickedImageUri?.let {
        Text(it)
    }
}

suspend fun performAsyncTask(uri: Uri): String {
    // 非同期処理をここに記述します
    // 例: URIからファイルを読み込んで処理する
    delay(1000) // これはサンプルの遅延です。実際の非同期処理に置き換えてください。
    Log.d("AsyncTask", "Async task completed with URI: $uri")
    return uri.toString()
}
