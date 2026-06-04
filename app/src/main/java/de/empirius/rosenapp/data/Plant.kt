package de.empirius.rosenapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Default label accent color (rose), as a packed ARGB int. */
val DEFAULT_ACCENT_COLOR: Int = 0xFFB3315A.toInt()

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
    /** Botanical (Latin) name, e.g. "Rosa 'New Dawn'"; optional. */
    val latinName: String? = null,
    /** Horticultural class (Hybrid Tea, Climber, …); optional. */
    val type: RoseType? = null,
    /** Historical era (species / old garden / modern); optional. */
    val era: RoseEra? = null,
    /** Where the rose originally comes from — breeder and/or country/native range. */
    val origin: String? = null,
    /** Awards the rose has won (AARS, ADR, RHS AGM, World's Favourite Rose, …). */
    val awards: String? = null,
    /** Absolute path to the captured photo on disk, or null if none yet. */
    val photoPath: String? = null,
    /** Where in the garden it lives, free text. */
    val location: String? = null,
    /** Planting date as epoch milliseconds (UTC midnight), or null if unset. */
    val plantingDateMillis: Long? = null,
    /** Free-form care notes. */
    val careNotes: String? = null,
    /** Accent color for the label's title font, border, and section labels (packed ARGB). */
    val accentColor: Int = DEFAULT_ACCENT_COLOR,
    val createdAtMillis: Long = System.currentTimeMillis(),
)
