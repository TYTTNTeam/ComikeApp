package com.example.comikeapp

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
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
object MapListDatabaseProvider {
    private var instance: MapListDatabase? = null
    fun getDatabase(context: Context): MapListDatabase {
        return instance ?: synchronized(this) {
            val newInstance = Room.databaseBuilder(
                context.applicationContext, MapListDatabase::class.java, "app-database"
            ).fallbackToDestructiveMigration().build()
            instance = newInstance
            newInstance
        }
    }
}

class MapListRepository(private val mapListDao: MapListDao) {

    fun getAll(): List<MapList> {
        return mapListDao.getAll()
    }
    fun insertAndGetAll(mapName: String?, imagePath: String?): List<MapList> {
        val mapList = MapList(mapName = mapName, imagePath = imagePath)
        mapListDao.insert(mapList)
        return mapListDao.getAll()
    }

    fun updateAndGetAll(mapId: Int, newName: String): List<MapList> {
        mapListDao.updateMapNameById(mapId, newName)
        return mapListDao.getAll()
    }

    fun deleteAndGetAll(mapId: Int): List<MapList> {
        mapListDao.deleteById(mapId)
        return mapListDao.getAll()
    }
}
