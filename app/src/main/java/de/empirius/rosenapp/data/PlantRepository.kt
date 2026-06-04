package de.empirius.rosenapp.data

import kotlinx.coroutines.flow.Flow

/**
 * Thin repository over [PlantDao] and [PlantPhotoDao]. Keeps the rest of the app
 * free of Room types and gives us a single seam for future caching/sync.
 */
class PlantRepository(
    private val dao: PlantDao,
    private val photoDao: PlantPhotoDao,
) {

    val plants: Flow<List<Plant>> = dao.observeAll()

    fun observePlant(id: Long): Flow<Plant?> = dao.observeById(id)

    suspend fun getPlant(id: Long): Plant? = dao.getById(id)

    suspend fun getAllPlants(): List<Plant> = dao.getAll()

    suspend fun addPlant(plant: Plant): Long = dao.insert(plant)

    suspend fun updatePlant(plant: Plant) = dao.update(plant)

    suspend fun deletePlant(plant: Plant) = dao.delete(plant)

    suspend fun deleteAllPlants() = dao.deleteAll()

    // --- Photo journal ---

    fun observePhotos(plantId: Long): Flow<List<PlantPhoto>> = photoDao.observeForPlant(plantId)

    suspend fun getPhotos(plantId: Long): List<PlantPhoto> = photoDao.getForPlant(plantId)

    suspend fun getAllPhotos(): List<PlantPhoto> = photoDao.getAll()

    suspend fun addPhoto(photo: PlantPhoto): Long = photoDao.insert(photo)

    suspend fun updatePhoto(photo: PlantPhoto) = photoDao.update(photo)

    suspend fun deletePhoto(photo: PlantPhoto) = photoDao.delete(photo)

    suspend fun deletePhotosForPlant(plantId: Long) = photoDao.deleteForPlant(plantId)

    suspend fun deleteAllPhotos() = photoDao.deleteAll()
}
