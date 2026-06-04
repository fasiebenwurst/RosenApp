package de.empirius.rosenapp.reminder

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.empirius.rosenapp.RosenApplication
import java.time.LocalDate

/**
 * Runs roughly daily (and on app start): posts a notification for every reminder
 * due today that the user opted into and that we haven't already notified today.
 */
class ReminderCheckWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as RosenApplication
        val repository = app.repository
        val today = LocalDate.now()
        val todayEpoch = today.toEpochDay()

        val plantsById = repository.getAllPlants().associateBy { it.id }
        repository.getAllReminders()
            .filter { it.notify && it.lastNotifiedDayEpoch != todayEpoch && ReminderSchedule.occursOn(it, today) }
            .forEach { reminder ->
                val plantName = reminder.plantId?.let { plantsById[it]?.name }
                val recurrence = ReminderSchedule.recurrenceLabel(reminder)
                val text = if (plantName != null) "$plantName · $recurrence" else recurrence
                ReminderNotifier.notify(applicationContext, reminder.id.toInt(), reminder.title, text)
                repository.updateReminder(reminder.copy(lastNotifiedDayEpoch = todayEpoch))
            }
        return Result.success()
    }
}
