package com.example.comikeapp.data.fileoperate.reserve

import java.nio.file.Path


abstract class Accessing {
    var accessedFile: Path? = null
    abstract fun access(absolutePath: Path): Boolean
}