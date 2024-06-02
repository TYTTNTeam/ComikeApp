package com.example.comikeapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class MapImageRecorder(private val context: Context) {
    private val dir: File = File(context.filesDir, "maps").apply {
        if (!exists()) {
            mkdir()
        }
    }
    private var targetFile: File? = this.dir

    fun render(uri: Uri, fileName: String): File?{
        /*
        uriが正しく読み込めた場合にFile型を返し、そうでない場合はnullを返します。レンダリング中の失敗はエラーが送出されます。
         */
        this.context.contentResolver.openFileDescriptor(uri, "r")?.let { pfd ->
            // PDFの1ページ目のインスタンスを生成
            val renderer = PdfRenderer(pfd)
            val page = renderer.openPage(0)
            // 空のBitmapインスタンスを生成
            val width = page.width
            val height = page.height
            val maxSize = 2400.0
            val bitmapScale: Double = if (width < height) {
                maxSize / height
            } else {
                maxSize / width
            }
            val bitmap = Bitmap.createBitmap(
                (width * bitmapScale).toInt(),
                (height * bitmapScale).toInt(),
                Bitmap.Config.ARGB_8888
            )
            // Pageのメソッドで、Bitmapにレンダリングするよう依頼
            page.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
            )

            page.close()
            pfd.close()

            // 空のファイルのインスタンスを生成
            this.targetFile = File(this.dir, fileName)
            // Bitmapのメソッドで、画像を圧縮しつつ空のファイルに保存するよう依頼
            val fileOut = FileOutputStream(this.targetFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut)
            fileOut.close()

            return this.targetFile
        }
        return null
    }

    fun rollback(): Boolean {
        /*
        成功した場合はtrueを返します。先にレンダリングしていない場合はfalseを返します。
         */
        this.targetFile?.let { file ->
            file.delete()
            return true
        }
        return false
    }

    fun getRecordFile(fileName: String): File?{
        /*
        fileNameが正しい時場合はFile型を返し、そうでない場合はnullを返します。
         */
        val file = File(dir, fileName)
        return if(file.isFile) {
            file
        }else{
            null
        }
    }
}