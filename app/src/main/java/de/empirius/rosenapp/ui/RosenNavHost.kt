package de.empirius.rosenapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.empirius.rosenapp.ui.detail.PlantDetailScreen
import de.empirius.rosenapp.ui.edit.PlantEditScreen
import de.empirius.rosenapp.ui.calendar.CalendarScreen
import de.empirius.rosenapp.ui.label.LabelScreen
import de.empirius.rosenapp.ui.plants.PlantListScreen
import de.empirius.rosenapp.ui.stats.StatsScreen

/** Central navigation graph. Routes are plain strings; ids travel as path args. */
object Routes {
    const val LIST = "plants"
    const val DETAIL = "detail/{plantId}"
    const val EDIT = "edit?plantId={plantId}"
    const val LABEL = "label/{plantId}"
    const val STATS = "stats"
    const val CALENDAR = "calendar"

    fun detail(plantId: Long) = "detail/$plantId"
    fun edit(plantId: Long? = null) = if (plantId == null) "edit?plantId=-1" else "edit?plantId=$plantId"
    fun label(plantId: Long) = "label/$plantId"
}

@Composable
fun RosenNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LIST) {

        composable(Routes.LIST) {
            PlantListScreen(
                onAddPlant = { navController.navigate(Routes.edit()) },
                onOpenPlant = { id -> navController.navigate(Routes.detail(id)) },
                onOpenStats = { navController.navigate(Routes.STATS) },
                onOpenCalendar = { navController.navigate(Routes.CALENDAR) },
            )
        }

        composable(Routes.STATS) {
            StatsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("plantId") { type = NavType.LongType }),
        ) { entry ->
            val plantId = entry.arguments?.getLong("plantId") ?: return@composable
            PlantDetailScreen(
                plantId = plantId,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.edit(plantId)) },
                onCreateLabel = { navController.navigate(Routes.label(plantId)) },
                onDeleted = { navController.popBackStack(Routes.LIST, inclusive = false) },
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(
                navArgument("plantId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
            ),
        ) { entry ->
            val raw = entry.arguments?.getLong("plantId") ?: -1L
            val plantId = raw.takeIf { it >= 0 }
            PlantEditScreen(
                plantId = plantId,
                onDone = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.LABEL,
            arguments = listOf(navArgument("plantId") { type = NavType.LongType }),
        ) { entry ->
            val plantId = entry.arguments?.getLong("plantId") ?: return@composable
            LabelScreen(
                plantId = plantId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
