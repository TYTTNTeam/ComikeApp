package com.example.comikeapp.data.maplist

object MapListDefinition {
    val image = ColumnData(
        name = MapListColumns.IMAGE_PATH,
        uniqueDirectoryName = "maps"
    )

    val rawImage = ColumnData(
        name = MapListColumns.RAW_IMAGE_PATH,
        uniqueDirectoryName = "raw_maps"
    )

    val drawingData = ColumnData(
        name = MapListColumns.DRAWING_DATA_PATH,
        uniqueDirectoryName = "draw_maps"
    )
}
