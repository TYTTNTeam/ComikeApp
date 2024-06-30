package com.example.comikeapp

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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
