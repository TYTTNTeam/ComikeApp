package com.example.comikeapp

import android.graphics.LightingColorFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.comikeapp.ui.theme.ComikeAppTheme

@Composable
fun ChangeMapNameDialog(mapName:String,
                        onYes: (String) -> Unit,
                        onNo: () -> Unit){

    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var bottonEnalsed by remember {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = { onNo() }) {
        DialogBox {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "名前の変更",
                    modifier = Modifier.padding(start = 10.dp,top = 10.dp),
                    fontSize = 32.sp
                )
                Text(
                    text = mapName,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 10.dp),
                )
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                        bottonEnalsed = it.text.isNotEmpty()
                    },
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                    textStyle = TextStyle(fontSize = 24.sp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "新しい名前",
                            style = TextStyle(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
                            fontSize = 24.sp
                        )
                    },

                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onYes(text.text) },
                        enabled = bottonEnalsed,
                        modifier = Modifier.padding(start = 30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "決定",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                    TextButton(
                        onClick = { onNo() },
                        modifier = Modifier.padding(vertical = 10.dp,horizontal = 15.dp)
                    ) {
                        Text(
                            "キャンセル",
                            style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true, widthDp = 320, heightDp = 1000)
@Composable
fun PreviewChangeMapNameDialog() {
    ComikeAppTheme(darkTheme = false) {
        ChangeMapNameDialog(mapName = "地図1",
            onYes = { /* 確認ボタンが押された時の動作 */ },
            onNo = { /* キャンセルボタンが押された時の動作 */ })
    }
}