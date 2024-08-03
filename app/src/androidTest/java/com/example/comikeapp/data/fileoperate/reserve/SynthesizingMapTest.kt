package com.example.comikeapp.data.fileoperate.reserve

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.asImageBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SynthesizingMapTest {
    private lateinit var appContext: Context

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun fileExistsTest() {
        val target = File(appContext.dataDir, "test.png")

        val image = createDummyImage()
        val viewModel = DrawingViewModel()

        val s = SynthesizingMap(image.asImageBitmap(), viewModel, appContext)

        assertFalse(target.exists())

        s.access(target.toPath())

        assertTrue(target.exists())

        target.delete()
    }

    private fun createDummyImage(): Bitmap {  // 100 × 100の赤い四角い画像を作成
        val width = 100
        val height = 100
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.RED
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }
}