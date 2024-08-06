package com.example.comikeapp.ui.layout.menu

import android.content.Context
import android.net.Uri
import com.example.comikeapp.data.fileoperate.manager.ByFileReserve
import com.example.comikeapp.data.fileoperate.manager.FileTypeDefinition
import com.example.comikeapp.data.fileoperate.manager.FileTypes
import com.example.comikeapp.data.fileoperate.reserve.ConvertingImage
import com.example.comikeapp.data.fileoperate.reserve.Deleting
import com.example.comikeapp.data.mapimagefile.MapImageCleaner
import com.example.comikeapp.data.maplist.MapList
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import com.example.comikeapp.data.maplist.MapListRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class MapRegistrationSequencer {
    private val confirmName = CompletableDeferred<String?>()

    suspend fun register(
        scope: CoroutineScope,
        appContext: Context,
        pdf: Uri,
        onComplete: (List<MapList>) -> Unit
    ) {
        val mapUUID = UUID.randomUUID().toString()
        val convertingImage = ConvertingImage(pdf, appContext)

        val imageFilePath = scope.async {
            val byFileReserve = ByFileReserve(FileTypes.rawImage, convertingImage)
            val result = byFileReserve.execute(appContext, mapUUID)
            if (result) {
                convertingImage.accessedFile?.toFile()?.absolutePath
            } else {
                null
            }
        }.await()
        if (imageFilePath == null){
            throw IOException("MapRegistrationSequencer: Failed to create image file from PDF.")
        }

        val name = this.confirmName.await()
        if (name == null) {
            val cleaner = ByFileReserve(FileTypes.rawImage, Deleting())
            cleaner.execute(appContext, mapUUID)
        } else {
            val db = MapListRepository(MapListDatabaseProvider.getDatabase(appContext).mapListDao())
            val list = db.insertAndGetAll(name, imageFilePath)

            onComplete(list)

            scope.launch {
                val paths = list.map { it.imagePath }
                val cleaner = MapImageCleaner(appContext)
                cleaner.clean(paths)
            }
        }
    }

    fun confirmName(name: String, confirm: Boolean) {
        if (confirm) {
            this.confirmName.complete(name)
        } else {
            this.confirmName.complete(null)
        }
    }
}
