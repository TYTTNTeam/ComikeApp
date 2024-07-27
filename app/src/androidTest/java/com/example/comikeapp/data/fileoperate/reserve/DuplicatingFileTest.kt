package com.example.comikeapp.data.fileoperate.reserve


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.comikeapp.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DuplicatingFileTest {

    private var test = InstrumentationRegistry.getInstrumentation().targetContext.filesDir

    @Before
    fun setUp() {
        test = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
    }

    @Test
    fun testAccessSuccessful() {
        // テスト用のディレクトリを作成
        val testDir = File("path/to/testDir")
        if (!testDir.exists()) {
            testDir.mkdirs()
        }

        // ImageBitmap（ここではBitmap）をファイルに保存
        val imageBitmap = BitmapFactory.decodeResource(
            InstrumentationRegistry.getInstrumentation().targetContext.resources,
           // R.drawable.test_image
        )
        val fromFile = File(testDir, "from.png")
        saveImageBitmapToFile(imageBitmap, fromFile)

        // コピー先のファイルを作成
        val targetFile = File(testDir, "target.png")
        targetFile.createNewFile()

        // DuplicatingFile クラスを使用してコピーを実行
        val duplicatingFile = DuplicatingFile(fromFile.toPath())
        val state = duplicatingFile.access(targetFile.toPath())

        // コピーが成功したかどうかを確認
        Assert.assertTrue(state)

        // コピーされたファイルが存在するか確認
        Assert.assertTrue(targetFile.exists())

        // 必要に応じてファイルの内容を確認（省略可能）
        // Assert.assertEquals(fromFile.readBytes(), targetFile.readBytes())
    }


    @Test
    fun testAccessNotFile() {  // 無効なディレクトリパスを指定
        val target = File(test, "test.png")

        // val data = DuplicatingFile(from).access(target.toPath())

        //   Assert.assertFalse()
    }

    class DuplicatingFileTest {

        private lateinit var duplicatingFile: DuplicatingFile

        @Before
        fun setUp() {
            //   duplicatingFile = DuplicatingFile(from)
        }


    }
    fun saveImageBitmapToFile(imageBitmap: Bitmap, file: File): Boolean {
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

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}



