package com.example.comikeapp.data.fileoperate.manager

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.fileoperate.reserve.Deleting
import com.example.comikeapp.data.fileoperate.reserve.DuplicatingFile
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDao
import com.example.comikeapp.data.maplist.MapListDatabase
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
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

        MapListDatabaseProvider.setInstanceForTest(
            Room.inMemoryDatabaseBuilder(
                appContext,
                MapListDatabase::class.java
            ).build()
        )
        database = MapListDatabaseProvider.getDatabase(appContext)
        dao = database.mapListDao()

        // テストのためだけの地図データを作成
        val dir = File(appContext.filesDir, "maps")
        dir.mkdir()

        for (i in 0..1) {
            val file = File(dir, "$i")
            file.createNewFile()

            val d = File(appContext.filesDir, "editor_data/$i")
            d.mkdirs()
            File(d, "raw_image.png").createNewFile()

            dao.insert(MapList(mapName = "test file $i", imagePath = file.absolutePath))
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
        val deleter = Executor(
            mapId = dao.getAll()[0].mapId,
            reserves = listOf(
                ByFileReserve(FileTypes.image, Deleting())
            )
        )

        assertTrue(files[0].exists())
        assertTrue(files[1].exists())

        deleter.execute(appContext)

        assertFalse(files[0].exists())
        assertTrue(files[1].exists())
    }

    @Test
    fun outPutSuccessState() {
        val deleter = Executor(
            mapId = dao.getAll()[0].mapId,
            reserves = listOf(
                ByFileReserve(FileTypes.image, Deleting())
            )
        )

        assertTrue(deleter.execute(appContext))
        assertFalse(deleter.execute(appContext))
    }

    @Test
    fun deleteRawImageFileTest() {
        val deleter = Executor(
            mapId = dao.getAll()[0].mapId,
            reserves = listOf(
                ByFileReserve(FileTypes.rawImage, Deleting())
            )
        )

        assertTrue(File(appContext.filesDir, "editor_data/0/raw_image.png").exists())

        deleter.execute(appContext)

        assertFalse(File(appContext.filesDir, "editor_data/0/raw_image.png").exists())
    }

    @Test
    fun createDirectoryTest(){
        dao.insert(MapList(
            mapId = -1,
            mapName = "map for writing",
            imagePath = appContext.filesDir.toString() + "maps/_1"
        ))

        val deleter = Executor(
            mapId = dao.getAll()[0].mapId,
            reserves = listOf(
                ByFileReserve(FileTypes.rawImage, DuplicatingFile())
            )
        )

        assertFalse(File(appContext.filesDir,  "editor_data/_1").exists())

        deleter.execute(appContext)

        assertTrue(File(appContext.filesDir,  "editor_data/_1").exists())
    }
}