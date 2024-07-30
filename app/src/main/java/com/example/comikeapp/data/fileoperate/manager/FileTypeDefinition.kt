package com.example.comikeapp.data.fileoperate.manager

data class FileTypeDefinition(
    val type: String,
    val getRelativeDirFromMapUUID: (String) -> String
)
