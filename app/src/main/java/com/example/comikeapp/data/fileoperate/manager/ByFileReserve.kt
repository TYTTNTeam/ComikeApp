package com.example.comikeapp.data.fileoperate.manager

import android.content.Context
import android.util.Log
import com.example.comikeapp.data.fileoperate.reserve.Accessing
import java.io.File

class ByFileReserve(
    private val fileType: FileTypeDefinition,
    private val reserve: Accessing,
) {
    fun execute(context: Context, mapUUID: String): Boolean {
        return if (
            reserve.access(
                File(
                    context.filesDir,
                    fileType.getRelativeDirFromMapUUID(mapUUID)
                ).toPath()
            )
        ) {
            true
        } else {
            Log.e("ByFileReserve", "ファイル'${fileType.type}'へのアクセスが失敗。")
            false
        }
    }

}