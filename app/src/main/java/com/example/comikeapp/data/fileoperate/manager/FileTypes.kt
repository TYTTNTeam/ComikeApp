package com.example.comikeapp.data.fileoperate.manager

object FileTypes {
    val image = FileTypeDefinition(
        type = "image",
        getRelativeDirFromMapUUID = { uuid ->
            "maps/$uuid"
        }
    )

    val rawImage = FileTypeDefinition(
        type = "raw_image",
        getRelativeDirFromMapUUID = { uuid ->
            "editor_data/$uuid/raw_image.png"
        }
    )

    val drawingData = FileTypeDefinition(
        type = "drawing_data",
        getRelativeDirFromMapUUID = { uuid ->
            "editor_data/$uuid/drawing_data.dat"
        }
    )
}
