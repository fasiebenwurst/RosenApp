package de.empirius.rosenapp.reminder

import de.empirius.rosenapp.data.CareReminder
import de.empirius.rosenapp.data.RecurrenceUnit
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/** Date/occurrence helpers shared between the calendar UI and the notification worker. */
object ReminderSchedule {

    /** UTC-midnight epoch millis (matching the Material date picker) -> [LocalDate]. */
    fun toLocalDate(epochMillis: Long): LocalDate =
        Instant.ofEpochMilli(epochMillis).atZone(ZoneOffset.UTC).toLocalDate()

    /** [LocalDate] -> UTC-midnight epoch millis. */
    fun toEpochMillis(date: LocalDate): Long =
        date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    /** Whether [reminder] falls on [date]. */
    fun occursOn(reminder: CareReminder, date: LocalDate): Boolean {
        val start = toLocalDate(reminder.startDateMillis)
        if (date.isBefore(start)) return false
        if (!reminder.isRecurring) return date == start
        val n = reminder.intervalCount.toLong()
        return when (reminder.intervalUnit) {
            RecurrenceUnit.DAYS -> ChronoUnit.DAYS.between(start, date) % n == 0L
            RecurrenceUnit.WEEKS -> {
                val days = ChronoUnit.DAYS.between(start, date)
                days % (7L * n) == 0L
            }
            RecurrenceUnit.MONTHS -> {
                val months = ChronoUnit.MONTHS.between(start, date)
                months % n == 0L && start.plusMonths(months) == date
            }
            RecurrenceUnit.YEARS -> {
                val years = ChronoUnit.YEARS.between(start, date)
                years % n == 0L && start.plusYears(years) == date
            }
        }
    }

    /** A short human description of the recurrence, e.g. "Every 2 weeks" or "One-off". */
    fun recurrenceLabel(reminder: CareReminder): String {
        if (!reminder.isRecurring) return "One-off"
        val unit = reminder.intervalUnit.displayName
        return if (reminder.intervalCount == 1) {
            "Every ${unit.dropLast(1)}" // "days" -> "day"
        } else {
            "Every ${reminder.intervalCount} $unit"
        }
    }
}
