package com.example.comikeapp

class MapListRepository(private val mapListDao: MapListDao) {

    fun getAll(): List<MapList> {
        return mapListDao.getAll()
    }
    fun insertAndGetAll(mapName: String?, imagePath: String?): List<MapList> {
        val mapList = MapList(mapName = mapName, imagePath = imagePath)
        mapListDao.insert(mapList)
        return mapListDao.getAll()
    }

    fun updateAndGetAll(mapId: Int, newName: String): List<MapList> {
        mapListDao.updateMapNameById(mapId, newName)
        return mapListDao.getAll()
    }

    fun deleteAndGetAll(mapId: Int): List<MapList> {
        mapListDao.deleteById(mapId)
        return mapListDao.getAll()
    }
}