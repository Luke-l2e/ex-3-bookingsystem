package com.example.aufgabe3.ui.add

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.R
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * A screen for adding a new booking entry.
 *
 * This composable function provides a UI to input booking details, including the name,
 * arrival date, and departure date. Users can select a date range using a modal date range picker.
 * The screen also handles input validation and saves the booking entry via the shared view model.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param sharedViewModel The shared view model used for adding a new booking entry.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    var name by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf<LocalDate?>(null) }
    var departureDate by remember { mutableStateOf<LocalDate?>(null) }

    var showDateRangePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_booking_entry)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (arrivalDate != null && departureDate != null) {
                    "${arrivalDate!!.format(dateFormatter)} - ${departureDate!!.format(dateFormatter)}"
                } else {
                    ""
                },
                onValueChange = {},
                label = { Text(stringResource(R.string.select_date_range)) },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateRangePicker = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (isBookEntryValid(arrivalDate, departureDate, name)) {
                        sharedViewModel.addBookingEntry(arrivalDate!!, departureDate!!, name.trim())
                        navController.popBackStack()
                    } else {
                        Toast.makeText(
                            navController.context,
                            R.string.invalid_booking_entry,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
    if (showDateRangePicker) {
        DateRangePickerModal(
            onDismiss = { showDateRangePicker = false },
            onDateRangeSelected = { dateRange ->
                val startDate = dateRange.first
                val endDate = dateRange.second
                arrivalDate = startDate?.let { convertToLocalDate(it) }
                departureDate = endDate?.let { convertToLocalDate(it) }
                showDateRangePicker = false
            },

            validateDate = { dateToValidate ->
                !convertToLocalDate(dateToValidate).isBefore(LocalDate.now())
            })
    }
}

/**
 * A modal dialog for selecting a date range.
 *
 * This composable displays a date picker in a modal dialog, allowing users to select a start
 * and end date. The selected date range is returned to the parent composable through a callback.
 *
 * @param onDateRangeSelected Callback invoked when a date range is selected. Returns a pair of
 * start and end date timestamps (in milliseconds) or `null` if no date is selected.
 * @param onDismiss Callback invoked when the dialog is dismissed without selection.
 * @param validateDate A lambda function to validate selectable dates. Should return true for
 * valid dates and false otherwise.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit,
    validateDate: (Long) -> Boolean
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = stringResource(R.string.select_date_range)
                )
            },
            showModeToggle = false, dateValidator = { validateDate(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

/**
 * Converts a timestamp (in milliseconds) to a `LocalDate`.
 *
 * This utility function converts an epoch timestamp to a `LocalDate` object using the
 * system's default time zone.
 *
 * @param timestamp The timestamp in milliseconds to be converted.
 * @return The corresponding `LocalDate` instance.
 */
fun convertToLocalDate(timestamp: Long): LocalDate {
    return timestamp.let {
        LocalDateTime.ofEpochSecond(
            it / 1000, 0, ZoneId.systemDefault().rules.getOffset(
                LocalDateTime.now()
            )
        ).toLocalDate()
    }
}

/**
 * Validates the input data for a booking entry.
 *
 * This utility function checks if the arrival date, departure date, and name are
 * valid. The name must not be null or blank, and both dates must be non-null.
 *
 * @param arrivalDate The arrival date of the booking.
 * @param departureDate The departure date of the booking.
 * @param name The name associated with the booking.
 * @return True if the input data is valid; false otherwise.
 */
fun isBookEntryValid(arrivalDate: LocalDate?, departureDate: LocalDate?, name: String?): Boolean {
    return !(null == arrivalDate || null == departureDate || name.isNullOrBlank())
}