package de.empirius.rosenapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CareReminderDao {

    @Query("SELECT * FROM care_reminders ORDER BY startDateMillis")
    fun observeAll(): Flow<List<CareReminder>>

    @Query("SELECT * FROM care_reminders")
    suspend fun getAll(): List<CareReminder>

    @Insert
    suspend fun insert(reminder: CareReminder): Long

    @Update
    suspend fun update(reminder: CareReminder)

    @Delete
    suspend fun delete(reminder: CareReminder)

    @Query("DELETE FROM care_reminders WHERE plantId = :plantId")
    suspend fun deleteForPlant(plantId: Long)

    @Query("DELETE FROM care_reminders")
    suspend fun deleteAll()
}
