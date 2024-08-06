package com.example.comikeapp.ui.layout.menu

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.comikeapp.data.fileoperate.manager.ByFileReserve
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
import java.util.UUID

class MapRegistrationSequencer {
    private val confirmName = CompletableDeferred<String?>()

    suspend fun register(
        scope: CoroutineScope,
        appContext: Context,
        pdf: Uri,
        onComplete: (List<MapList>?) -> Unit
    ) {
        val mapUUID = UUID.randomUUID().toString()
        val convertingImage = ConvertingImage(pdf, appContext)
        val byFileReserve = ByFileReserve(FileTypes.rawImage, convertingImage)

        val imageFilePath: String? = try {
            scope.async {
                val result = byFileReserve.execute(appContext, mapUUID)
                if (result) {
                    convertingImage.accessedFile?.toFile()?.absolutePath
                } else {
                    null
                }
            }.await()
        } catch (e: Exception) {
            Log.e("MapRegistrationSequencer", "Failed during image file conversion.\nError: ${e.message}", e)
            null
        }

        val name = this.confirmName.await()
        if (name == null) {
            val cleaner = ByFileReserve(FileTypes.rawImage, Deleting())
            cleaner.execute(appContext, mapUUID)
        } else {
            if (imageFilePath == null) {
                val errorMessage = "MapRegistrationSequencer: Failed to create image file from PDF."
                Log.e("MapRegistrationSequencer", errorMessage)
                throw Exception(errorMessage)
            } else {
                val db = MapListRepository(MapListDatabaseProvider.getDatabase(appContext).mapListDao())
                val list: List<MapList>
                try {
                    list = db.insertAndGetAll(name, imageFilePath)
                } catch (e: Exception) {
                    Log.e("MapRegistrationSequencer", "Failed to insert and get all map lists.\nError: ${e.message}", e)
                    throw e
                }

                onComplete(list)

                scope.launch {
                    val paths = list.mapNotNull { it.imagePath }
                    val cleaner = MapImageCleaner(appContext)
                    cleaner.clean(paths)
                }
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
