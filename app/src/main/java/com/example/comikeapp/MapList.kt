package com.example.comikeapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapList() {
    val context = LocalContext.current
    val repository by remember {
        mutableStateOf(
            MapListRepository(
                MapListDatabaseProvider.getDatabase(context).mapListDao()
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()
    var mapList: List<MapList> by remember { mutableStateOf(emptyList()) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            this.coroutineContext
            val dataList = repository.getAll()
            withContext(Dispatchers.Main) {
                mapList = dataList
            }
        }
    }


    // ランチャーを定義
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFileUri = uri
            }
        }
    }
    // ファイルピッカーを起動するIntent
    val pickFileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "*/*"
    }

    var showNameChangeDialog by remember { mutableStateOf(false) }
    var showMapDeleteDialog by remember { mutableStateOf(false) }
    var showMapRegistDialog by remember { mutableStateOf(false) }
    val newName by remember { mutableStateOf("") }
    var index by remember { mutableStateOf(-1) }
    var loading by remember { mutableStateOf(false)}


    Box(
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
                    .background(MaterialTheme.colorScheme.background) // 白色の背景
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(mapList.size) { mapItem ->
                        MapListItem(mapName = mapList[mapItem].mapName!!,
                            onNameChange = {
                                showNameChangeDialog = true
                                index = mapItem
                            },
                            onDelete = {
                                showMapDeleteDialog = true
                                //index = mapList.indexOf(mapItem)
                            }

                        )
                    }
                }
                selectedFileUri?.let { uri ->
                    showMapRegistDialog = true
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 10.dp, end = 10.dp)
                ) {
                    // NOTE このBoxは影を濃くするためだけにあります。
                    Box(
                        modifier = Modifier
                            .height(5.dp)
                            .width(50.dp)
                            .shadow(20.dp)
                    )
                    FloatingActionButton(
                        onClick = { launcher.launch(pickFileIntent) },
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
    }

    if (showNameChangeDialog) {
        ChangeMapNameDialog(
            mapName = newName,
            onYes = {
                coroutineScope.launch(Dispatchers.IO) {
                   // mapList[index].mapName = ,
                    if (mapList[index].mapName != null) {
                        val data = repository.updateAndGetAll(
                            mapList[index].mapId,
                            mapList[index].mapName!!
                        )
                        withContext(Dispatchers.Main) {
                            mapList = data
                        }
                    } else {
                      //  Text(text = "")
                    }
                }
            },
            onNo = { showNameChangeDialog = false }
        )
    }

    if(loading){
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }

    if (showMapDeleteDialog) {
        DeleteMapDialog(
            mapName = newName,
            onYes = {
                loading = true
                coroutineScope.launch(Dispatchers.IO) {
                    val data = repository.deleteAndGetAll(mapList[index].mapId)
                    withContext(Dispatchers.Main) {
                        mapList = data
                    }
                }
            },
            onNo = { showMapDeleteDialog = false })
    }

    if (showMapRegistDialog) {
        MapRegistDialog(
            pdfsName = newName,
            onYes = {

            },
            onNo = { showMapRegistDialog = false })
    }
}



@Composable
fun ErrorMessage(){
    Box(
        modifier = Modifier
        .fillMaxSize()
    ){
        Text(text = "mapListControllerでエラーが発生しました")
    }
}







