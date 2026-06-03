package de.empirius.rosenapp.ui.plants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PlantListViewModel(repository: PlantRepository) : ViewModel() {

    val plants: StateFlow<List<Plant>> = repository.plants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
