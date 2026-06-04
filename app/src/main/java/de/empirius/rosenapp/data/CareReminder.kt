package de.empirius.rosenapp.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** A category of garden care task; drives the default title and calendar icon. */
enum class CareTask(val displayName: String) {
    PRUNE("Prune"),
    FEED("Feed"),
    SPRAY("Spray"),
    WATER("Water"),
    MULCH("Mulch"),
    DEADHEAD("Deadhead"),
    OTHER("Other"),
}

/** Unit for a recurring reminder's interval. */
enum class RecurrenceUnit(val displayName: String) {
    DAYS("days"),
    WEEKS("weeks"),
    MONTHS("months"),
    YEARS("years"),
}

/**
 * A care reminder. One-off when [intervalCount] is 0, otherwise it repeats every
 * [intervalCount] [intervalUnit] from [startDateMillis]. May be tied to a plant
 * ([plantId]) or be garden-wide (null).
 *
 * Dates are stored as UTC-midnight epoch millis to match the Material date picker.
 */
@Entity(
    tableName = "care_reminders",
    indices = [Index("plantId")],
)
data class CareReminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val plantId: Long? = null,
    val task: CareTask = CareTask.OTHER,
    val title: String,
    val startDateMillis: Long,
    /** 0 = one-off; otherwise repeat every [intervalCount] [intervalUnit]. */
    val intervalCount: Int = 0,
    val intervalUnit: RecurrenceUnit = RecurrenceUnit.WEEKS,
    val notify: Boolean = true,
    /** Epoch-day of the last day we posted a notification, to avoid duplicates. */
    val lastNotifiedDayEpoch: Long = 0,
    val createdAtMillis: Long = System.currentTimeMillis(),
) {
    val isRecurring: Boolean get() = intervalCount > 0
}
