package com.example.comikeapp.data.mapimagefile

import android.content.Context
import java.io.File

open class MapImageOperateable(protected val context: Context) {
    // コンストラクタで地図画像を配置する場所を定義
    val mapImagesDirectory: File = File(context.filesDir, "maps").apply {
        if (!exists()) {
            mkdir()
        }
    }
}