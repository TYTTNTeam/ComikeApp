package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import com.example.comikeapp.data.maplist.MapListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DBTest() {
    val context = LocalContext.current
    val repository by remember{ mutableStateOf(
        MapListRepository(
        MapListDatabaseProvider.getDatabase(context).mapListDao()
    )
    ) }
    var mapName by remember { mutableStateOf("") }
    var newName by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var mapList: List<MapList> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val data = repository.getAll()
            withContext(Dispatchers.Main) {
                mapList = data
            }
        }
    }
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {

            TextField(
                value = mapName,
                onValueChange = { mapName = it },
                label = { Text("名前") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            TextField(
                value = imagePath,
                onValueChange = { imagePath = it },
                label = { Text("画像パス") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val data = repository.insertAndGetAll(mapName, imagePath)
                        withContext(Dispatchers.Main) {
                            mapList = data
                        }
                    }

                }, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)

            ) {
                Text(text = "追加")
            }
            TextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("名前変更") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
        ) {
            Text("登録されたマップリスト:")
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(mapList) { mapItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "ID${mapItem.mapId}: 名前${mapItem.mapName}, 画像パス${mapItem.imagePath}",
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                val data = repository.updateAndGetAll(mapItem.mapId,newName)
                                withContext(Dispatchers.Main) {
                                    mapList = data
                                }
                            }
                        }, modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text("変更")
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                val data = repository.deleteAndGetAll(mapItem.mapId)
                                withContext(Dispatchers.Main) {
                                    mapList = data
                                }
                            }
                        }, modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text("削除")
                    }
                }
            }
        }
    }
}