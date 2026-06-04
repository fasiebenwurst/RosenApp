package de.empirius.rosenapp.data

import android.content.Context
import android.net.Uri
import de.empirius.rosenapp.photo.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Backup and restore for the whole garden. Produces / consumes a single ZIP
 * archive containing a `plants.json` data dump plus the `photos/` the plants
 * reference, so a backup is fully self-contained and portable across devices.
 *
 * Import is additive: plants from the archive are inserted alongside whatever
 * already exists (ids are regenerated), so restoring never destroys current data.
 */
class BackupManager(
    private val context: Context,
    private val repository: PlantRepository,
    private val photoStorage: PhotoStorage,
) {

    /** Writes a backup ZIP to [dest]; returns the number of plants exported. */
    suspend fun export(dest: Uri): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            val plants = repository.getAllPlants()
            val output = context.contentResolver.openOutputStream(dest)
                ?: error("Could not open the chosen file for writing.")
            output.use { raw ->
                ZipOutputStream(BufferedOutputStream(raw)).use { zip ->
                    val array = JSONArray()
                    plants.forEachIndexed { index, plant ->
                        val obj = JSONObject()
                        obj.put("name", plant.name)
                        plant.latinName?.let { obj.put("latinName", it) }
                        plant.type?.let { obj.put("type", it.name) }
                        plant.era?.let { obj.put("era", it.name) }
                        plant.location?.let { obj.put("location", it) }
                        plant.plantingDateMillis?.let { obj.put("plantingDateMillis", it) }
                        plant.careNotes?.let { obj.put("careNotes", it) }
                        obj.put("accentColor", plant.accentColor)
                        obj.put("createdAtMillis", plant.createdAtMillis)

                        val photoPath = plant.photoPath
                        if (photoPath != null && File(photoPath).exists()) {
                            val entryName = "photo_$index.jpg"
                            obj.put("photo", entryName)
                            zip.putNextEntry(ZipEntry("photos/$entryName"))
                            File(photoPath).inputStream().use { it.copyTo(zip) }
                            zip.closeEntry()
                        }
                        array.put(obj)
                    }

                    val root = JSONObject().put("version", BACKUP_VERSION).put("plants", array)
                    zip.putNextEntry(ZipEntry(MANIFEST))
                    zip.write(root.toString(2).toByteArray(Charsets.UTF_8))
                    zip.closeEntry()
                }
            }
            plants.size
        }
    }

    /** Reads a backup ZIP from [src] and inserts its plants; returns the number imported. */
    suspend fun import(src: Uri, mode: ImportMode): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            var manifest: String? = null
            val photoPaths = HashMap<String, String>() // archive filename -> restored path

            val input = context.contentResolver.openInputStream(src)
                ?: error("Could not open the chosen file for reading.")
            input.use { raw ->
                ZipInputStream(BufferedInputStream(raw)).use { zip ->
                    var entry: ZipEntry? = zip.nextEntry
                    while (entry != null) {
                        val name = entry.name
                        when {
                            name == MANIFEST -> manifest = zip.readBytes().toString(Charsets.UTF_8)
                            name.startsWith("photos/") && !entry.isDirectory -> {
                                val base = name.substringAfterLast('/')
                                photoStorage.importStream(zip)?.let { photoPaths[base] = it }
                            }
                        }
                        zip.closeEntry()
                        entry = zip.nextEntry
                    }
                }
            }

            val text = manifest ?: error("This file is not a RosenApp backup (no plants.json).")
            val array = JSONObject(text).getJSONArray("plants")

            // For a full restore, clear existing plants (and their photos) first.
            // Done after the archive's photos are already extracted, so nothing
            // we're about to reference gets removed.
            if (mode == ImportMode.REPLACE) {
                repository.getAllPlants().forEach { photoStorage.deletePhoto(it.photoPath) }
                repository.deleteAllPlants()
            }

            var imported = 0
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val plant = Plant(
                    name = obj.getString("name"),
                    latinName = obj.optStringOrNull("latinName"),
                    type = obj.optStringOrNull("type")?.let { runCatching { RoseType.valueOf(it) }.getOrNull() },
                    era = obj.optStringOrNull("era")?.let { runCatching { RoseEra.valueOf(it) }.getOrNull() },
                    photoPath = obj.optStringOrNull("photo")?.let { photoPaths[it] },
                    location = obj.optStringOrNull("location"),
                    plantingDateMillis = if (obj.has("plantingDateMillis")) obj.getLong("plantingDateMillis") else null,
                    careNotes = obj.optStringOrNull("careNotes"),
                    accentColor = obj.optInt("accentColor", DEFAULT_ACCENT_COLOR),
                    createdAtMillis = obj.optLong("createdAtMillis", System.currentTimeMillis()),
                )
                repository.addPlant(plant)
                imported++
            }
            imported
        }
    }

    private fun JSONObject.optStringOrNull(key: String): String? =
        if (has(key) && !isNull(key)) getString(key) else null

    /** How an import combines with the current data. */
    enum class ImportMode {
        /** Add the archive's plants alongside existing ones. */
        MERGE,

        /** Delete all current plants and photos, then restore from the archive. */
        REPLACE,
    }

    companion object {
        private const val BACKUP_VERSION = 1
        private const val MANIFEST = "plants.json"
        const val SUGGESTED_FILE_NAME = "rosenapp-backup.zip"
        const val MIME_TYPE = "application/zip"
    }
}
