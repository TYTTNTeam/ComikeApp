package com.example.comikeapp

import android.content.Context
import java.io.File

open class MapImageOperateable(protected val context: Context) {
    // コンストラクタで地図画像を配置する場所を定義
    val mapImagesDirectory: File = File(context.filesDir, "maps").apply {
        if (!exists()) {
            mkdir()
        }
    }

    // 画像ファイルを削除するメソッド
    fun deleteImageFile(imageName: String) {
        val file = File(mapImagesDirectory, imageName)
        if (file.exists()) {
            file.delete()
        }
    }
}