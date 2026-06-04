package de.empirius.rosenapp.ui.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import de.empirius.rosenapp.R
import de.empirius.rosenapp.data.RoseEntry
import de.empirius.rosenapp.data.RoseEra
import de.empirius.rosenapp.data.RoseType
import de.empirius.rosenapp.ui.rememberApp
import java.io.File
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantEditScreen(
    plantId: Long?,
    onDone: () -> Unit,
    onCancel: () -> Unit,
) {
    val app = rememberApp()
    val viewModel: PlantEditViewModel = viewModel(
        factory = viewModelFactory {
            initializer { PlantEditViewModel(app.repository, app.photoStorage, plantId) }
        },
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { success -> viewModel.onCaptureResult(success) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri -> viewModel.onPhotoPicked(uri) }

    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (viewModel.isEditing) R.string.edit_plant_title else R.string.new_plant_title,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.cancel))
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.save(onDone) }) {
                        Text(
                            stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PhotoArea(
                photoPath = viewModel.photoPath,
                onTakePhoto = { cameraLauncher.launch(viewModel.prepareCapture()) },
                onPickPhoto = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
            )

            NameField(
                name = viewModel.name,
                suggestions = viewModel.nameSuggestions,
                isError = viewModel.showNameError,
                onNameChange = viewModel::onNameChange,
                onRoseSelected = viewModel::onRoseSelected,
            )

            OutlinedTextField(
                value = viewModel.latinName,
                onValueChange = viewModel::onLatinNameChange,
                label = { Text(stringResource(R.string.field_latin_name)) },
                placeholder = { Text(stringResource(R.string.field_latin_placeholder)) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.fillMaxWidth(),
            )

            ClassificationDropdown(
                label = stringResource(R.string.field_type),
                notSetLabel = stringResource(R.string.not_set),
                options = RoseType.entries,
                selected = viewModel.type,
                optionLabel = { it.displayName },
                onSelect = viewModel::onTypeChange,
            )

            ClassificationDropdown(
                label = stringResource(R.string.field_era),
                notSetLabel = stringResource(R.string.not_set),
                options = RoseEra.entries,
                selected = viewModel.era,
                optionLabel = { it.displayName },
                onSelect = viewModel::onEraChange,
            )

            OutlinedTextField(
                value = viewModel.origin,
                onValueChange = viewModel::onOriginChange,
                label = { Text(stringResource(R.string.field_origin)) },
                placeholder = { Text(stringResource(R.string.field_origin_placeholder)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = viewModel.awards,
                onValueChange = viewModel::onAwardsChange,
                label = { Text(stringResource(R.string.field_awards)) },
                placeholder = { Text(stringResource(R.string.field_awards_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = viewModel.location,
                onValueChange = viewModel::onLocationChange,
                label = { Text(stringResource(R.string.field_location)) },
                placeholder = { Text(stringResource(R.string.field_location_placeholder)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            DateField(
                millis = viewModel.plantingDateMillis,
                onPick = { showDatePicker = true },
                onClear = { viewModel.onDateChange(null) },
            )

            OutlinedTextField(
                value = viewModel.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text(stringResource(R.string.field_notes)) },
                placeholder = { Text(stringResource(R.string.field_notes_placeholder)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = viewModel.plantingDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDateChange(state.selectedDateMillis)
                    showDatePicker = false
                }) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        ) {
            DatePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameField(
    name: String,
    suggestions: List<RoseEntry>,
    isError: Boolean,
    onNameChange: (String) -> Unit,
    onRoseSelected: (RoseEntry) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val showMenu = expanded && suggestions.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = showMenu,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                onNameChange(it)
                expanded = true
            },
            label = { Text(stringResource(R.string.field_name)) },
            placeholder = { Text(stringResource(R.string.field_name_placeholder)) },
            isError = isError,
            supportingText = {
                Text(
                    stringResource(
                        if (isError) R.string.name_required else R.string.field_name_hint,
                    ),
                )
            },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMenu) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = showMenu,
            onDismissRequest = { expanded = false },
        ) {
            suggestions.forEach { entry ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(entry.name, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                entry.latinName,
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    onClick = {
                        onRoseSelected(entry)
                        expanded = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> ClassificationDropdown(
    label: String,
    notSetLabel: String,
    options: List<T>,
    selected: T?,
    optionLabel: (T) -> String,
    onSelect: (T?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selected?.let(optionLabel) ?: notSetLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(notSetLabel) },
                onClick = {
                    onSelect(null)
                    expanded = false
                },
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun PhotoArea(
    photoPath: String?,
    onTakePhoto: () -> Unit,
    onPickPhoto: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (!photoPath.isNullOrBlank()) {
                AsyncImage(
                    model = File(photoPath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onTakePhoto, modifier = Modifier.weight(1f)) {
                Icon(Icons.Outlined.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(if (photoPath.isNullOrBlank()) R.string.take_photo else R.string.retake_photo),
                )
            }
            OutlinedButton(onClick = onPickPhoto, modifier = Modifier.weight(1f)) {
                Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.choose_photo))
            }
        }
    }
}

@Composable
private fun DateField(
    millis: Long?,
    onPick: () -> Unit,
    onClear: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(onClick = onPick, modifier = Modifier.weight(1f)) {
            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = millis?.let {
                    DateFormat.getDateInstance(DateFormat.LONG).format(Date(it))
                } ?: stringResource(R.string.field_planting_date),
            )
        }
        if (millis != null) {
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = onClear) { Text(stringResource(R.string.clear_date)) }
        }
    }
}
