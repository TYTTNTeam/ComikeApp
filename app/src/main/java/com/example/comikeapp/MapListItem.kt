package com.example.comikeapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex

@Composable
fun MapListItem(
    mapName: String,
    onNameChange: () -> Unit,
    onDelete: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = 64.dp

    var isMenuOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures { // 外側の領域をタップした場合の処理
                    if (isMenuOpen) {
                        isMenuOpen = false
                    }
                }
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // スクリーン幅に応じてサイズを調整
            val boxWidth = if (screenWidth >= 600.dp) screenWidth else screenWidth
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = boxWidth, height = screenHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = mapName,
                        textAlign = TextAlign.Start,
                        style = TextStyle(MaterialTheme.colorScheme.background),
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "メニュー",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                            .clickable {
                                isMenuOpen = !isMenuOpen
                            },
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isMenuOpen) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(focusable = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { // 外側の領域をタップした場合の処理
                                if (isMenuOpen) {
                                    isMenuOpen = false
                                }
                            }
                        }
                ){}
            }
            //ポップアップ表示にする
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(x = 0, y = 0),
                properties = PopupProperties(focusable = false)
            ) {
                AnimatedVisibility(
                    visible = isMenuOpen,
                    enter = expandVertically(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                    exit = shrinkVertically(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = FastOutLinearInEasing
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 13.dp, horizontal = 6.dp)
                            .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .size(width = 153.dp, height = 190.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .align(Alignment.TopEnd)
                            .zIndex(1f)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .size(60.dp)
                                    .align(Alignment.End)
                                    .padding(8.dp)
                                    .clickable {
                                        if (isMenuOpen) {
                                            isMenuOpen = false
                                        }
                                    }
                            )

                            Button(
                                onClick = onNameChange,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .align(Alignment.Start),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    "名前変更",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    style = TextStyle(MaterialTheme.colorScheme.background),
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(3.dp)
                                    .background(MaterialTheme.colorScheme.background)
                            )

                            Button(
                                onClick = onDelete,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .align(Alignment.Start),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    "削除　　",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    style = TextStyle(MaterialTheme.colorScheme.background),
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(3.dp)
                                    .background(MaterialTheme.colorScheme.background)
                            )
                        }
                    }
                }
            }
        }
    }
}