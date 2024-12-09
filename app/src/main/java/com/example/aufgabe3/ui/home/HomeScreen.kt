package com.example.aufgabe3.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.R
import com.example.aufgabe3.model.BookingEntry
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Displays the Home screen of the application.
 *
 * The Home screen includes a top app bar, a list of booking entries, and a floating action button
 * for navigating to the "AddScreen.kt" screen. If no booking entries exist, a placeholder message
 * is displayed.
 *
 * @param navController The navigation controller used to navigate between screens.
 * @param sharedViewModel The shared view model providing the booking entries and actions for
 * modifying the data.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val bookingsEntries by sharedViewModel.bookingsEntries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add")
            }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_booking))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (bookingsEntries.isEmpty()) {
                Text(
                    stringResource(R.string.no_booking_entries),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    items(bookingsEntries) { bookingEntry ->
                        BookingEntryItem(bookingEntry) {
                            sharedViewModel.deleteBookingEntry(bookingEntry)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Displays a single booking entry as a card.
 *
 * Each card shows the booking name and the arrival and departure dates formatted for the user's
 * locale. A delete button is provided for removing the booking entry.
 *
 * @param bookingEntry The booking entry to be displayed, containing the booking details.
 * @param onDeleteClick A lambda function to be invoked when the delete button is clicked.
 */
@Composable
fun BookingEntryItem(
    bookingEntry: BookingEntry,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = bookingEntry.name,
                    style = MaterialTheme.typography.titleMedium
                )
                val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault())
                val date = "${bookingEntry.arrivalDate.format(formatter)} - ${
                    bookingEntry.departureDate.format(formatter)
                }"
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_booking)
                )
            }
        }
    }
}
