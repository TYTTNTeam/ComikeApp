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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapList() {
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
    val coroutineScope = rememberCoroutineScope()
    var mapList: List<MapList>? by remember { mutableStateOf(null) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit, Dispatchers.Main) {
        if(mapList == null){
            loading = true
        }
        val dataList: List<MapList>
        withContext(Dispatchers.IO){
            dataList = repository.getAll()
        }
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
            }
        }
    }
    // ファイルピッカーを起動するIntent
    val pickFileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "*/*"
    }


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
                    mapList?.let {
                        items(it.size) { index ->
                            MapListItem(mapName = it[index].mapName!!,
                                onNameChange = {
                                    newName = it[index].mapName!!
                                    showNameChangeDialog = true
                                    indexToDelete = index
                                },
                                onDelete = {
                                    showMapDeleteDialog = true
                                    indexToDelete = index
                                    newName = it[index].mapName!!
                                }
                            )
                        }
                    }
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

    mapList?.let {
        if (showNameChangeDialog) {
            ChangeMapNameDialog(
                mapName = newName,
                onYes = { changeName ->
                    loading = true
                    showNameChangeDialog = false
                    coroutineScope.launch(Dispatchers.IO) {
                        val data = repository.updateAndGetAll(
                            it[indexToDelete].mapId,
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
                        val data = repository.deleteAndGetAll(it[indexToDelete].mapId)
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
            MapRegistDialog( /* TODO insertしてね */
                pdfsName = newName,
                onYes = {

                },
                onNo = { showMapRegistDialog = false }
            )
        }
    }
}