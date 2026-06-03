package de.empirius.rosenapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.label.LabelExporter
import de.empirius.rosenapp.photo.PhotoStorage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantDetailViewModel(
    plantId: Long,
    private val repository: PlantRepository,
    private val photoStorage: PhotoStorage,
    private val labelExporter: LabelExporter,
) : ViewModel() {

    val plant: StateFlow<Plant?> = repository.observePlant(plantId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun delete(onDeleted: () -> Unit) {
        val current = plant.value ?: return
        viewModelScope.launch {
            repository.deletePlant(current)
            photoStorage.deletePhoto(current.photoPath)
            labelExporter.cleanupFor(current)
            onDeleted()
        }
    }
}
