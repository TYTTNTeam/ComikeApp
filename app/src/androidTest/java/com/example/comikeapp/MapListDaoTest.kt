package com.example.comikeapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDao
import com.example.comikeapp.data.maplist.MapListDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    fun testInsertAndGetAll() {
        val mapList = MapList(
            mapName = "Test Map",
            imagePath = "path/to/image",
            rawImagePath = "path/to/raw_image",
            drawingDataPath = "path/to/drawing_data"
        )
        // Insert and retrieve the ID
        mapListDao.insert(mapList)

        // Retrieve all maps and assert
        val maps = mapListDao.getAll()
        assertEquals(1, maps.size)
        assertEquals("Test Map", maps[0].mapName)
    }

    @Test
    fun testUpdateMapNameById() {
        val mapList = MapList(
            mapName = "Test Map",
            imagePath = "path/to/image",
            rawImagePath = "path/to/raw_image",
            drawingDataPath = "path/to/drawing_data"
        )
        mapListDao.insert(mapList)
        val mapId = mapListDao.getAll()[0].mapId

        mapListDao.updateMapNameById(mapId, "Updated Map")

        val updatedMap = mapListDao.getAll().find { it.mapId == mapId }
        assertNotNull(updatedMap)
        assertEquals("Updated Map", updatedMap?.mapName)
    }

    @Test
    fun testUpdateRawImageAndDrawingData() {
        val mapList = MapList(
            mapName = "Test Map",
            imagePath = "path/to/image",
            rawImagePath = "path/to/raw_image",
            drawingDataPath = "path/to/drawing_data"
        )
        mapListDao.insert(mapList)
        val mapId = mapListDao.getAll()[0].mapId

        mapListDao.updateRawImageAndDrawingData(mapId, "new_raw_image", "new_drawing_data")

        val updatedMap = mapListDao.getAll().find { it.mapId == mapId }
        assertNotNull(updatedMap)
        assertEquals("new_raw_image", updatedMap?.rawImagePath)
        assertEquals("new_drawing_data", updatedMap?.drawingDataPath)
    }

    @Test
    fun testUpdateImageAndDrawingData() {
        val mapList = MapList(
            mapName = "Test Map",
            imagePath = "path/to/image",
            rawImagePath = "path/to/raw_image",
            drawingDataPath = "path/to/drawing_data"
        )
        mapListDao.insert(mapList)
        val mapId = mapListDao.getAll()[0].mapId

        mapListDao.updateImageAndDrawingData(mapId, "new_image", "new_drawing_data")

        val updatedMap = mapListDao.getAll().find { it.mapId == mapId }
        assertNotNull(updatedMap)
        assertEquals("new_image", updatedMap?.imagePath)
        assertEquals("new_drawing_data", updatedMap?.drawingDataPath)
    }

    @Test
    fun testDeleteById() {
        val mapList = MapList(
            mapName = "Test Map",
            imagePath = "path/to/image",
            rawImagePath = "path/to/raw_image",
            drawingDataPath = "path/to/drawing_data"
        )
        mapListDao.insert(mapList)
        val mapId = mapListDao.getAll()[0].mapId

        mapListDao.deleteById(mapId)

        val deletedMap = mapListDao.getAll().find { it.mapId == mapId }
        assertEquals(null, deletedMap)
    }

    @Test
    fun testSelectForMemoEditor(){
        val mapList = MapList(
            mapName = "Incorrect map",
            imagePath = "path/to/image",
            rawImagePath = "path/to/raw_image",
            drawingDataPath = "path/to/drawing_data"
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
}
