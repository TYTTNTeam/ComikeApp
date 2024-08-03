package com.example.comikeapp.ui.layout.menu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.comikeapp.R
import com.example.comikeapp.data.fileoperate.manager.ByFileReserve
import com.example.comikeapp.data.fileoperate.manager.FileTypes
import com.example.comikeapp.data.fileoperate.reserve.Deleting
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import com.example.comikeapp.data.maplist.MapListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapList() {
    var access by remember { mutableStateOf(false) }
    var showNameChangeDialog by remember { mutableStateOf(false) }
    var showMapDeleteDialog by remember { mutableStateOf(false) }
    var showMapRegistDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var indexToDelete by remember { mutableIntStateOf(-1) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val repository by remember {
        mutableStateOf(
            MapListRepository(
                MapListDatabaseProvider.getDatabase(context).mapListDao()
            )
        )
    }
    var manager by remember { mutableStateOf(MapRegistrationSequencer()) }
    val coroutineScope = rememberCoroutineScope()
    var mapList: List<MapList>? by remember { mutableStateOf(null) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        if (mapList == null) {
            loading = true
        }
        val dataList: List<MapList>
        withContext(Dispatchers.IO) {
            dataList = repository.getAll()
        }// コミケ地図
        mapList = dataList
        loading = false
    }

    // ランチャーを定義
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFileUri = uri
                showMapRegistDialog = true
                newName = getFileNameFromUri(context, uri) ?: uri.toString()
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        manager.register(this, context, uri) { newList ->
                            coroutineScope.launch(Dispatchers.Main) {
                                mapList = newList
                                loading = false
                            }
                        }
                    } catch (e: Exception) {
                        snackBarHostState.showSnackbar("地図の追加に失敗しました")
                    }
                }
            }
        }
    }

    // ファイルピッカーを起動するIntent
    val pickFileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "application/pdf"
    }

    Box( // 　
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.background)// 白色の背景
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    mapList?.let {
                        items(it.size) { index ->
                            MapListItem(mapName = it[index].mapName,
                                onNameChange = {
                                    newName = it[index].mapName
                                    showNameChangeDialog = true
                                    indexToDelete = index
                                },
                                onDelete = {
                                    showMapDeleteDialog = true
                                    indexToDelete = index
                                    newName = it[index].mapName
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(96.dp))
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 10.dp, end = 10.dp)
                ) {
                    Box(
                        // NOTE このBoxは影を濃くするためだけにあります
                        modifier = Modifier
                            .height(5.dp)
                            .width(50.dp)
                            .shadow(20.dp)
                    )
                    FloatingActionButton(
                        onClick = {
                            access = true
                            launcher.launch(pickFileIntent)
                        },
                        shape = CircleShape,
                        modifier = Modifier.zIndex(1f)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.button_add_map),
                            contentDescription = "Add new map.",
                        )
                    }
                }
            }
        }
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center)
                //trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }

    mapList?.let { list ->
        if (showNameChangeDialog) {
            ChangeMapNameDialog(
                mapName = newName,
                onYes = { changeName ->
                    loading = true
                    showNameChangeDialog = false
                    coroutineScope.launch(Dispatchers.IO) {
                        val data = repository.updateAndGetAll(
                            list[indexToDelete].mapId,
                            changeName
                        )
                        withContext(Dispatchers.Main) {
                            mapList = data
                            loading = false
                        }
                    }
                },
                onNo = { showNameChangeDialog = false }
            )
        }

        if (showMapDeleteDialog) {
            DeleteMapDialog(
                mapName = newName,
                onYes = {
                    loading = true
                    showMapDeleteDialog = false
                    coroutineScope.launch(Dispatchers.IO) {
                        val mapToDelete = list[indexToDelete]

                        val deleteImage = ByFileReserve(FileTypes.image, Deleting())
                        val deleteImagePath = ByFileReserve(FileTypes.rawImage, Deleting())
                        val deleteDrawingData = ByFileReserve(FileTypes.drawingData,Deleting())

                        deleteImage.execute(context, mapToDelete.imagePath)
                        deleteImagePath.execute(context, mapToDelete.imagePath)
                        deleteDrawingData.execute(context, mapToDelete.imagePath)

                        val data = repository.deleteAndGetAll(mapToDelete.mapId)
                        withContext(Dispatchers.Main) {
                            mapList = data
                            loading = false
                        }
                    }
                },
                onNo = { showMapDeleteDialog = false }
            )
        }

        if (showMapRegistDialog) {
            MapRegistDialog(
                pdfsName = getFileNameFromUri(context, selectedFileUri!!) ?: "Unknown",
                onYes = { newName ->
                    loading = true
                    showMapRegistDialog = false
                    manager.confirmName(newName, true)
                    manager = MapRegistrationSequencer()
                },
                onNo = {
                    showMapRegistDialog = false
                    manager.confirmName("", false)
                    manager = MapRegistrationSequencer()
                },
                onAccess = {
                    manager = MapRegistrationSequencer()
                    launcher.launch(pickFileIntent)
                }
            )
        }
    }

    SnackbarHost(hostState = snackBarHostState) { errorBar ->
        Snackbar(
            snackbarData = errorBar,
            shape = RoundedCornerShape(8.dp),
            containerColor = Color.Red,
            contentColor = Color.White
        )
    }
}