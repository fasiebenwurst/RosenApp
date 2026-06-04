package de.empirius.rosenapp.ui.plants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import de.empirius.rosenapp.R
import de.empirius.rosenapp.data.BackupManager
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.RoseEra
import de.empirius.rosenapp.data.RoseType
import de.empirius.rosenapp.ui.rememberApp
import kotlinx.coroutines.launch
import java.io.File
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantListScreen(
    onAddPlant: () -> Unit,
    onOpenPlant: (Long) -> Unit,
    onOpenStats: () -> Unit,
    onOpenCalendar: () -> Unit,
) {
    val app = rememberApp()
    val viewModel: PlantListViewModel = viewModel(
        factory = viewModelFactory { initializer { PlantListViewModel(app.repository) } },
    )
    val plants by viewModel.plants.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val selectedEras by viewModel.selectedEras.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var menuExpanded by remember { mutableStateOf(false) }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument(BackupManager.MIME_TYPE),
    ) { uri ->
        if (uri != null) scope.launch {
            val result = app.backupManager.export(uri)
            snackbarHostState.showSnackbar(
                result.fold(
                    onSuccess = { context.getString(R.string.backup_exported, it) },
                    onFailure = { context.getString(R.string.backup_failed) },
                ),
            )
        }
    }
    // Holds the picked archive while the user chooses merge vs. replace.
    var pendingImportUri by remember { mutableStateOf<Uri?>(null) }
    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri -> pendingImportUri = uri }

    fun runImport(uri: Uri, mode: BackupManager.ImportMode) {
        scope.launch {
            val result = app.backupManager.import(uri, mode)
            snackbarHostState.showSnackbar(
                result.fold(
                    onSuccess = { context.getString(R.string.backup_imported, it) },
                    onFailure = { context.getString(R.string.import_failed) },
                ),
            )
        }
    }

    pendingImportUri?.let { uri ->
        AlertDialog(
            onDismissRequest = { pendingImportUri = null },
            title = { Text(stringResource(R.string.import_dialog_title)) },
            text = { Text(stringResource(R.string.import_dialog_body)) },
            confirmButton = {
                Row {
                    TextButton(onClick = {
                        pendingImportUri = null
                        runImport(uri, BackupManager.ImportMode.MERGE)
                    }) { Text(stringResource(R.string.import_merge)) }
                    TextButton(onClick = {
                        pendingImportUri = null
                        runImport(uri, BackupManager.ImportMode.REPLACE)
                    }) { Text(stringResource(R.string.import_replace)) }
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingImportUri = null }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.plants_title)) },
                actions = {
                    IconButton(onClick = onOpenCalendar) {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = stringResource(R.string.calendar_title),
                        )
                    }
                    IconButton(onClick = onOpenStats) {
                        Icon(
                            Icons.Outlined.BarChart,
                            contentDescription = stringResource(R.string.stats_title),
                        )
                    }
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            Icons.Outlined.MoreVert,
                            contentDescription = stringResource(R.string.more_options),
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.export_backup)) },
                            leadingIcon = { Icon(Icons.Outlined.FileDownload, contentDescription = null) },
                            onClick = {
                                menuExpanded = false
                                exportLauncher.launch(BackupManager.SUGGESTED_FILE_NAME)
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.import_backup)) },
                            leadingIcon = { Icon(Icons.Outlined.FileUpload, contentDescription = null) },
                            onClick = {
                                menuExpanded = false
                                importLauncher.launch(arrayOf(BackupManager.MIME_TYPE, "application/octet-stream"))
                            },
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlant,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_plant))
            }
        },
    ) { inner ->
        val filtersActive by viewModel.hasActiveFilters.collectAsStateWithLifecycle()
        // Hide the filter bar only when the garden is genuinely empty.
        val showFilterBar = plants.isNotEmpty() || filtersActive

        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            if (showFilterBar) {
                FilterBar(
                    query = query,
                    selectedEras = selectedEras,
                    selectedType = selectedType,
                    filtersActive = filtersActive,
                    onQueryChange = viewModel::setQuery,
                    onToggleEra = viewModel::toggleEra,
                    onTypeChange = viewModel::setType,
                    onClear = viewModel::clearFilters,
                )
            }
            when {
                plants.isNotEmpty() -> LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(plants, key = { it.id }) { plant ->
                        PlantCard(plant = plant, onClick = { onOpenPlant(plant.id) })
                    }
                }

                filtersActive -> Box(
                    Modifier.fillMaxWidth().weight(1f).padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        stringResource(R.string.no_matching_plants),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }

                else -> EmptyState(Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBar(
    query: String,
    selectedEras: Set<RoseEra>,
    selectedType: RoseType?,
    filtersActive: Boolean,
    onQueryChange: (String) -> Unit,
    onToggleEra: (RoseEra) -> Unit,
    onTypeChange: (RoseType?) -> Unit,
    onClear: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.search_plants)) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.clear_search))
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoseEra.entries.forEach { era ->
                FilterChip(
                    selected = era in selectedEras,
                    onClick = { onToggleEra(era) },
                    label = { Text(era.displayName) },
                )
            }
            Box {
                var typeMenu by remember { mutableStateOf(false) }
                FilterChip(
                    selected = selectedType != null,
                    onClick = { typeMenu = true },
                    label = { Text(selectedType?.displayName ?: stringResource(R.string.filter_type)) },
                    trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, contentDescription = null) },
                )
                DropdownMenu(expanded = typeMenu, onDismissRequest = { typeMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.filter_all_types)) },
                        onClick = { onTypeChange(null); typeMenu = false },
                    )
                    RoseType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.displayName) },
                            onClick = { onTypeChange(type); typeMenu = false },
                        )
                    }
                }
            }
            if (filtersActive) {
                TextButton(onClick = onClear) { Text(stringResource(R.string.clear_filters)) }
            }
        }
    }
}

@Composable
private fun PlantCard(plant: Plant, onClick: () -> Unit) {
    androidx.compose.material3.Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                if (!plant.photoPath.isNullOrBlank()) {
                    AsyncImage(
                        model = File(plant.photoPath),
                        contentDescription = plant.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.LocalFlorist,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                val subtitle = buildList {
                    plant.location?.takeIf { it.isNotBlank() }?.let { add(it) }
                    plant.plantingDateMillis?.let {
                        add(DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(it)))
                    }
                }.joinToString(" · ")
                if (subtitle.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Place,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.LocalFlorist,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_plants_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.empty_plants_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
