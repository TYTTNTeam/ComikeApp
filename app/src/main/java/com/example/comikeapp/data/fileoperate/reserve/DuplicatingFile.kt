package com.example.comikeapp.data.fileoperate.reserve

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class DuplicatingFile(private val from : Path) : Writing() { // fromが意図しないアクセスや変更を防ぐ
    override fun access(absolutePath: Path): Boolean {
        return try {
            Files.copy(from, absolutePath, StandardCopyOption.REPLACE_EXISTING)
            // このオプションはPathが被っている場合に上書きするためにある
            true
        } catch (e: IOException) {
            Log.e("DuplicatingFile", "Failed accessing: File copy")
            false
        }
    }
}
fun drawableToBitmap(drawable: Drawable): Bitmap {
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}