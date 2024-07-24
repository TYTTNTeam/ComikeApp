package com.example.comikeapp.data.maplist

import android.content.Context

data class ColumnData(
    val name: String,
    val uniqueDirectoryName: String
) {
    fun getAbsolutePath(appContext: Context): String {
        val absolutePath = "${appContext.filesDir}/$uniqueDirectoryName"
        return absolutePath
    }
}
