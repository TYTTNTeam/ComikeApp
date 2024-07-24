package com.example.comikeapp.data.fileoperate.reserve

import android.util.Log
import java.io.File
import java.nio.file.Path

class Deleting: Accessing() {
    override fun access(absolutePath: Path): Boolean {
        val target = File(absolutePath.toString())
        val success = target.delete()
        if(success) this.accessedFile = target.toPath() else Log.e("Deleting", "Failed accessing: deleting")
        return success
    }
}