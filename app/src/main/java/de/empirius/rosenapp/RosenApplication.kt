package de.empirius.rosenapp

import android.app.Application
import de.empirius.rosenapp.data.BackupManager
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.data.RosenDatabase
import de.empirius.rosenapp.label.LabelExporter
import de.empirius.rosenapp.photo.PhotoStorage
import de.empirius.rosenapp.reminder.ReminderNotifier
import de.empirius.rosenapp.reminder.ReminderScheduler

/**
 * Owns the app-wide singletons. We keep dependency wiring deliberately small
 * (no DI framework) — the object graph here is tiny.
 */
class RosenApplication : Application() {

    val repository: PlantRepository by lazy {
        val db = RosenDatabase.get(this)
        PlantRepository(db.plantDao(), db.plantPhotoDao(), db.careReminderDao())
    }

    val photoStorage: PhotoStorage by lazy {
        PhotoStorage(this)
    }

    val labelExporter: LabelExporter by lazy {
        LabelExporter(this)
    }

    val backupManager: BackupManager by lazy {
        BackupManager(this, repository, photoStorage)
    }

    override fun onCreate() {
        super.onCreate()
        ReminderNotifier.ensureChannel(this)
        ReminderScheduler.schedule(this)
    }
}
