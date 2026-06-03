package de.empirius.rosenapp.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.PlantRepository
import de.empirius.rosenapp.data.RoseEra
import de.empirius.rosenapp.data.RoseType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

/** Aggregated counts for the statistics screen. */
data class GardenStats(
    val total: Int = 0,
    val byEra: List<Count<RoseEra>> = emptyList(),
    val eraUnset: Int = 0,
    val byType: List<Count<RoseType>> = emptyList(),
    val typeUnset: Int = 0,
    val timeline: List<YearCount> = emptyList(),
    val undated: Int = 0,
) {
    data class Count<T>(val value: T, val count: Int)
    data class YearCount(val year: Int, val count: Int)
}

class StatsViewModel(repository: PlantRepository) : ViewModel() {

    val stats: StateFlow<GardenStats> = repository.plants
        .map { computeStats(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GardenStats())

    private fun computeStats(plants: List<Plant>): GardenStats {
        // Era counts in the natural era order (species → old garden → modern).
        val eraCounts = plants.mapNotNull { it.era }.groupingBy { it }.eachCount()
        val byEra = RoseEra.entries
            .filter { (eraCounts[it] ?: 0) > 0 }
            .map { GardenStats.Count(it, eraCounts.getValue(it)) }

        // Type counts, most common first.
        val typeCounts = plants.mapNotNull { it.type }.groupingBy { it }.eachCount()
        val byType = typeCounts.entries
            .sortedWith(compareByDescending<Map.Entry<RoseType, Int>> { it.value }.thenBy { it.key.displayName })
            .map { GardenStats.Count(it.key, it.value) }

        // Planting timeline by year, ascending.
        val timeline = plants.mapNotNull { it.plantingDateMillis?.let(::yearOf) }
            .groupingBy { it }.eachCount()
            .entries.sortedBy { it.key }
            .map { GardenStats.YearCount(it.key, it.value) }

        return GardenStats(
            total = plants.size,
            byEra = byEra,
            eraUnset = plants.count { it.era == null },
            byType = byType,
            typeUnset = plants.count { it.type == null },
            timeline = timeline,
            undated = plants.count { it.plantingDateMillis == null },
        )
    }

    private fun yearOf(millis: Long): Int =
        Calendar.getInstance().apply { timeInMillis = millis }.get(Calendar.YEAR)
}
