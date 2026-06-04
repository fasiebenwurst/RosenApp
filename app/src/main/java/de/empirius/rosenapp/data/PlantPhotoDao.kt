package de.empirius.rosenapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantPhotoDao {

    @Query("SELECT * FROM plant_photos WHERE plantId = :plantId ORDER BY takenAtMillis DESC, id DESC")
    fun observeForPlant(plantId: Long): Flow<List<PlantPhoto>>

    @Query("SELECT * FROM plant_photos WHERE plantId = :plantId ORDER BY takenAtMillis DESC, id DESC")
    suspend fun getForPlant(plantId: Long): List<PlantPhoto>

    @Query("SELECT * FROM plant_photos")
    suspend fun getAll(): List<PlantPhoto>

    @Insert
    suspend fun insert(photo: PlantPhoto): Long

    @Update
    suspend fun update(photo: PlantPhoto)

    @Delete
    suspend fun delete(photo: PlantPhoto)

    @Query("DELETE FROM plant_photos WHERE plantId = :plantId")
    suspend fun deleteForPlant(plantId: Long)

    @Query("DELETE FROM plant_photos")
    suspend fun deleteAll()
}
