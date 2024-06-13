package com.example.comikeapp

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import java.io.File


@Composable
fun RotatableMap(imagePath : String) {
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val zoomState = rememberZoomState()
    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(snackBarHostState)

    // 画像を読み込む
    LaunchedEffect(imagePath) {
        try {
            val file = File(imagePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                imageBitmap.value = bitmap?.asImageBitmap()
            }
        } catch (e :Exception){
            // エラー処理
            snackBarHostState.showSnackbar("エラーが発生しました")
        }


    }

    // 画像が存在する場合、表示する
    if (imageBitmap.value != null) {
        Box(modifier = Modifier.padding(bottom = BottomBarHeightDp)) {
            Image(
                bitmap = imageBitmap.value!!,
                contentDescription = null,
                modifier = Modifier
                    .zoomable(zoomState)
            )
        }
    } else {
        // 画像が存在しない場合の処理 (エラー表示など)
        Text("画像が見つかりませんでした")
    }
}
