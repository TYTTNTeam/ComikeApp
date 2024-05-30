package com.example.comikeapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.ImageType
import com.tom_roush.pdfbox.rendering.PDFRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Composable
fun FilePickerButton() {
    var pickedImageUri by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        if (uri != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val file = convert(uri, context)
                pickedImageUri = BitmapFactory.decodeFile(file?.path)?.asImageBitmap()
            }
        }
    }

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            launcher.launch(intent)
        }
    ) {
        Text("Select")
    }
    pickedImageUri?.let {
        Text(it.toString())
        Image(bitmap = it, contentDescription = "test image")
    }
}

suspend fun convert(uri: Uri, context: Context): File?{
    try{
        PDFBoxResourceLoader.init(context)

        val document = PDDocument.load(context.contentResolver.openInputStream(uri))
        val render = PDFRenderer(document)
        val image = render.renderImage(0, 1f, ImageType.RGB)

        val topDir = context.filesDir
        val mapsDir = File(topDir, "maps")
        mapsDir.mkdir()
        val renderFile = File(mapsDir, "test")

        val fileOut = FileOutputStream(renderFile)
        image.compress(Bitmap.CompressFormat.JPEG, 100, fileOut)
        fileOut.close()

        Log.d("convert", "Successful rendering into $fileOut from $uri")
        return renderFile
    } catch (e: IOException) {
        Log.e("convert", "Exception thrown while rendering file", e)
        return null
    }

}
