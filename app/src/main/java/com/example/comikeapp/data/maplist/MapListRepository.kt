package com.example.comikeapp.data.maplist

class MapListRepository(private val mapListDao: MapListDao) {

    fun getAll(): List<MapList> {
        return mapListDao.getAll()
    }

    fun insertAndGetAll(
        mapName: String?,
        imagePath: String?
    ): List<MapList> {
        val mapList: MapList
        if(mapName != null && imagePath != null){
            mapList = MapList(
                mapName = mapName,
                imagePath = imagePath
            )
        }else{
            throw NullPointerException("行を追加する場合、nullを渡さないでください。")
        }
        mapListDao.insert(mapList)
        return mapListDao.getAll()
    }

    fun updateAndGetAll(mapId: Int, newName: String): List<MapList> {
        mapListDao.updateMapNameById(mapId, newName)
        return mapListDao.getAll()
    }

    fun deleteAndGetAll(mapId: Int): List<MapList> {
        // TODO ファイルが削除されないため、変更が必要。
        mapListDao.deleteById(mapId)
        return mapListDao.getAll()
    }
}