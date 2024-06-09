package com.example.comikeapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun FilePickerButton() {
    var pickedImageUri by remember { mutableStateOf<ImageBitmap?>(null) }
    var fileName by remember { mutableStateOf("")}
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        if (uri != null) {
            uri.path?.let {path -> fileName = File(path).name }
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val newMap = MapImageRecorder(context)
                    val file = newMap.render(uri, "test")
                    newMap.rollback()
                    val filePicker = MapImageRecorder(context)
                    val pickedFile = filePicker.getRecordFile("test")
                    val bitmap = BitmapFactory.decodeFile(pickedFile?.path)?.asImageBitmap()
                    withContext(Dispatchers.Main) {
                        pickedImageUri = bitmap
                    }
                } catch (e: Exception) {
                    // エラー処理
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
    pickedImageUri?.let {
        Text(it.toString())
        Image(bitmap = it, contentDescription = "test image")
    }
}