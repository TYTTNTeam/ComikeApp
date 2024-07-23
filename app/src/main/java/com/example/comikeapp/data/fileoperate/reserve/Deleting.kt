package com.example.comikeapp.data.fileoperate.reserve

import java.io.File
import java.nio.file.Path

class Deleting: Accessing() {
    override fun access(absolutePath: Path): Boolean {
        val target = File(absolutePath.toString())
        val success = target.delete()
        if(success) this.accessedFile = target.toPath()
        return success
    }
}