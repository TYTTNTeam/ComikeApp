package com.example.comikeapp.data.fileoperate.reserve

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ReadingImageTest {

    private lateinit var testDir: File

    @Before
    fun setUp() {
        testDir = File(InstrumentationRegistry.getInstrumentation().targetContext.dataDir, "path/to/testDir")
        if (!testDir.exists()) {
            testDir.mkdirs()
        }
    }

    @Test
    fun testReadingImage() {
        // ダミーのBitmapを生成してファイルに保存
        val imageBitmap = createDummyImage()
        val testFile = File(testDir, "testImage.png")
        saveImageBitmapToFile(imageBitmap, testFile)

        // ReadingImage クラスを使用して画像を読み取る
        val readingImage = ReadingImage()
        val state = readingImage.access(testFile.toPath())

        // 読み取りが成功したかどうかを確認
        Assert.assertTrue(state)

        // 読み取った画像が元の画像と一致するか確認
        val readBitmap = readingImage.getData()?.asAndroidBitmap()
        Assert.assertNotNull(readBitmap)
        Assert.assertTrue(imageBitmapsEqual(imageBitmap, readBitmap!!))
    }

    @Test  // 存在しないディレクトリにファイルを書き込もうとする場合
    fun testSaveImageBitmapToFileIOException() {
        val readingImage = ReadingImage()
        // テストディレクトリを削除して存在しない状態にする
        if (testDir.exists()) {
            testDir.deleteRecursively()
        }
        // 存在しないディレクトリにファイルを保存しようとする
        val testFile = File(testDir, "testImage.png")
        val success = readingImage.access(testFile.toPath())

        // 例外が発生して保存に失敗することを確認
        Assert.assertFalse(success)
    }

    // Bitmap オブジェクトを PNG ファイルとして指定されたファイルに保存
    private fun saveImageBitmapToFile(imageBitmap: Bitmap, file: File): Boolean {
        return try {
            FileOutputStream(file).use { out ->
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
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

    // 画像が完全一致かを確かめてくれている
    private fun imageBitmapsEqual(bmp1: Bitmap, bmp2: Bitmap): Boolean {
        if (bmp1.width != bmp2.width || bmp1.height != bmp2.height) {
            return false
        }
        for (x in 0 until bmp1.width) {
            for (y in 0 until bmp1.height) {
                if (bmp1.getPixel(x, y) != bmp2.getPixel(x, y)) {
                    return false
                }
            }
        }
        return true
    }
}