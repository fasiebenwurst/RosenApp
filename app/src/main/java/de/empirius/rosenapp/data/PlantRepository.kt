package de.empirius.rosenapp.data

import kotlinx.coroutines.flow.Flow

/**
 * Thin repository over [PlantDao]. Keeps the rest of the app free of Room types
 * and gives us a single seam for future caching/sync.
 */
class PlantRepository(private val dao: PlantDao) {

    val plants: Flow<List<Plant>> = dao.observeAll()

    fun observePlant(id: Long): Flow<Plant?> = dao.observeById(id)

    suspend fun getPlant(id: Long): Plant? = dao.getById(id)

    suspend fun getAllPlants(): List<Plant> = dao.getAll()

    suspend fun addPlant(plant: Plant): Long = dao.insert(plant)

    suspend fun updatePlant(plant: Plant) = dao.update(plant)

    suspend fun deletePlant(plant: Plant) = dao.delete(plant)
}
