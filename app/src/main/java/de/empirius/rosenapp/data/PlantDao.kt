package de.empirius.rosenapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Query("SELECT * FROM plants ORDER BY createdAtMillis DESC")
    fun observeAll(): Flow<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :id")
    fun observeById(id: Long): Flow<Plant?>

    @Query("SELECT * FROM plants WHERE id = :id")
    suspend fun getById(id: Long): Plant?

    /** Inserts a new plant and returns its generated id. */
    @Insert
    suspend fun insert(plant: Plant): Long

    @Update
    suspend fun update(plant: Plant)

    @Delete
    suspend fun delete(plant: Plant)
}
