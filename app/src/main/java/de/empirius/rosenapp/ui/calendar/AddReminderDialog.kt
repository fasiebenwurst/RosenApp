package de.empirius.rosenapp.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import de.empirius.rosenapp.R
import de.empirius.rosenapp.data.CareReminder
import de.empirius.rosenapp.data.CareTask
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.data.RecurrenceUnit
import de.empirius.rosenapp.reminder.ReminderSchedule
import androidx.compose.ui.res.stringResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    plants: List<Plant>,
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onSave: (CareReminder) -> Unit,
) {
    var task by remember { mutableStateOf(CareTask.PRUNE) }
    var title by remember { mutableStateOf("") }
    var plant by remember { mutableStateOf<Plant?>(null) }
    var date by remember { mutableStateOf(initialDate) }
    var recurring by remember { mutableStateOf(true) }
    var intervalText by remember { mutableStateOf("4") }
    var unit by remember { mutableStateOf(RecurrenceUnit.WEEKS) }
    var notify by remember { mutableStateOf(true) }
    var showDatePicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp), color = androidx.compose.material3.MaterialTheme.colorScheme.surface) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(stringResource(R.string.add_reminder), style = androidx.compose.material3.MaterialTheme.typography.titleLarge)

                EnumDropdown(
                    label = stringResource(R.string.reminder_task_label),
                    options = CareTask.entries,
                    selected = task,
                    optionLabel = { it.displayName },
                    onSelect = { task = it },
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.reminder_title_label)) },
                    placeholder = { Text(task.displayName) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                PlantDropdown(
                    plants = plants,
                    selected = plant,
                    onSelect = { plant = it },
                )

                TextButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.reminder_start_date) + ": " +
                            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(selected = !recurring, onClick = { recurring = false }),
                    ) {
                        RadioButton(selected = !recurring, onClick = { recurring = false })
                        Text(stringResource(R.string.reminder_one_off))
                    }
                    Spacer(Modifier.width(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(selected = recurring, onClick = { recurring = true }),
                    ) {
                        RadioButton(selected = recurring, onClick = { recurring = true })
                        Text(stringResource(R.string.reminder_repeats))
                    }
                }

                if (recurring) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.reminder_every))
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = intervalText,
                            onValueChange = { new -> intervalText = new.filter { it.isDigit() }.take(3) },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(80.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        EnumDropdown(
                            label = "",
                            options = RecurrenceUnit.entries,
                            selected = unit,
                            optionLabel = { it.displayName },
                            onSelect = { unit = it },
                            modifier = Modifier.width(140.dp),
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.reminder_notify), modifier = Modifier.weight(1f))
                    Switch(checked = notify, onCheckedChange = { notify = it })
                }

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
                    Button(onClick = {
                        val count = if (recurring) intervalText.toIntOrNull()?.coerceAtLeast(1) ?: 1 else 0
                        onSave(
                            CareReminder(
                                plantId = plant?.id,
                                task = task,
                                title = title.ifBlank { task.displayName },
                                startDateMillis = ReminderSchedule.toEpochMillis(date),
                                intervalCount = count,
                                intervalUnit = unit,
                                notify = notify,
                            ),
                        )
                    }) { Text(stringResource(R.string.save)) }
                }
            }
        }
    }

    if (showDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = ReminderSchedule.toEpochMillis(date),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { date = ReminderSchedule.toLocalDate(it) }
                    showDatePicker = false
                }) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.cancel)) }
            },
        ) {
            DatePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EnumDropdown(
    label: String,
    options: List<T>,
    selected: T,
    optionLabel: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = optionLabel(selected),
            onValueChange = {},
            readOnly = true,
            label = if (label.isNotEmpty()) {
                { Text(label) }
            } else {
                null
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = { onSelect(option); expanded = false },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlantDropdown(
    plants: List<Plant>,
    selected: Plant?,
    onSelect: (Plant?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected?.name ?: stringResource(R.string.reminder_no_plant),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.reminder_plant_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.reminder_no_plant)) },
                onClick = { onSelect(null); expanded = false },
            )
            plants.forEach { p ->
                DropdownMenuItem(
                    text = { Text(p.name) },
                    onClick = { onSelect(p); expanded = false },
                )
            }
        }
    }
}
