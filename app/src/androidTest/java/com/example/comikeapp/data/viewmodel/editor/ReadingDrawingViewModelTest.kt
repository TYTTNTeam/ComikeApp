package com.example.comikeapp.data.viewmodel.editor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.Path
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.fileoperate.reserve.ReadingDrawingViewModel
import com.example.comikeapp.data.fileoperate.reserve.WritingDrawingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ReadingDrawingViewModelTest {

    private lateinit var appContext: Context

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun successStateTest() {
        val originalViewModel = DrawingViewModel()
        originalViewModel.addPath(Pair(Path(), PathStyle()))
        val writing = WritingDrawingViewModel(originalViewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")
        writing.access(file.toPath())

        val willBeSuccess = ReadingDrawingViewModel()
        assertTrue(willBeSuccess.access(file.toPath()))

        val notExistFile = File("/test.dat")

        val willBeFail = ReadingDrawingViewModel()
        assertFalse(willBeFail.access(notExistFile.toPath()))

        file.delete()
        notExistFile.delete()
    }

    @Test
    fun classTypeTest() {
        val originalViewModel = DrawingViewModel()
        originalViewModel.addPath(Pair(Path(), PathStyle()))
        val writing = WritingDrawingViewModel(originalViewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")
        writing.access(file.toPath())

        val reading = ReadingDrawingViewModel()
        reading.access(file.toPath())

        assert(reading.getData() is DrawingViewModel.DrawingViewModelSaveData)

        file.delete()
    }

    @Test
    fun pathStyleDataTest() {
        val originalViewModel = DrawingViewModel()
        originalViewModel.addPath(Pair(Path(), PathStyle()))
        val writing = WritingDrawingViewModel(originalViewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")
        writing.access(file.toPath())

        val reading = ReadingDrawingViewModel()
        reading.access(file.toPath())

        val readViewModel = DrawingViewModel()
        readViewModel.restoreSaveData(reading.getData()!!)

        assertEquals(
            originalViewModel.paths.value!!.first().second,
            readViewModel.paths.value!!.first().second
        )
    }

    @Test
    fun multiDataTest() {
        val originalViewModel = DrawingViewModel()

        val path = Path()
        path.moveTo(1f, 2f)
        path.lineTo(2f, 3f)
        originalViewModel.addPath(Pair(path, PathStyle(alpha = 1f)))

        path.moveTo(3f, 4f)
        path.lineTo(5f, 6f)
        originalViewModel.addPath(Pair(path, PathStyle(alpha = 0.5f)))

        val writing = WritingDrawingViewModel(originalViewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")
        writing.access(file.toPath())

        val reading = ReadingDrawingViewModel()
        reading.access(file.toPath())

        val readViewModel = DrawingViewModel()
        readViewModel.restoreSaveData(reading.getData()!!)

        assertEquals(
            originalViewModel.getSaveData().toString(),
            readViewModel.getSaveData().toString()
        )
    }
}