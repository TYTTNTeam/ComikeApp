package com.example.comikeapp.ui.layout.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.comikeapp.R
import com.example.comikeapp.data.fileoperate.manager.ByFileReserve
import com.example.comikeapp.data.fileoperate.manager.FileTypes
import com.example.comikeapp.data.fileoperate.reserve.ReadingImage
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import com.example.comikeapp.ui.theme.ComikeAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Preview
@Composable
fun DataSynchronizerPreview() {
    val appContext = LocalContext.current
    val db = MapListDatabaseProvider.getDatabase(appContext).mapListDao()

    var readImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    var saving by remember { mutableStateOf(false) }

    var mapId by remember { mutableStateOf<Int?>(null) }

    val uuid = "test-uuid"

    LaunchedEffect(key1 = Unit) {
        if (imageBitmap == null) {
            imageBitmap = vectorDrawableToImageBitmap(R.drawable.button_pen_change, appContext)
        }

        withContext(Dispatchers.IO) {
            db.insert(MapList(mapName = "test map name", imagePath = uuid))

            val imageFile =
                File(appContext.dataDir, FileTypes.image.getRelativeDirFromMapUUID(uuid))
            imageFile.parentFile!!.mkdirs()

            FileOutputStream(imageFile).use { out ->
                imageBitmap!!.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }

        mapId = async(Dispatchers.IO) { db.getAll().last().mapId }.await()
    }

    LaunchedEffect(key1 = saving) {
        if (saving) {
            val r = ReadingImage()

            ByFileReserve(
                FileTypes.image,
                r
            ).execute(appContext, uuid)

            readImage = r.getData()

            saving = false
        }
    }

    ComikeAppTheme {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            mapId?.let {
                MemoEditor(
                    modifier = Modifier.size(350.dp),
                    mapId = it,
                    onClose = { saving = true }
                )
            }
            readImage?.let {
                Image(
                    modifier = Modifier
                        .size(350.dp),
                    bitmap = it,
                    contentDescription = ""
                )
            }
        }
    }
}

private fun vectorDrawableToImageBitmap(
    @DrawableRes drawableRes: Int,
    context: Context
): ImageBitmap {
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    val bitmap = Bitmap.createBitmap(
        1000,
        1300,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        color = android.graphics.Color.YELLOW
    }
    canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap.asImageBitmap()
}
