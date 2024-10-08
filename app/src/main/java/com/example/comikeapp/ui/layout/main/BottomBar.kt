package com.example.comikeapp.ui.layout.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comikeapp.R

val BottomBarHeightDp = 56.dp
@Composable
fun BottomBar(onChange: (Int) -> Unit) {
    val selectedIndex = remember { mutableStateOf(0) }
    val onClick: (Int) -> Unit = { index ->
        selectedIndex.value = index
        onChange(index)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(BottomBarHeightDp)
            .background(MaterialTheme.colorScheme.primary)
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
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(if (selectedIndex.value == 0) MaterialTheme.colorScheme.background else Color.Transparent)
                        .align(Alignment.BottomCenter)
                )
                Image(painter = painterResource(R.drawable.button_map_view_screen),
                    contentDescription = "BottomBarIcon",
                    Modifier.alpha(if (selectedIndex.value == 0) 1f else 0.5f)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clickable { onClick(1) },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(if (selectedIndex.value == 1) MaterialTheme.colorScheme.background else Color.Transparent)
                        .align(Alignment.BottomCenter)
                )
                Image(painter = painterResource(R.drawable.button_menu_screen),
                    contentDescription = "BottomBarIcon",
                    Modifier.alpha(if (selectedIndex.value == 1) 1f else 0.5f)
                )
            }
        }
    }
}
