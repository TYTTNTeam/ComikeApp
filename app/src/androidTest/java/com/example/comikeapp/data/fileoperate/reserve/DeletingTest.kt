package com.example.comikeapp.data.fileoperate.reserve

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DeletingTest {
    @Test
    fun shouldDeleteAFile() {
        val dir = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
        val log = mutableListOf<Boolean>()

        val target = File(dir, "test.png")
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
    fun shouldReturnFailedState(){
        val dir = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
        val cannotFindFile = File(dir, "test.png")

        assertFalse(Deleting().access(cannotFindFile.toPath()))
    }
}