package com.example.comikeapp.ui.layout.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.R

@Composable
fun ChangeList(
    mapList: List<MapList>?, onNo: () -> Unit, passImagePath: (String?) -> Unit
) {
    val maxVisibleItems = 6
    val userScrollEnabled = if (mapList == null) false else mapList.size >= maxVisibleItems
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Popup(
            alignment = Alignment.BottomEnd,
            onDismissRequest = onNo
        ) {
            Box(
                modifier = Modifier
                    .width(194.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val roundedShape = RoundedCornerShape(16.dp)
                Box(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.background,
                        shape = roundedShape
                    )
                ) {
                    if (mapList != null) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            LazyColumn(
                                modifier = (if (userScrollEnabled) {
                                    Modifier.height(screenHeight * (1 / 2f))
                                } else Modifier).fillMaxWidth(),
                                userScrollEnabled = userScrollEnabled
                            ) {
                                items(mapList) { mapItem ->
                                    Button(modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        onClick = {
                                            passImagePath(mapItem.imagePath)
                                        }) {
                                        Text(
                                            text = mapItem.mapName ?: "mapNameがありません。",
                                            color = MaterialTheme.colorScheme.background,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                            Button(
                                onClick = onNo,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.buttonexitmaplist),
                                    contentDescription = "Close ChangeList"
                                )
                            }
                        }

                    } else {
                        Box(
                            modifier = Modifier
                                .height(screenHeight * (1 / 2f))
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

