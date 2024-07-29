package com.example.comikeapp.data.fileoperate.manager

import com.example.comikeapp.data.fileoperate.reserve.Writing
import java.nio.file.Path

class DuplicatingFile : Writing() {
    override fun access(absolutePath: Path): Boolean {
        return true
    }

}
