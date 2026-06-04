package de.empirius.rosenapp

import android.app.Application
import de.empirius.rosenapp.data.BackupManager
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.data.RosenDatabase
import de.empirius.rosenapp.label.LabelExporter
import de.empirius.rosenapp.photo.PhotoStorage

/**
 * Owns the app-wide singletons. We keep dependency wiring deliberately small
 * (no DI framework) — the object graph here is tiny.
 */
class RosenApplication : Application() {

    val repository: PlantRepository by lazy {
        PlantRepository(RosenDatabase.get(this).plantDao())
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
}
