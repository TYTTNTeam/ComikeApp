package com.example.comikeapp.data.fileoperate.reserve

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.nio.file.NoSuchFileException
import java.nio.file.Path


internal class ReadingImage : Reading() {
    private var result: ImageBitmap? = null

    override fun getData(): ImageBitmap? {
        return result
    }

    override fun access(absolutePath: Path): Boolean {
        val file = File(absolutePath.toString())
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)

        if (!file.exists()) { // ファイルの存在確認
            val e = NoSuchFileException(file.absolutePath.toString())
            Log.e(
                "data.fileOperate.reserve",
                "ReadingImage: Failed accessing. \nFile does not exist.",
                e
            )
            accessedFile = null
            return false
        }

        if (!file.canRead()) { // ファイルの読み取り可能確認
            val e = SecurityException("File is not readable: " + file.absolutePath)
            Log.e(
                "data.fileOperate.reserve",
                "ReadingImage: Failed accessing. \nFile is not readable.",
                e
            )
            accessedFile = null
            return false
        }

        if (bitmap != null) { // 元画像と読み取った画像が同じかの確認
            this.result = bitmap.asImageBitmap()
            accessedFile = absolutePath
            return true
        } else {
            val e = IllegalArgumentException("Decoding image from file failed: " + file.absolutePath)
            Log.e(
                "data.fileOperate.reserve",
                "ReadingImage: Failed accessing. \nDecoding image from file.",
                e
            )
            accessedFile = null
            return false
        }
    }
}