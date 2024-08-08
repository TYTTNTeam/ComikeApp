package com.example.comikeapp.data.fileoperate.manager

object FileTypes {
    val image = FileTypeDefinition(
        type = "image",
        getRelativeDirFromMapUUID = { uuid ->
            "maps/$uuid"
        }
    )

    private const val EDITOR_DATA_DIR = "editor_data"

    val rawImage = FileTypeDefinition(
        type = "raw_image",
        getRelativeDirFromMapUUID = { uuid ->
            "$EDITOR_DATA_DIR/$uuid/raw_image.png"
        }
    )

    val drawingData = FileTypeDefinition(
        type = "drawing_data",
        getRelativeDirFromMapUUID = { uuid ->
            "$EDITOR_DATA_DIR/$uuid/drawing_data.dat"
        }
    )
}
