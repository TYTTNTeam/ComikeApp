package com.example.comikeapp.ui.layout.menu

import android.content.Context
import android.net.Uri
import com.example.comikeapp.data.mapimagefile.MapImageCleaner
import com.example.comikeapp.data.mapimagefile.MapImageRecorder
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
        onComplete: (List<MapList>) -> Unit
    ){
        val mir = MapImageRecorder(appContext)
        val renderImage = scope.async{
            val uuid = UUID.randomUUID()
            val newMap = mir.render(pdf, uuid.toString())
            newMap
        }

        val imageFile = renderImage.await()
        val name = this.confirmName.await()
        if(name == null){
            mir.rollback()
        }else {
            val db = MapListRepository(MapListDatabaseProvider.getDatabase(appContext).mapListDao())
            val list = db.insertAndGetAll(name, imageFile.path)

            onComplete(list)

            scope.launch {
                val paths = list.map { it.imagePath!! }
                val cleaner = MapImageCleaner(appContext)
                cleaner.clean(paths)
            }
        }
    }

    fun confirmName(name: String, confirm: Boolean){
        if(confirm){
            this.confirmName.complete(name)
        }else{
            this.confirmName.complete(null)
        }
    }
}
