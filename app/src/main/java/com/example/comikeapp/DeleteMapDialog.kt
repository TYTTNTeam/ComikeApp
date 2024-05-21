package com.example.comikeapp


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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DeleteMapDialog(mapName:String,
                    onYes: () -> Unit,
                    onNo: () -> Unit){
    Dialog(onDismissRequest = { onNo() }) {
        DialogBox (onNo = onNo){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = "本当に消しますか？",
                    modifier = Modifier.padding(top = 10.dp),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 24.sp
                )
                Text(
                    text = mapName,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 24.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onYes() },
                        modifier = Modifier.padding(horizontal = 15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "はい",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                    TextButton(
                        onClick = { onNo() },
                        modifier = Modifier.padding(vertical = 10.dp,horizontal = 15.dp)
                    ) {
                        Text(
                            "いいえ",
                            style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}