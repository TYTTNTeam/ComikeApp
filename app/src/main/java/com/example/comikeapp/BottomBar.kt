package com.example.comikeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(onChange : (Int) ->Unit) {
    val selectedIndex = remember { mutableStateOf(0) }
    // onClickラムダ式を定義
    val onClick: (Int) -> Unit = { index ->
        selectedIndex.value = index
        onChange(index)}
    val customColor = Color(android.graphics.Color.parseColor("#00FF66"))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(customColor)
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center)
        )
        {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clickable { onClick(0) },
                contentAlignment = Alignment.Center
            ) {Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(if (selectedIndex.value == 0) MaterialTheme.colorScheme.background.copy(0.5f) else Color.Transparent)
                    .align(Alignment.BottomCenter)
            )
                Icon(Icons.Filled.Check, contentDescription = "Localized description",
                    modifier = Modifier.size(48.dp))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clickable { onClick(1) },
                contentAlignment = Alignment.Center
            ) {Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(if (selectedIndex.value == 1) MaterialTheme.colorScheme.background.copy(0.5f) else Color.Transparent)
                    .align(Alignment.BottomCenter)
            )
                Icon(Icons.Filled.Edit, contentDescription = "Localized description",
                    modifier = Modifier.size(48.dp))
            }
        }

    }
    }

