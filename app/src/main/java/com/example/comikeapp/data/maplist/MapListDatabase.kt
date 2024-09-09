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
    @ColumnInfo(name = MapListColumns.MAP_NAME) val mapName: String,
    @ColumnInfo(name = MapListColumns.IMAGE_PATH) val imagePath: String
)

@Dao
interface MapListDao {
    @Query("SELECT * FROM maplist")
    fun getAll(): List<MapList>

    @Query("SELECT * FROM mapList WHERE mapId = :mapId")
    fun selectById(mapId: Int): MapList

    @Query("SELECT imagePath FROM mapList WHERE mapId = :mapId")
    fun getImagePath(mapId: Int): String

    @Query("UPDATE maplist SET 名前 = :newName WHERE mapId = :mapId")
    fun updateMapNameById(mapId: Int, newName: String)

    @Insert
    fun insert(mapList: MapList)

    @Query("DELETE FROM maplist WHERE mapId = :mapId")
    fun deleteById(mapId: Int)
}

@Database(entities = [MapList::class], version = 2)
abstract class MapListDatabase : RoomDatabase() {
    abstract fun mapListDao(): MapListDao

}
