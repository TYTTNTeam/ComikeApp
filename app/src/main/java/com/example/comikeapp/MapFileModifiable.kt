package com.example.comikeapp

import android.content.Context
import java.io.File

class MapFileModifiable(context: Context) {
    val targetFIle: File = File(context.filesDir, "maps")
    private var context: Context

    init {
        this.context = context
        targetFIle.mkdir()
    }
}