package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog

@Composable
fun ChangeList(
    mapList: List<MapList>, onNo: () -> Unit
) {
    var contentSize by remember { mutableStateOf(Size.Zero) }

    Dialog(onDismissRequest = onNo) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Box( // 外枠の緑
                modifier = Modifier
                    .width(194.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val roundedShape = RoundedCornerShape(16.dp) // 白四角の角の丸さ加減
                Box( // 内側の白
                    modifier = Modifier
                        .width(179.dp)
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.background, shape = roundedShape)
                        .onGloballyPositioned { coordinates ->
                            contentSize = coordinates.size.toSize()
                        }) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(mapList) { mapItem ->
                            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                                //ボタン押処理
                            }) {
                                Text(text = if (mapItem.mapName != null){
                                    mapItem.mapName!!
                                }else{"mapNameがありません。"}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}