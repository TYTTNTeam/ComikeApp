package com.example.comikeapp.data.fileoperate.reserve

import android.util.Log
import java.io.File
import java.nio.file.NoSuchFileException
import java.nio.file.Path

class Deleting: Accessing() {
    override fun access(absolutePath: Path): Boolean {
        val target = File(absolutePath.toString())
        val success = target.delete()
        if (success) {
            this.accessedFile = target.toPath()
        } else {
            val e = NoSuchFileException(absolutePath.toString())
            Log.e(
                "data.fileOperate.reserve",
                "DeletingFile: Failed accessing. \nFile does not exist or cannot be deleted.",
                e
            )
        }
        return success
    }
}