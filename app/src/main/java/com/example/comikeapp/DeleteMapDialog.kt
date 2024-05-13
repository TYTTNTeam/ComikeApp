package com.example.comikeapp


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun DeleteMapDialog(mapName:String,
                    onYes: () -> Unit,
                    onNo: () -> Unit) {
    Dialog(onDismissRequest = { onYes() }) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        contentAlignment = Alignment.Center) {
        Box(                                //外枠の緑
            modifier = Modifier
                .size(325.dp, 208.dp)
                .background(Color(0xFF66CC8F))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            val roundedShape = RoundedCornerShape(16.dp)
            Box(                            //内側の白
                modifier = Modifier
                    .size(310.dp, 192.dp)
                    .background(Color.White, shape = roundedShape)
            )
            Text(
                text = "本当に消しますか？",
                modifier = Modifier.offset(y = (-50).dp), // 上部に余白を追加
                color = Color.Red,
                fontSize = 24.sp
            )
            Text(
                text = mapName,
                modifier = Modifier.offset(y = (-10).dp),
                color = Color.Black,
                fontSize = 24.sp
            )
                Button(
                    onClick = { onYes() },
                    modifier = Modifier
                        .offset(x = (-70).dp, y = 40.dp),
                        colors = ButtonDefaults
                            .buttonColors(
                                containerColor = Color.Red,),
                                shape = RoundedCornerShape(8.dp)
                    ) {
                    Text(
                        text = "はい",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
                TextButton(
                    onClick = { onNo() },
                    modifier = Modifier.offset(x = 70.dp,y = 40.dp),
                ) {
                    Text("いいえ",
                        style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                        fontSize = 18.sp
                    )

                }
            }
        }
    }
}
