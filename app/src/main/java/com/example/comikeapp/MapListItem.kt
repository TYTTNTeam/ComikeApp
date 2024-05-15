package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun MapListItem(mapName:String,
                onNameChange: () -> Unit,
                onDelete: () -> Unit
) {

    var isSquareVisible by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(width = 330.dp, height = 210.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(width = 325.dp, height = 64.dp)
                        .clip(RoundedCornerShape(8.dp)) // 丸みを持たせる
                        .background(color = Color(0xFF66CC8F)) // 長方形の色を設定します
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.padding(20.dp, 0.dp),
                            text = mapName,  //地図の名前の引数(mapname)
                            textAlign = TextAlign.Start,
                            style = TextStyle(color = Color.White),
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
                                    // ボックスがクリックされたときに正方形の表示をトグルする
                                    isSquareVisible = !isSquareVisible
                                },
                            tint = Color.White // メニューアイコンの色を白色に設定
                        )
                    }
                }
            }


            // クリックされたときに表示される正方形
            if (isSquareVisible) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 13.dp, horizontal = 6.dp)
                        .size(width = 153.dp, height = 190.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = Color(0xFF66CC8F))
                        .align(Alignment.TopEnd)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(4.dp))
                        .let {
                            if (isSquareVisible) it else Modifier.size(0.dp) // isSquareVisibleがfalseの場合は高さと幅を0に設定して非表示にする
                        }
                )
                {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        // ×アイコン
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier
                                .size(width = 60.dp, height = 60.dp)
                                .align(Alignment.End) // 右上に配置
                                .padding(8.dp)
                                .clickable {
                                    // アイコンがクリックされたときに正方形の表示をトグルする
                                    isSquareVisible = !isSquareVisible
                                }
                        )

                        // 名前変更用のボタン
                        Button(
                            onClick = { onNameChange() },
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .align(Alignment.Start),
                            colors = ButtonDefaults
                                .buttonColors(
                                    containerColor = Color(0xFF66CC8F)
                                )
                        ) {
                            Text(
                                "名前変更",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                            )
                        }
                        // 下線
                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .height(3.dp) // 下線の高さを設定
                                .background(Color.White) // 下線の色を設定
                        ) {}


                        // 削除用のボタン
                        Button(
                            onClick = { onDelete() },
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .align(Alignment.Start),
                            colors = ButtonDefaults
                                .buttonColors(
                                    containerColor = Color(0xFF66CC8F)
                                )
                        ) {
                            Text(
                                "削除　　",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                            )
                        }
                        // 下線
                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .height(3.dp) // 下線の高さを設定
                                .background(Color.White) // 下線の色を設定
                        ) {}
                    }
                }
            }
        }
    }
}