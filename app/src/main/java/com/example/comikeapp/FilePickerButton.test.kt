package com.example.comikeapp

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.comikeapp.data.mapimagefile.MapImageRecorder
import com.example.comikeapp.ui.layout.map.RotatableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun FilePickerButton() {
    var fileName by remember { mutableStateOf("")}
    var filePath by remember { mutableStateOf("")}
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHost(snackbarHostState)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        if (uri != null) {
            uri.path?.let {path -> fileName = File(path).name }

            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val newMap = MapImageRecorder(context)
                    val file = newMap.render(uri, fileName)
                    val gotFile = File(filePath)
                    withContext(Dispatchers.Main) {
                        filePath = file.path // この状態でデータベースに格納
                    }

                } catch (e: Exception) {
                    // エラー表示
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("エラーが発生しました")
                    }
                }
            }
        }
    }


    Text(text = fileName)
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
    RotatableMap(imagePath = filePath)
}


