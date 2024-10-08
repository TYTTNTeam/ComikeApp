package com.example.comikeapp.data.mapimagefile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class MapImageRecorder(context: Context) : MapImageOperateable(context) {
    private var targetFile: File? = null

    fun render(uri: Uri, fileName: String): File{
        /*
        レンダリング中の失敗はエラーが送出されます。
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
            this.targetFile = File(this.mapImagesDirectory, fileName)
            // Bitmapのメソッドで、画像を圧縮しつつ空のファイルに保存するよう依頼
            val fileOut = FileOutputStream(this.targetFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut)
            fileOut.close()

            return this.targetFile!!
        }
        throw ContentResolverCrashException()
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
}

class ContentResolverCrashException: Exception("ContentResolverがクラッシュしました。Uriが正しくない可能性があります。")
