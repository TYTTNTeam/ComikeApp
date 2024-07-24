package com.example.comikeapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDao
import com.example.comikeapp.data.maplist.MapListDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapListDaoTest {

    private lateinit var database: MapListDatabase
    private lateinit var mapListDao: MapListDao

    @Before
    fun setUp() {
        // In-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MapListDatabase::class.java
        ).build()
        mapListDao = database.mapListDao()
    }

    @After
    fun tearDown() {
        // Close the database after each test
        database.close()
    }

    @Test
    fun testSelectOneRaw(){
        val mapList = MapList(
            mapName = "Incorrect map",
            imagePath = "path/to/image",
        )
        mapListDao.insert(mapList)
        mapListDao.insert(mapList.copy(mapName = "Nice map"))

        val all = mapListDao.getAll()
        assertEquals(all.size, 2)

        var map = mapListDao.selectById(all[0].mapId)
        assertEquals("Incorrect map", map.mapName)
        map = mapListDao.selectById(all[1].mapId)
        assertEquals("Nice map", map.mapName)
    }

    @Test
    fun testGetMapUUID() {
        val mapList = MapList(
            mapName = "Incorrect map",
            imagePath = "path/to/image",
        )
        mapListDao.insert(mapList)
        val list = mapListDao.getAll()
        val uuid = mapListDao.getImagePath(list[0].mapId)

        assertEquals("path/to/image", uuid)
    }
}
