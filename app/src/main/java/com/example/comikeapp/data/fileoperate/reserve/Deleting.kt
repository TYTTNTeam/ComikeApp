package com.example.comikeapp.data.fileoperate.reserve

import android.util.Log
import java.io.File
import java.io.IOException
import java.nio.file.Path

class Deleting: Accessing() {
    override fun access(absolutePath: Path): Boolean {
        try {
            val target = File(absolutePath.toString())
            target.delete()
            this.accessedFile = target.toPath()
        }catch (e: IOException){
            Log.e("com.example.comikeapp.data.fileoperate.reserve", "例外が発生した。", e)
            return false
        }
        return true
    }
}