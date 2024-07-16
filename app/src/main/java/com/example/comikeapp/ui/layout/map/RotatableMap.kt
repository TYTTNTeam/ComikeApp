package com.example.comikeapp.ui.layout.map

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.comikeapp.ui.layout.map.ImageLoadingStatus.Error
import com.example.comikeapp.ui.layout.map.ImageLoadingStatus.ImageLoaded
import com.example.comikeapp.ui.layout.map.ImageLoadingStatus.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import java.io.File
import java.io.FileNotFoundException

enum class ImageLoadingStatus {
    Loading, // 画像読み込み中状態
    ImageLoaded, // 画像読み込み完了状態
    Error // 画像読み込み失敗状態
}

@Composable
fun RotatableMap(imagePath: String) {
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    //val zoomState = rememberZoomState(50f)
    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState) { snackbarData ->
        Snackbar(
            snackbarData = snackbarData,
            shape = RoundedCornerShape(8.dp),
            containerColor = Color.Red,
            contentColor = Color.White
        )
    }
    var imageLoadingStatus by remember { mutableStateOf(Loading) }

    LaunchedEffect(imagePath) {
        imageLoadingStatus = Loading
    }

    val uiStateFlow = remember { MutableStateFlow(imageLoadingStatus) }
    LaunchedEffect(imageLoadingStatus) {
        val currentState = imageLoadingStatus
        uiStateFlow.value = currentState
        try {
            val file = File(imagePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                imageBitmap.value = bitmap?.asImageBitmap()
                imageLoadingStatus = ImageLoaded
            } else {
                throw FileNotFoundException("")
            }
        } catch (e: Exception) {
            imageLoadingStatus = when (e) {
                is FileNotFoundException -> {
                    snackBarHostState.showSnackbar("画像読み込みエラー: ファイルが見つかりませんでした: ${e.message}")
                    Error
                }

                else -> {
                    snackBarHostState.showSnackbar("画像読み込みエラー: ${e.message}")
                    Error
                }
            }
        } finally {
            if (imageBitmap.value != null) {
                imageLoadingStatus = ImageLoaded
            } else {
                imageLoadingStatus = Error
            }
        }

    }

    val currentUiState = uiStateFlow.collectAsState().value
    Column {
        when (currentUiState) {
            Loading -> {
                Text("画像を読み込み中...")
                // インジケーターの表示
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            ImageLoaded -> {
                val zoomState = rememberZoomState(50f)
                // 画像の表示
                if (imageBitmap.value != null) {
                    Image(
                        bitmap = imageBitmap.value!!,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .zoomable(zoomState)
                    )
                } else {
                    Text("画像読み込みに失敗しました。")
                }
            }

            Error -> {

            }
        }
    }
}