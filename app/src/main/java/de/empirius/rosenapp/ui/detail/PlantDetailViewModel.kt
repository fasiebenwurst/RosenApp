package de.empirius.rosenapp.ui.detail

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantPhoto
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.label.LabelExporter
import de.empirius.rosenapp.photo.PhotoStorage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantDetailViewModel(
    private val plantId: Long,
    private val repository: PlantRepository,
    private val photoStorage: PhotoStorage,
    private val labelExporter: LabelExporter,
) : ViewModel() {

    val plant: StateFlow<Plant?> = repository.observePlant(plantId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val photos: StateFlow<List<PlantPhoto>> = repository.observePhotos(plantId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** A journal photo file we created for the camera but haven't confirmed yet. */
    private var pendingPhotoPath: String? = null

    /** Creates a capture target for a new journal photo; returns the content URI to write into. */
    fun preparePhotoCapture(): Uri {
        val target = photoStorage.newPhotoTarget()
        pendingPhotoPath = target.path
        return target.uri
    }

    fun onPhotoCaptured(success: Boolean) {
        val pending = pendingPhotoPath
        pendingPhotoPath = null
        if (success && pending != null) {
            addJournalPhoto(pending)
        } else if (pending != null) {
            photoStorage.deletePhoto(pending)
        }
    }

    fun onPhotoPicked(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch {
            val imported = photoStorage.importFrom(uri) ?: return@launch
            addJournalPhoto(imported)
        }
    }

    private fun addJournalPhoto(path: String) {
        viewModelScope.launch {
            repository.addPhoto(PlantPhoto(plantId = plantId, path = path))
        }
    }

    fun updatePhoto(photo: PlantPhoto, takenAtMillis: Long, note: String?) {
        viewModelScope.launch {
            repository.updatePhoto(photo.copy(takenAtMillis = takenAtMillis, note = note?.ifBlank { null }))
        }
    }

    fun deleteJournalPhoto(photo: PlantPhoto) {
        viewModelScope.launch {
            repository.deletePhoto(photo)
            photoStorage.deletePhoto(photo.path)
        }
    }

    /** Promotes a journal photo to be the plant's cover image. */
    fun setAsCover(photo: PlantPhoto) {
        val current = plant.value ?: return
        viewModelScope.launch {
            repository.updatePlant(current.copy(photoPath = photo.path))
        }
    }

    fun delete(onDeleted: () -> Unit) {
        val current = plant.value ?: return
        viewModelScope.launch {
            val journal = repository.getPhotos(plantId)
            repository.deletePlant(current)
            repository.deletePhotosForPlant(plantId)
            photoStorage.deletePhoto(current.photoPath)
            journal.forEach { photoStorage.deletePhoto(it.path) }
            labelExporter.cleanupFor(current)
            onDeleted()
        }
    }
}
