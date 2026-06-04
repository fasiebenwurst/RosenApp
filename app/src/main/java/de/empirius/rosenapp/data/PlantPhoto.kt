package de.empirius.rosenapp.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A dated photo in a plant's journal. Separate from [Plant.photoPath] (the
 * cover image): a plant can have many of these, each with its own capture
 * date and an optional note. Photo files live in the app's internal storage
 * (see [de.empirius.rosenapp.photo.PhotoStorage]); we delete them explicitly
 * when a photo or its plant is removed.
 */
@Entity(
    tableName = "plant_photos",
    indices = [Index("plantId")],
)
data class PlantPhoto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val plantId: Long,
    /** Absolute path to the image file on disk. */
    val path: String,
    /** The date the photo represents (epoch millis); defaults to when it was added. */
    val takenAtMillis: Long = System.currentTimeMillis(),
    /** Optional free-text note for this photo. */
    val note: String? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
)
