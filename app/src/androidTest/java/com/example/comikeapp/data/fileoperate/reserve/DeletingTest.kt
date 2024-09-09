package com.example.comikeapp.data.fileoperate.reserve

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.nio.file.Path

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DeletingTest {
    private var appFilesDir = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
    @Before
    fun setUp(){
        appFilesDir = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
    }

    @Test
    fun shouldDeleteAFile() {
        val log = mutableListOf<Boolean>()

        val target = File(appFilesDir, "test.png")
        log.add(target.exists())
        target.createNewFile()
        log.add(target.exists())
        Deleting().access(target.toPath())
        log.add(target.exists())

        assertEquals(
            log,
            listOf(false, true, false)
        )
    }

    @Test
    fun shouldReturnSuccessState() {
        val target = File(appFilesDir, "test.png")
        target.createNewFile()
        val state = Deleting().access(target.toPath())

        assertTrue(state)
    }

    @Test
    fun shouldReturnFailedState(){
        val cannotFindFile = File(appFilesDir, "test.png")

        assertFalse(Deleting().access(cannotFindFile.toPath()))
    }

    @Test
    fun shouldPutAccessedFile(){
        val target = File(appFilesDir, "test.png")
        val log = mutableListOf<Path?>()
        var d: Deleting

        target.createNewFile()

        d = Deleting()
        log.add(d.accessedFile) //まだ削除が実行されていないので、値がないべき。
        d = Deleting()
        d.access(target.toPath())
        log.add(d.accessedFile) //正しい値があるべき。
        d = Deleting()
        d.access(target.toPath())
        log.add(d.accessedFile) //ファイルがないので削除に失敗し、値がないべき。

        assertEquals(
            log,
            mutableListOf(null, target.toPath(), null)
        )
    }
}