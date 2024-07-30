package com.example.comikeapp.data.fileoperate.reserve

import android.util.Log
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class DuplicatingFile(private val from: Path) : Writing() { // fromが意図しないアクセスや変更を防ぐ
    override fun access(absolutePath: Path): Boolean {
        return try {
            Files.copy(from, absolutePath, StandardCopyOption.REPLACE_EXISTING)
            // このオプションはPathが被っている場合に上書きするためにある
            true
        } catch (e: IOException) {
            Log.e("data.fileOperate.reserve", "Duplicating:Failed accessing. \n Failed to copy files.",e)
            false
        }
    }
}