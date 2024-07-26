package com.example.comikeapp.data.fileoperate.reserve


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class DuplicatingFileTest {

    private var test = InstrumentationRegistry.getInstrumentation().targetContext.filesDir

    @Before
    fun setUp() {
        test = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
    }

    @Test
    fun testAccessSuccessful() { // ファイルがコピーできているかの確認
        val target = File(test, "test.png")
        target.createNewFile()
        val state = DuplicatingFile().access(target.toPath())

        Assert.assertTrue(state)
    }

    @Test
    fun testAccessNotFile() {  // 無効なディレクトリパスを指定
        val target = File(test, "test.png")

        val data = DuplicatingFile().access(target.toPath())

        Assert.assertFalse(data)
    }

    class DuplicatingFileTest {

        private lateinit var duplicatingFile: DuplicatingFile

        @Before
        fun setUp() {
            duplicatingFile = DuplicatingFile()
        }


    }
}