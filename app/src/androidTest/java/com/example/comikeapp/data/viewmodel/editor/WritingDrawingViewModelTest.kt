package com.example.comikeapp.data.viewmodel.editor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.editorrendering.PathStyle
import com.example.comikeapp.data.fileoperate.reserve.WritingDrawingViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class WritingDrawingViewModelTest {

    private lateinit var appContext: Context

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun fileExistsTest() {
        val viewModel = DrawingViewModel()
        viewModel.addPath(Pair(emptyList(), PathStyle()))
        val writing = WritingDrawingViewModel(viewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")

        assertFalse(file.exists())

        writing.access(file.toPath())

        assertTrue(file.exists())

        file.delete()
    }

    @Test
    fun successStateTest() {
        val viewModel = DrawingViewModel()
        viewModel.addPath(Pair(emptyList(), PathStyle()))

        val willBeFail = WritingDrawingViewModel(viewModel.getSaveData())
        val notExistFile = File("/test.dat")
        assertFalse(willBeFail.access(notExistFile.toPath()))

        val willBeSuccess = WritingDrawingViewModel(viewModel.getSaveData())
        val file = File(appContext.dataDir, "test.dat")
        assertTrue(willBeSuccess.access(file.toPath()))

        file.delete()
        notExistFile.delete()
    }
}