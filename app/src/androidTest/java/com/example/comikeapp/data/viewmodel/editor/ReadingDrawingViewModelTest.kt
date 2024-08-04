package com.example.comikeapp.data.viewmodel.editor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.geometry.Offset
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
        useEmptyViewModelFile {file, _ ->
            val willBeSuccess = ReadingDrawingViewModel()
            assertTrue(willBeSuccess.access(file.toPath()))

            val notExistFile = File("/test.dat")
            val willBeFail = ReadingDrawingViewModel()
            assertFalse(willBeFail.access(notExistFile.toPath()))
            notExistFile.delete()
        }
    }

    @Test
    fun classTypeTest() {
        useEmptyViewModelFile {file, _ ->
            val reading = ReadingDrawingViewModel()
            reading.access(file.toPath())

            assert(reading.getData() is DrawingViewModel.DrawingViewModelSaveData)
        }
    }

    @Test
    fun pathStyleDataTest() {
        useEmptyViewModelFile { file, original ->
            val reading = ReadingDrawingViewModel()
            reading.access(file.toPath())

            val readViewModel = DrawingViewModel()
            readViewModel.restoreSaveData(reading.getData()!!)

            assertEquals(
                original.paths.value!!.first().second,
                readViewModel.paths.value!!.first().second
            )
        }
    }

    @Test
    fun multiDataTest() {
        useWrittenViewModelFile { file, original ->
            val reading = ReadingDrawingViewModel()
            reading.access(file.toPath())

            val readViewModel = DrawingViewModel()
            readViewModel.restoreSaveData(reading.getData()!!)

            assertEquals(
                original.getSaveData().toString(),
                readViewModel.getSaveData().toString()
            )
        }
    }

    @Test
    fun dataExistTest() {
        useWrittenViewModelFile { file, _ ->
            val reading = ReadingDrawingViewModel()
            reading.access(file.toPath())

            val readViewModel = DrawingViewModel()
            readViewModel.restoreSaveData(reading.getData()!!)

            assert(!readViewModel.paths.value!!.first().first.isEmpty)
        }
    }

    private fun useEmptyViewModelFile(block: (file: File, original: DrawingViewModel) -> Unit) {
        val originalViewModel = DrawingViewModel()
        originalViewModel.addPath(Pair(emptyList(), PathStyle()))
        val writing = WritingDrawingViewModel(originalViewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")
        writing.access(file.toPath())

        block(file, originalViewModel)
        file.delete()
    }

    private fun useWrittenViewModelFile(block: (file: File, original: DrawingViewModel) -> Unit) {
        val originalViewModel = DrawingViewModel()

        val points = mutableListOf<Offset>()
        points.add(Offset(1f, 2f))
        points.add(Offset(11f, 22f))
        originalViewModel.addPath(Pair(points, PathStyle(alpha = 1f)))

        points.add(Offset(31f, 32f))
        points.add(Offset(11f, 22f))
        originalViewModel.addPath(Pair(points, PathStyle(alpha = 0.5f)))

        val writing = WritingDrawingViewModel(originalViewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")
        writing.access(file.toPath())

        block(file, originalViewModel)
        file.delete()
    }
}