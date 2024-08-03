package com.example.comikeapp.data.fileoperate.reserve

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.data.editorrendering.PathStyle
import com.example.comikeapp.data.viewmodel.editor.DrawingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

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
        val image = createDummyImage().asImageBitmap()
        val viewModel = createEmptyDrawingViewModel()

        val target = File(appContext.dataDir, "test.png")

        val s = SynthesizingMap(image, viewModel)

        assertFalse(target.exists())

        s.access(target.toPath())

        assertTrue(target.exists())

        target.delete()
    }

    @Test
    fun successStateTest() {
        val image = createDummyImage().asImageBitmap()
        val viewModel = createEmptyDrawingViewModel()


        val shouldBeSuccess = SynthesizingMap(image, viewModel)
        val file = File(appContext.dataDir, "test.png")
        assertTrue(shouldBeSuccess.access(file.toPath()))

        val shouldBeFail = SynthesizingMap(image, viewModel)
        val cannotAccessFile = File("/test.png")
        assertFalse(shouldBeFail.access(cannotAccessFile.toPath()))

        file.delete()
        cannotAccessFile.delete()
    }

    @Test
    fun getAccessedFileTest() {
        val image = createDummyImage().asImageBitmap()
        val viewModel = createEmptyDrawingViewModel()


        val shouldBeSuccess = SynthesizingMap(image, viewModel)
        val shouldBeFail = SynthesizingMap(image, viewModel)

        assertNull(shouldBeSuccess.accessedFile)
        val file = File(appContext.dataDir, "test.png")
        shouldBeSuccess.access(file.toPath())
        assertEquals(shouldBeSuccess.accessedFile, file.toPath())

        assertNull(shouldBeFail.accessedFile)
        val cannotAccessFile = File("/test.png")
        shouldBeFail.access(cannotAccessFile.toPath())
        assertNull(shouldBeFail.accessedFile)

        file.delete()
        cannotAccessFile.delete()
    }

    @Test
    fun bitmapEquivalenceTest() {
        val image = createDummyImage().asImageBitmap()
        val viewModel = createEmptyDrawingViewModel()

        val target = File(appContext.dataDir, "test.png")

        val s = SynthesizingMap(image, viewModel)
        s.access(target.toPath())

        val r = ReadingImage()
        r.access(target.toPath())
        val readImage = r.getData()

        val original = IntArray(100 * 100)
        image.readPixels(original, width = 100, height = 100)
        val read = IntArray(100 * 100)
        readImage!!.readPixels(read, width = 100, height = 100)

        assert(original.contentEquals(read))
        assertEquals(read[0], Color.RED)

        target.delete()
    }

    @Test
    fun drawingTest() {
        val image = createDummyImage().asImageBitmap()
        val viewModel = createEmptyDrawingViewModel()

        val p = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, 5000f)
        }

        viewModel.addPath(Pair(p, PathStyle(color = androidx.compose.ui.graphics.Color.White)))

        val target = File(appContext.dataDir, "test.png")

        val s = SynthesizingMap(image, viewModel)
        s.access(target.toPath())

        val r = ReadingImage()
        r.access(target.toPath())
        val readImage = r.getData()

        val read = IntArray(100 * 100)
        readImage!!.readPixels(read, width = 100, height = 100)

        assertEquals(read.first(), Color.WHITE)
        assertEquals(read.last(), Color.RED)
    }

    @Test
    fun drownBitmapEquivalenceTest() {

    }

    private fun createEmptyDrawingViewModel(): DrawingViewModel {
        val viewModel = DrawingViewModel()
        viewModel.setCanvasSizePx(Offset(5000f, 5000f))
        return viewModel
    }

    private fun createDummyImage(): Bitmap {  // 100 × 100の赤い四角い画像を作成
        val width = 1000
        val height = 1200
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.RED
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }
}