package com.example.comikeapp.data.fileoperate.manager

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.fileoperate.reserve.Deleting
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDao
import com.example.comikeapp.data.maplist.MapListDatabase
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ExecutorTest {

    private lateinit var appContext: Context
    private lateinit var database: MapListDatabase
    private lateinit var dao: MapListDao
    private var files = emptyList<File>().toMutableList()

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        database = Room.inMemoryDatabaseBuilder(
            appContext,
            MapListDatabase::class.java
        ).build()
        dao = database.mapListDao()

        // テストのためだけの地図データを作成
        val dir = File(appContext.dataDir, "maps")
        dir.mkdir()

        for (i in 0..1) {
            val file = File(dir, "$i")
            file.createNewFile()

            val d = File(appContext.dataDir, "editor_data/$i")
            d.mkdirs()
            File(d, "raw_image.png").createNewFile()
            File(d, "drawing_data.dat").createNewFile()

            dao.insert(MapList(mapName = "test file $i", imagePath = i.toString()))
            files.add(file)
        }
    }

    @After
    fun tearDown() {
        // Close the database after each test
        database.close()
        // ファイルをすべて取り除く
        files.forEach { f ->
            f.delete()
        }
    }

    @Test
    fun deletingFileTest() {
        val del = ByFileReserve(FileTypes.image, Deleting())
        val uuid = dao.getImagePath(dao.getAll()[0].mapId)

        assertTrue(files[0].exists())
        assertTrue(files[1].exists())

        del.execute(appContext, uuid)

        assertFalse(files[0].exists())
        assertTrue(files[1].exists())
    }

    @Test
    fun outPutSuccessState() {
        val deleter = ByFileReserve(FileTypes.image, Deleting())
        val uuid = dao.getImagePath(dao.getAll()[0].mapId)

        assertTrue(deleter.execute(appContext, uuid))
        assertFalse(deleter.execute(appContext, uuid))
    }

    @Test
    fun deleteRawImageFileTest() {
        val deleter = ByFileReserve(FileTypes.rawImage, Deleting())
        val uuid = dao.getImagePath(dao.getAll()[0].mapId)

        assertTrue(File(appContext.dataDir, "editor_data/0/raw_image.png").exists())

        deleter.execute(appContext, uuid)

        assertFalse(File(appContext.dataDir, "editor_data/0/raw_image.png").exists())
    }

    @Test
    fun createDirectoryTest() {
        dao.insert(
            MapList(
                mapId = -1,
                mapName = "map for writing",
                imagePath = "_1"
            )
        )

        val deleter = ByFileReserve(FileTypes.rawImage, DuplicatingFile())
        val uuid = dao.getImagePath(-1)

        assertFalse(File(appContext.dataDir, "editor_data/_1").exists())

        deleter.execute(appContext, uuid)

        assertTrue(File(appContext.dataDir, "editor_data/_1").exists())
    }

    @Test
    fun deleteDirWhenEmpty() {
        val uuid = dao.getImagePath(dao.getAll()[0].mapId)

        assertTrue(File(appContext.dataDir, "editor_data/$uuid").exists())

        val a = ByFileReserve(FileTypes.rawImage, Deleting())
        a.execute(appContext, uuid)

        val b = ByFileReserve(FileTypes.rawImage, DuplicatingFile())
        b.execute(appContext, uuid)

        assertTrue(File(appContext.dataDir, "editor_data/$uuid").exists())

        val c = ByFileReserve(FileTypes.rawImage, Deleting())
        c.execute(appContext, uuid)

        val d = ByFileReserve(FileTypes.drawingData, Deleting())
        d.execute(appContext, uuid)

        assertFalse(File(appContext.dataDir, "editor_data/$uuid").exists())
    }
}