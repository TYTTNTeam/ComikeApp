package com.example.comikeapp.ui.layout.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import com.example.comikeapp.data.maplist.MapListRepository
import com.example.comikeapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MapView(onShowMemoEditor: (Boolean) -> Unit, onMapIdSelected: (Int) -> Unit) {
    val context = LocalContext.current
    val repository by remember {
        mutableStateOf(
            MapListRepository(
                MapListDatabaseProvider.getDatabase(context).mapListDao()
            )
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    var imagePathState by remember { mutableStateOf<String?>(null) }
    var mapList: List<MapList>? by remember { mutableStateOf(null) }
    var currentMapId by remember { mutableStateOf(0) }

    val didNotRegistration = "dnr"

    LaunchedEffect(mapList, Dispatchers.Main) {
        if (mapList == null) {
            var data: List<MapList>
            withContext(Dispatchers.IO) {
                data = repository.getAll()
            }
            if (data.isNotEmpty()) {
                mapList = data
                imagePathState = data[0].imagePath
                currentMapId = data[0].mapId
                onMapIdSelected(currentMapId) // 初期選択状態を更新
            } else {
                mapList = emptyList()
                imagePathState = didNotRegistration
            }
        }
    }

    imagePathState?.let {
        if (imagePathState == didNotRegistration) {
            Box {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "まだ地図が登録されていません。",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            RotatableMap(imagePath = it)
        }
    }

    if (imagePathState == null) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(100.dp)
                .padding(12.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
        Button(
            onClick = {
                onShowMemoEditor(true)
                onMapIdSelected(currentMapId)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(1f)
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.NoteAlt,
                contentDescription = "MemoEditorIcon",
                modifier = Modifier.size(75.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .zIndex(1f)
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.button_change_map_shortcut),
                contentDescription = "BottomBarIcon"
            )
        }
        if (showDialog) {
            ChangeList(
                mapList = mapList,
                onNo = { showDialog = false },
                passImagePath = { newImagePath ->
                    imagePathState = newImagePath
                    currentMapId = mapList?.find { it.imagePath == newImagePath }?.mapId ?: 0
                    onMapIdSelected(currentMapId)
                    showDialog = false
                }
            )
        }
    }
}
