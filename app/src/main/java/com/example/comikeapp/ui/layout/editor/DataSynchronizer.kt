package com.example.comikeapp.ui.layout.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.comikeapp.data.fileoperate.manager.ByFileReserve
import com.example.comikeapp.data.fileoperate.manager.FileTypeDefinition
import com.example.comikeapp.data.fileoperate.manager.FileTypes
import com.example.comikeapp.data.fileoperate.reserve.DuplicatingFile
import com.example.comikeapp.data.fileoperate.reserve.ReadingDrawingViewModel
import com.example.comikeapp.data.fileoperate.reserve.ReadingImage
import com.example.comikeapp.data.fileoperate.reserve.SynthesizingMap
import com.example.comikeapp.data.fileoperate.reserve.Writing
import com.example.comikeapp.data.fileoperate.reserve.WritingDrawingViewModel
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun DataSynchronizer(
    mapId: Int,
    saving: Boolean,
    onSavingChange: (Boolean) -> Unit,
    content: @Composable (DrawingViewModel?, MapList?, ImageBitmap?) -> Unit
) {
    var map by remember { mutableStateOf<MapList?>(null) }
    var drawing by remember { mutableStateOf<DrawingViewModel?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val errorList = remember { mutableStateListOf<ErrorState>() }

    val appContext = LocalContext.current
    val db = MapListDatabaseProvider.getDatabase(appContext).mapListDao()
    val snackBarHostState = remember { SnackbarHostState() }

    fun writingHelper(
        f: FileTypeDefinition,
        w: Writing,
        uuid: String
    ) {
        if (!
            ByFileReserve(f, w).execute(appContext, uuid)
        ) errorList.add(ErrorState.WritingException)
    }

    suspend fun synchronizeDrawing(
        currentUUID: String
    ) {
        if (drawing == null) {
            val readingDrawing = ReadingDrawingViewModel()
            ByFileReserve(
                FileTypes.drawingData,
                readingDrawing
            ).execute(appContext, currentUUID)

            val saveData = readingDrawing.getData()

            // セーブデータが見つかったときは読み込み、そうでないときは書き込み
            withContext(Dispatchers.Main) {
                val emptyViewModel = DrawingViewModel()

                if (saveData != null) {
                    emptyViewModel.restoreSaveData(saveData)
                } else {
                    withContext(Dispatchers.IO){
                        writingHelper(
                            FileTypes.drawingData,
                            WritingDrawingViewModel(emptyViewModel.getSaveData()),
                            currentUUID
                        )
                    }
                }

                drawing = emptyViewModel
            }
        } else {
            writingHelper(
                FileTypes.drawingData,
                WritingDrawingViewModel(drawing!!.getSaveData()),
                currentUUID
            )
        }
    }

    suspend fun synchronizeImages(currentUUID: String) {
        if (imageBitmap == null) {
            val readingRawImage = ReadingImage()
            ByFileReserve(
                FileTypes.rawImage,
                readingRawImage
            ).execute(appContext, currentUUID)

            var readRawImage = readingRawImage.getData()

            // rawImageがファイルシステムにないときは、imageをもとに、rawImageファイルを新規作成
            if (readRawImage == null) {
                val readingImage = ReadingImage()
                ByFileReserve(
                    FileTypes.image,
                    readingImage
                ).execute(appContext, currentUUID)

                val readImage = readingImage.getData()

                if (readImage != null) {
                    writingHelper(
                        FileTypes.rawImage,
                        DuplicatingFile(readingImage.accessedFile!!),
                        currentUUID
                    )

                    readRawImage = readImage
                } else {
                    errorList.add(ErrorState.DefaultImageNotFound)
                }
            }

            withContext(Dispatchers.Main) {
                imageBitmap = readRawImage
            }
        } else {
            drawing?.let {
                writingHelper(
                    FileTypes.image,
                    SynthesizingMap(imageBitmap!!, it),
                    currentUUID
                )
            }
        }
    }

    LaunchedEffect(key1 = saving) {
        if (saving) {
            withContext(Dispatchers.IO) {
                val mapData: MapList = if (map != null){
                    map!!
                }else{
                    val m = db.selectById(mapId) ?: throw IllegalArgumentException(
                        "The map data matching the map ID cannot be found in the database. Map ID: $mapId"
                    ) // WARN: これがないと、NullPointerExceptionが出て原因がわかりにくい。
                    map = m
                    m
                }

                synchronizeDrawing(mapData.imagePath)
                synchronizeImages(mapData.imagePath)

                onSavingChange(false)
            }
        }
    }

    LaunchedEffect(key1 = errorList.isEmpty()) {
        if(errorList.isNotEmpty()){
            var text = "・エラー："
            errorList.forEachIndexed { i, state ->
                if(i != 0) text += "\n・エラー："
                text += when(state) {
                    ErrorState.WritingException -> "メモのセーブに失敗しました。エディターを開きなおしてください。"
                    ErrorState.DefaultImageNotFound -> "ファイルが破損しています。地図を登録しなおす必要があります。"
                }
            }
            snackBarHostState.showSnackbar(message = text, duration = SnackbarDuration.Indefinite)
            errorList.clear()
        }
    }

    content(drawing, map, imageBitmap)

    Column {
        Spacer(modifier = Modifier.height(100.dp))
        SnackbarHost(hostState = snackBarHostState) {
            Snackbar(
                snackbarData = it,
                shape = RoundedCornerShape(8.dp),
                containerColor = Color.Red,
                contentColor = Color.White
            )
        }
    }
}

private enum class ErrorState { WritingException, DefaultImageNotFound }
