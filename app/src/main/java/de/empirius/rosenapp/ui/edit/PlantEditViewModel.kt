package de.empirius.rosenapp.ui.edit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.data.RoseCatalog
import de.empirius.rosenapp.data.RoseEntry
import de.empirius.rosenapp.data.RoseEra
import de.empirius.rosenapp.data.RoseType
import de.empirius.rosenapp.photo.PhotoStorage
import kotlinx.coroutines.launch

/**
 * Backs the new/edit form. Holds the in-progress field values and coordinates
 * photo capture/import through [PhotoStorage].
 */
class PlantEditViewModel(
    private val repository: PlantRepository,
    private val photoStorage: PhotoStorage,
    private val plantId: Long?,
) : ViewModel() {

    var name by mutableStateOf("")
        private set
    var latinName by mutableStateOf("")
        private set
    var type by mutableStateOf<RoseType?>(null)
        private set
    var era by mutableStateOf<RoseEra?>(null)
        private set
    var location by mutableStateOf("")
        private set
    var notes by mutableStateOf("")
        private set
    var plantingDateMillis by mutableStateOf<Long?>(null)
        private set
    var photoPath by mutableStateOf<String?>(null)
        private set
    var showNameError by mutableStateOf(false)
        private set

    val isEditing: Boolean get() = plantId != null

    /** A photo file we created for the camera but haven't confirmed was written yet. */
    private var pendingPhotoPath: String? = null
    private var createdAtMillis: Long = System.currentTimeMillis()

    init {
        if (plantId != null) {
            viewModelScope.launch {
                repository.getPlant(plantId)?.let { plant ->
                    name = plant.name
                    latinName = plant.latinName.orEmpty()
                    type = plant.type
                    era = plant.era
                    location = plant.location.orEmpty()
                    notes = plant.careNotes.orEmpty()
                    plantingDateMillis = plant.plantingDateMillis
                    photoPath = plant.photoPath
                    createdAtMillis = plant.createdAtMillis
                }
            }
        }
    }

    /** Catalog suggestions for the current name input. */
    val nameSuggestions: List<RoseEntry>
        get() = RoseCatalog.search(name)

    fun onNameChange(value: String) {
        name = value
        if (showNameError && value.isNotBlank()) showNameError = false
        // Convenience: if the typed name exactly matches a known rose, pre-fill
        // any classification fields the user hasn't filled in yet.
        RoseCatalog.entryFor(value)?.let { entry ->
            if (latinName.isBlank()) latinName = entry.latinName
            if (type == null) type = entry.type
            if (era == null) era = entry.era
        }
    }

    /** Picking a rose from the dropdown fills its name, Latin name, type, and era. */
    fun onRoseSelected(entry: RoseEntry) {
        name = entry.name
        latinName = entry.latinName
        type = entry.type
        era = entry.era
        showNameError = false
    }

    fun onLatinNameChange(value: String) { latinName = value }
    fun onTypeChange(value: RoseType?) { type = value }
    fun onEraChange(value: RoseEra?) { era = value }

    fun onLocationChange(value: String) { location = value }
    fun onNotesChange(value: String) { notes = value }
    fun onDateChange(millis: Long?) { plantingDateMillis = millis }

    /** Creates a fresh target file for the camera and returns the content URI to write into. */
    fun prepareCapture(): Uri {
        val target = photoStorage.newPhotoTarget()
        pendingPhotoPath = target.path
        return target.uri
    }

    fun onCaptureResult(success: Boolean) {
        val pending = pendingPhotoPath
        if (success && pending != null) {
            // Replacing an existing photo: drop the old file.
            photoPath?.let { if (it != pending) photoStorage.deletePhoto(it) }
            photoPath = pending
        } else if (pending != null) {
            photoStorage.deletePhoto(pending)
        }
        pendingPhotoPath = null
    }

    fun onPhotoPicked(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch {
            val imported = photoStorage.importFrom(uri) ?: return@launch
            photoPath?.let { photoStorage.deletePhoto(it) }
            photoPath = imported
        }
    }

    fun save(onSaved: () -> Unit) {
        if (name.isBlank()) {
            showNameError = true
            return
        }
        viewModelScope.launch {
            val plant = Plant(
                id = plantId ?: 0,
                name = name.trim(),
                latinName = latinName.trim().ifBlank { null },
                type = type,
                era = era,
                photoPath = photoPath,
                location = location.trim().ifBlank { null },
                plantingDateMillis = plantingDateMillis,
                careNotes = notes.trim().ifBlank { null },
                createdAtMillis = createdAtMillis,
            )
            if (plantId == null) repository.addPlant(plant) else repository.updatePlant(plant)
            onSaved()
        }
    }
}
