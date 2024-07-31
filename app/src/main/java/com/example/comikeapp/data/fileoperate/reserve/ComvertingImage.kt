package com.example.comikeapp.data.fileoperate.reserve

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path

class ConvertingImage(
    private val pdfFile: Uri,
    private val context: Context
) : Accessing() {

    override fun access(absolutePath: Path): Boolean {
        return try {
            context.contentResolver.openFileDescriptor(pdfFile, "r")?.use { pfd ->
                val renderer = PdfRenderer(pfd)
                val page = renderer.openPage(0)

                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()

                val file = File(absolutePath.toUri())
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                renderer.close()

                accessedFile = absolutePath
                true
            } ?: run {
                Log.e("ConvertingImage", "Failed accessing.\nError: PDF file descriptor is null.")
                false
            }
        } catch (e: Exception) {
            Log.e("ConvertingImage", "Failed accessing.\nError: ${e.message}", e)
            accessedFile = null
            false
        }
    }
}
