package de.empirius.rosenapp.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

/** Schedules the daily reminder check (and an immediate one on app start). */
object ReminderScheduler {

    private const val DAILY_WORK = "care-reminder-daily-check"
    private const val STARTUP_WORK = "care-reminder-startup-check"

    /** Roughly 8 AM local time. */
    private val CHECK_TIME = LocalTime.of(8, 0)

    fun schedule(context: Context) {
        val workManager = WorkManager.getInstance(context)

        // A check right away, so reminders created/just due surface promptly.
        workManager.enqueueUniqueWork(
            STARTUP_WORK,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<ReminderCheckWorker>().build(),
        )

        // A daily check, first firing at the next ~8 AM.
        val periodic = PeriodicWorkRequestBuilder<ReminderCheckWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelayMinutes(), TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(DAILY_WORK, ExistingPeriodicWorkPolicy.KEEP, periodic)
    }

    private fun initialDelayMinutes(): Long {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        var next = now.toLocalDate().atTime(CHECK_TIME)
        if (!next.isAfter(now)) next = next.plusDays(1)
        return ChronoUnit.MINUTES.between(now, next).coerceAtLeast(1)
    }
}
