package de.empirius.rosenapp.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.label.LabelExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class LabelViewModel(
    plantId: Long,
    private val repository: PlantRepository,
    private val labelExporter: LabelExporter,
) : ViewModel() {

    val plant: StateFlow<Plant?> = repository.observePlant(plantId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Persists a new accent color; the preview updates via the observed [plant] flow. */
    fun setAccentColor(color: Int) {
        val current = plant.value ?: return
        if (current.accentColor == color) return
        viewModelScope.launch { repository.updatePlant(current.copy(accentColor = color)) }
    }

    /** Renders to PDF on a background thread and shares it. */
    fun exportAndSharePdf() = withPlant { plant ->
        val file = withContext(Dispatchers.Default) { labelExporter.exportPdf(plant) }
        labelExporter.share(file)
    }

    /** Renders to PNG on a background thread and shares it. */
    fun exportAndSharePng() = withPlant { plant ->
        val file = withContext(Dispatchers.Default) { labelExporter.exportPng(plant) }
        labelExporter.share(file)
    }

    /** Saves a PDF without opening the share sheet; reports the file back. */
    fun savePdf(onSaved: (File) -> Unit) = withPlant { plant ->
        val file = withContext(Dispatchers.Default) { labelExporter.exportPdf(plant) }
        onSaved(file)
    }

    private inline fun withPlant(crossinline block: suspend (Plant) -> Unit) {
        val current = plant.value ?: return
        viewModelScope.launch { block(current) }
    }
}
