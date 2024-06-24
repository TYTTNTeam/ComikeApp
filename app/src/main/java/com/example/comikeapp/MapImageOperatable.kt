package com.example.comikeapp

import android.content.Context
import java.io.File

open class MapImageOperateable(protected val context: Context) {
    // コンストラクタで地図画像を配置する場所を定義
    protected val dir: File = File(context.filesDir, "maps").apply {
        if (!exists()) {
            mkdir()
        }
    }
}