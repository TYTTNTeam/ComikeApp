package com.example.comikeapp



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.comikeapp.ui.theme.ComikeAppTheme

@Composable
fun DialogBox(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var contentSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier = Modifier
            .size(screenWidth)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(                      //外枠の緑
            modifier = Modifier
                .width(325.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            val roundedShape = RoundedCornerShape(16.dp) //白四角の角の丸さ加減
            Box(                            //内側の白
                modifier = Modifier
                    .width(310.dp)
                    .height(IntrinsicSize.Min)
                    .background(MaterialTheme.colorScheme.background, shape = roundedShape)
                    .onGloballyPositioned { coordinates ->
                        contentSize = coordinates.size.toSize()
                    }
            ) {
                content()
            }
        }
    }
}

