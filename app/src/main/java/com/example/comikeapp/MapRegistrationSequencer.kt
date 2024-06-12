package com.example.comikeapp

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
class MapRegistrationSequencer {
    private val confirmName = CompletableDeferred<String?>()

    suspend fun register(
        scope: CoroutineScope,
        appContext: Context,
        pdf: Uri,
        onComplete: (List<Database>) -> Unit
    ){
        val mir = MapImageRecorder(appContext)
        val renderImage = scope.async{
            val id = "0" /* TODO 利用可能な最小の地図IDを地図リストコントローラから取得 */
            val newMap = mir.render(pdf, id)
            newMap
        }

        val imageFile = renderImage.await()
        val name = this.confirmName.await()
        if(name == null){
            mir.rollback()
        }else {
            val newItem = Database(imageFile, name) /* TODO 地図リストコントローラに新しいデータを挿入 */
            val list = listOf(newItem) /* TODO 地図リストコントローラからリスト全体を取得 */

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
