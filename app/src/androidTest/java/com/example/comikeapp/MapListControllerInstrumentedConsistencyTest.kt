package com.example.comikeapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import com.example.comikeapp.data.maplist.MapListRepository

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MapListControllerInstrumentedConsistencyTest {
    private val db: MapListRepository
    init {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = MapListRepository(
            MapListDatabaseProvider.getDatabase(appContext).mapListDao()
        )
    }

    @Test
    fun shouldInsertSingleData() {
        val name = "test name"; val  path = "test/path.png"

        db.insertAndGetAll(name, path)

        val tracer = listOf(MapList(-1, name, path, null, null))

        assertEquals(
            idNormalize(tracer),
            idNormalize(db.getAll())
        )
    }

    @Test
    fun shouldInsertMultiple() {
        val tracer = mutableListOf<MapList>()
        for(i in 0..10){
            val name = "test $i"; val  path = "test/path.png"

            tracer.add(MapList(-1, name, path, null, null))
            db.insertAndGetAll(name, path)
        }

        assertEquals(
            idNormalize(tracer),
            idNormalize(db.getAll())
        )
    }

    @Test
    fun shouldDeleteSingle() {
        val name = arrayOf("name1", "name2")
        val path = arrayOf("path1.png", "path2.png")

        // 一つ目の要素を消したいという想定
        db.insertAndGetAll(name[0], path[0])
        db.insertAndGetAll(name[1], path[1])
        val targetIndex = 0
        val id = db.getAll()[targetIndex].mapId
        db.deleteAndGetAll(id)

        // データベース上の１つ目の要素は削除され、２つ目の要素の値が、１つ目として保存されているはず
        val expected = listOf(MapList(-1, name[1], path[1], null, null))

        assertEquals(
            idNormalize(expected),
            idNormalize(db.getAll())
        )
    }

    @Test
    fun shouldRename() {
        val name = arrayOf("name1", "name2")
        val path = arrayOf("path1.png", "path2.png")

        // 一つ目の要素の名前を変更したいという想定
        db.insertAndGetAll(name[0], path[0])
        db.insertAndGetAll(name[1], path[1])
        val targetIndex = 0
        val newName = "new name"
        val id = db.getAll()[targetIndex].mapId
        db.updateAndGetAll(id, newName)

        // 一つ目の名前だけ異なるはず
        val expected = listOf(
            MapList(-1, newName, path[0], null, null),
            MapList(-1, name[1], path[1], null, null)
        )

        assertEquals(
            idNormalize(expected),
            idNormalize(db.getAll())
        )
    }

    // idは要件に含まれていないため、整合性チェックから除外するための関数
    private fun idNormalize(list: List<MapList>): List<MapList>{
        val newList = mutableListOf<MapList>()
        for(d in list){
            newList.add(MapList(0, d.mapName, d.imagePath, d.rawImagePath, d.drawingDataPath))
        }
        return newList
    }
}