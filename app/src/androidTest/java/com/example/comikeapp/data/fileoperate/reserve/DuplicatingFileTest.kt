package com.example.comikeapp.data.fileoperate.reserve

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
class DuplicatingFileTest {

    private var test = InstrumentationRegistry.getInstrumentation().targetContext.dataDir

    @Before
    fun setUp() {
        test = InstrumentationRegistry.getInstrumentation().targetContext.dataDir
    }

    @Test
    fun testAccessSuccessful(){
        // テスト用のディレクトリを作成
        val testDir = File(InstrumentationRegistry.getInstrumentation().targetContext.dataDir,"path/to/testDir")
        if (!testDir.exists()) {
            testDir.mkdirs()
        }

        // ダミーのBitmapを生成してファイルに保存
        val imageBitmap = createDummyImage()
        val fromFile = File(testDir, "from.png")
        saveImageBitmapToFile(imageBitmap, fromFile)

        // コピー先のファイルを作成
        val targetFile = File(testDir, "target.png")
        Assert.assertFalse(targetFile.exists())

        // DuplicatingFile クラスを使用してコピーを実行
        val duplicatingFile = DuplicatingFile(fromFile.toPath())
        val state = duplicatingFile.access(targetFile.toPath())

        // コピーが成功したかどうかを確認
        Assert.assertTrue(state)

        // コピーされたファイルが存在するか確認
        Assert.assertTrue(targetFile.exists())

        // 必要に応じてファイルの内容を確認（省略可能）
        val tmp = fromFile.readBytes()
        val tmp2 = targetFile.readBytes()
        Assert.assertTrue(tmp.contentEquals(tmp2))
    }

    @Test
    fun testAccessNotFile() {  // 無効なディレクトリパスを指定
        val target = File(test, "test.png")

        val state = DuplicatingFile(target.toPath()).access(target.toPath())

        Assert.assertFalse(state)
    }

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
}