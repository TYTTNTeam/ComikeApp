package com.example.comikeapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun MapList() {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val mapNames = listOf("地図1", "地図2", "地図3", "地図4", "地図5", "地図6", "地図7", "地図8", "地図9", "地図10")

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
    var newName by remember { mutableStateOf("地図1") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ){
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
                    items(mapNames.size) { mapName ->
                        MapListItem(mapName = newName,
                            onNameChange = {},
                            onDelete = {})
                    }
                }
                selectedFileUri?.let { uri ->
                    //MapMapRegistDialog()
                }
                Image(painter = painterResource(R.drawable.buttonaddmap),
                    contentDescription = "BottomBarIcon",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { launcher.launch(pickFileIntent) }
                        .align(Alignment.BottomEnd)
                        .zIndex(1f)
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = 13.dp, horizontal = 9.dp)
                        .clip(CircleShape)
                        .shadow(8.dp, shape = CircleShape)
                        .size(86.dp, 86.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }

    if(false){
        ChangeMapNameDialog(
            mapName = newName,
            onYes = {newName ->
                Log.d("test", newName)
            },
            onNo = { showNameChangeDialog = false }
        )
    }

    if(false) {
        DeleteMapDialog(
            mapName = newName,
            onYes = { Log.d("test", "onYes") },
            onNo = { showMapDeleteDialog = false })
    }
}