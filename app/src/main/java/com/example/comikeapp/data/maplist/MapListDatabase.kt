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
    @PrimaryKey(autoGenerate = true) val mapId: Int = 0,
    @ColumnInfo(name = "名前") val mapName: String?,
    @ColumnInfo(name = "画像パス") val imagePath: String?
)
@Dao
interface MapListDao {
    @Query("SELECT * FROM maplist")
    fun getAll(): List<MapList>

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
