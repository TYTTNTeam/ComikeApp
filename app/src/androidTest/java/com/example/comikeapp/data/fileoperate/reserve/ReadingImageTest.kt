package com.example.comikeapp.data.fileoperate.reserve

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.nio.file.Path

@RunWith(AndroidJUnit4::class)
class ReadingImageTest{
    private var test = InstrumentationRegistry.getInstrumentation().targetContext.filesDir

    @Before
    fun setUp() {
        test = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
    }

    @Test
    fun readingImageTrue(){
        val target = File(test, "AndroidStudio.png")
        val state = ReadingImage().access(target.toPath())

        Assert.assertTrue(state)

    }
    @Test
    fun testAccessInvalidPath() {
        // 存在しないパスを指定
        val target = File("user/main/InvalidPath")
        val targetPath : Path = target.toPath()
        //val data = DuplicatingFile(from).access(targetPath)

        // 画像が読み込まれなかったことを検証
        // Assert.assertFalse()
       // Assert.assertNull(.getData())
    }
    @Test
    fun testReadNonexistentFile() {
        val nonExistentFile = File("non_existent_file.jpg")
        val reader = ReadingImage()
        val result = reader.access(nonExistentFile.toPath())
        assertFalse(result)
    }

}