package com.example.comikeapp.data.fileoperate.manager

import android.content.Context
import android.util.Log
import com.example.comikeapp.data.fileoperate.reserve.Accessing
import com.example.comikeapp.data.fileoperate.reserve.Deleting
import com.example.comikeapp.data.fileoperate.reserve.Writing
import java.io.File
import kotlin.io.path.createParentDirectories

class ByFileReserve(
    private val fileType: FileTypeDefinition,
    private val reserve: Accessing,
) {
    fun execute(context: Context, mapUUID: String): Boolean {
        val absolutePath = File(
            context.dataDir,
            fileType.getRelativeDirFromMapUUID(mapUUID)
        ).toPath()

        if (reserve is Writing) {
            absolutePath.createParentDirectories()
        }

        return if (reserve.access(absolutePath)) {
            val parentDirFile = absolutePath.parent.toFile()
            if (reserve is Deleting && parentDirFile.list()?.isEmpty() == true) {
                parentDirFile.delete()
            }
            true
        } else {
            Log.e("ByFileReserve", "Failed to access '${fileType.type}' file.")
            false
        }
    }

}