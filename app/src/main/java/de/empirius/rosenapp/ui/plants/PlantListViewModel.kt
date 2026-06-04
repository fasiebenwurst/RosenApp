package de.empirius.rosenapp.ui.plants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.data.RoseEra
import de.empirius.rosenapp.data.RoseType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PlantListViewModel(repository: PlantRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedEras = MutableStateFlow<Set<RoseEra>>(emptySet())
    val selectedEras: StateFlow<Set<RoseEra>> = _selectedEras.asStateFlow()

    private val _selectedType = MutableStateFlow<RoseType?>(null)
    val selectedType: StateFlow<RoseType?> = _selectedType.asStateFlow()

    /** The plants after applying the search text and the era/type filters. */
    val plants: StateFlow<List<Plant>> =
        combine(repository.plants, _query, _selectedEras, _selectedType) { all, query, eras, type ->
            val q = query.trim()
            all.filter { plant ->
                val matchesQuery = q.isEmpty() ||
                    plant.name.contains(q, ignoreCase = true) ||
                    plant.latinName?.contains(q, ignoreCase = true) == true
                val matchesEra = eras.isEmpty() || plant.era in eras
                val matchesType = type == null || plant.type == type
                matchesQuery && matchesEra && matchesType
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val hasActiveFilters: StateFlow<Boolean> =
        combine(_query, _selectedEras, _selectedType) { query, eras, type ->
            query.isNotBlank() || eras.isNotEmpty() || type != null
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setQuery(value: String) { _query.value = value }

    fun toggleEra(era: RoseEra) {
        _selectedEras.value = _selectedEras.value.toMutableSet().apply {
            if (!add(era)) remove(era)
        }
    }

    fun setType(type: RoseType?) { _selectedType.value = type }

    fun clearFilters() {
        _query.value = ""
        _selectedEras.value = emptySet()
        _selectedType.value = null
    }
}
