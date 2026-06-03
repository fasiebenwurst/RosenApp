package de.empirius.rosenapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single plant (typically a rose) the user is tracking in their garden.
 *
 * Everything is stored locally; [photoPath] points at a file in the app's
 * internal `photos/` directory (see [de.empirius.rosenapp.photo.PhotoStorage]).
 */
@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    /** Absolute path to the captured photo on disk, or null if none yet. */
    val photoPath: String? = null,
    /** Where in the garden it lives, free text. */
    val location: String? = null,
    /** Planting date as epoch milliseconds (UTC midnight), or null if unset. */
    val plantingDateMillis: Long? = null,
    /** Free-form care notes. */
    val careNotes: String? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
)
