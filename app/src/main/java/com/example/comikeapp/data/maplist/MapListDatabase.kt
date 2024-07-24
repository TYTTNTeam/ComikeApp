package com.example.comikeapp.data.maplist

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase



@Entity
data class MapList(
    @PrimaryKey(autoGenerate  = true) @ColumnInfo(name = MapListColumns.MAP_ID) val mapId: Int = 0,
    @ColumnInfo(name = MapListColumns.MAP_NAME) val mapName: String?,
    @ColumnInfo(name = MapListColumns.IMAGE_PATH) val imagePath: String?,
    @ColumnInfo(name = MapListColumns.RAW_IMAGE_PATH) val rawImagePath: String?,
    @ColumnInfo(name = MapListColumns.DRAWING_DATA_PATH) val drawingDataPath: String?
)

@Dao
interface MapListDao {
    @Query("SELECT * FROM maplist")
    fun getAll(): List<MapList>

    @Query("SELECT * FROM mapList WHERE mapId = :mapId")
    fun selectById(mapId: Int): MapList

    @Query("UPDATE maplist SET 名前 = :newName WHERE mapId = :mapId")
    fun updateMapNameById(mapId: Int, newName: String)

    @Query("UPDATE maplist SET  rawImagePath = :rawImage , drawingDataPath = :drawingData  WHERE mapId = :mapId")
    fun updateRawImageAndDrawingData(mapId: Int, rawImage: String?, drawingData: String?)

    @Query("UPDATE maplist SET imagePath = :image , drawingDataPath = :drawingData WHERE  mapId = :mapId")
    fun updateImageAndDrawingData(mapId: Int, image: String?, drawingData: String?)

    @Insert
    fun insert(mapList: MapList)

    @Query("DELETE FROM maplist WHERE mapId = :mapId")
    fun deleteById(mapId: Int)
}

@Database(entities = [MapList::class], version = 2)
abstract class MapListDatabase : RoomDatabase() {
    abstract fun mapListDao(): MapListDao

}
