package com.example.comikeapp

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

@Composable
fun MapListToMapRegistrationSequencer() {
    val isProgressing = booleanArrayOf(false, true, false, true)
    var sequence by remember { mutableStateOf(0) }
    var list by remember { mutableStateOf<List<MapList>?>(null) }
    var dummyFilePath by remember { mutableStateOf<String?>(null) }
    var manager by remember { mutableStateOf(MapRegistrationSequencer()) } // インスタンスは途中で失われないようにします

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        if (uri != null) {
            sequence = 2
            scope.launch(Dispatchers.IO) {
                try {
                    manager.register(this, context, uri) { newList ->
                        launch(Dispatchers.Main) {
                            list = newList
                            sequence = 0
                        }
                    }
                }catch (e: Exception){
                    Log.e("MapList.FromMapRegistrationSequencer.kt", e.message.toString())
                    sequence = 0
                }
            }
        }else{
            sequence = 0
        }
    }
    LaunchedEffect(key1 = sequence) {// 処理をやり直すときは初期化します
        if(sequence == 0){
            manager = MapRegistrationSequencer()
        }
    }
    val fileName = "dummy.png"
    val dir = MapImageRecorder(context).mapImagesDirectory
    LaunchedEffect(key1 = sequence) {
        val foundFiles = Files.list(dir.toPath())
            .filter { Files.isRegularFile(it) }
            .map(Path::toFile)
            .collect(Collectors.toList())
        dummyFilePath = foundFiles.toString()
        Log.d("test", "list update")
    }

    Column {
        Row(Modifier.background(Color.Gray)) {
            dummyFilePath?.let {
                Text(text = it)
            }
        }
        Button(
            onClick = {
                sequence = 1
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                launcher.launch(intent)
            }
        ) {
            Text(text = "Open")
        }
        if(isProgressing[sequence]){
            Text(text = "in Progressing...")
        }
        if(sequence == 2){
            Button(onClick = {
                sequence = 3
                manager.confirmName("map name test", true)
            }) {
                Text(text = "OK")
            }
            Button(onClick = {
                sequence = 0
                manager.confirmName("", false)
            }) {
                Text(text = "Cancel")
            }
        }
        list?.let {
            Text(text = it.toString())
        }
    }
}