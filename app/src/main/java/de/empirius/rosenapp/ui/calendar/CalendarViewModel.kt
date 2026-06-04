package de.empirius.rosenapp.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.CareReminder
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalendarViewModel(private val repository: PlantRepository) : ViewModel() {

    val plants: StateFlow<List<Plant>> = repository.plants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val reminders: StateFlow<List<CareReminder>> = repository.reminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addReminder(reminder: CareReminder) {
        viewModelScope.launch { repository.addReminder(reminder) }
    }

    fun deleteReminder(reminder: CareReminder) {
        viewModelScope.launch { repository.deleteReminder(reminder) }
    }
}
