package com.example.comikeapp.data.fileoperate.manager

import android.content.Context
import com.example.comikeapp.data.maplist.MapListDatabaseProvider
import java.io.File

class Executor(
    private val mapId: Int,
    private var mapUUID: String? = null,
    private val reserves: List<ByFileReserve>
) {
    fun execute(context: Context): Boolean {
        val db = MapListDatabaseProvider.getDatabase(context).mapListDao()

        if (mapUUID == null) {
            val p = db.getImagePath(mapId)
            // UUIDだけを抽出
            mapUUID = File(p).name
        }

        var ok = true
        reserves.forEach { res ->
            if(!res.execute(context, mapUUID!!)) ok = false
        }
        return ok
    }
}