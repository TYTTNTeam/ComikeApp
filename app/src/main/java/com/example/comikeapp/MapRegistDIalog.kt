package com.example.comikeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapRegistDialog(
    pdfsName: String, onYes: (String) -> Unit, onNo: () -> Unit
) {

    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var buttonPenalised by remember {
        mutableStateOf(false)
    }
    DialogBox(onNo = onNo) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 0.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 140.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = pdfsName,
                        modifier = Modifier.padding(start = 0.dp, top = 10.dp),
                        fontSize = 20.sp,
                        softWrap = true,

                        )
                }
                Box(modifier = Modifier
                    .padding(end = 30.dp)
                    .clickable {
                        //もう一度ファイルを選択ボタンを押した際の処理を入れてください
                    }) {
                    Image(
                        painter = painterResource(R.drawable.buttonselectagain),
                        contentDescription = "リソースIDに対応する画像表示"
                    )
                }

            }
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    buttonPenalised = it.text.isNotEmpty()
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(0.2f),
                    focusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(0.2f),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                textStyle = TextStyle(fontSize = 24.sp),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "地図の名前",
                        style = TextStyle(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
                        fontSize = 24.sp
                    )
                },
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { onYes(text.text) },
                    enabled = buttonPenalised,
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
                    onClick = onNo,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
                ) {
                    Text(
                        "キャンセル",
                        style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}
