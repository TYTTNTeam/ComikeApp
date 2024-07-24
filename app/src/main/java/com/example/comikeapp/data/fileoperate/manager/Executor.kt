package com.example.comikeapp.data.fileoperate.manager

import android.content.Context
import com.example.comikeapp.data.maplist.MapListDatabaseProvider

class Executor(
    private val mapId: Int,
    private var mapUUID: String? = null,
    private val reserves: List<ByFileReserve>
) {
    fun execute(context: Context): Boolean {
        val db = MapListDatabaseProvider.getDatabase(context).mapListDao()

        if (mapUUID == null) {
            mapUUID = db.selectById(mapId).imagePath
        }

        var ok = true
        reserves.forEach { res ->
            if(!res.execute(context, mapUUID!!)) ok =false
        }
        return ok
    }
}