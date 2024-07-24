package com.example.comikeapp.data.fileoperate.manager

object FileTypes {
    val image = FileTypeDefinition(
        getAbsolutePath = { uuid ->
            "maps/$uuid"
        }
    )

    val rawImage = FileTypeDefinition(
        getAbsolutePath = { uuid ->
            "editor_data/$uuid/raw_image.png"
        }
    )

    val drawingData = FileTypeDefinition(
        getAbsolutePath = { uuid ->
            "editor_data/$uuid/memo.dvmf"
        }
    )
}
