package com.example.aufgabe3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.aufgabe3.model.BookingEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

/**
 * ViewModel responsible for managing booking entries.
 *
 * This ViewModel holds a list of booking entries and provides methods for adding and removing
 * entries. The booking data is exposed as a state flow to enable UI components to react to
 * changes in real-time.
 */
class SharedViewModel : ViewModel() {
    private val _bookingsEntries = MutableStateFlow<List<BookingEntry>>(emptyList())
    val bookingsEntries: StateFlow<List<BookingEntry>> = _bookingsEntries

    /**
     * Adds a new booking entry to the list.
     *
     * Creates a new `BookingEntry` object using the provided details and appends it to the current
     * list of booking entries. The updated list is then emitted via the `_bookingsEntries` flow.
     *
     * @param arrivalDate The arrival date for the booking.
     * @param departureDate The departure date for the booking.
     * @param name The name associated with the booking.
     */
    fun addBookingEntry(arrivalDate: LocalDate, departureDate: LocalDate, name: String) {
        val newBookingEntry = BookingEntry(arrivalDate, departureDate, name)
        val updatedList = _bookingsEntries.value + newBookingEntry
        _bookingsEntries.value = updatedList
    }

    /**
     * Deletes an existing booking entry from the list.
     *
     * Removes the specified `BookingEntry` from the current list of booking entries. The updated
     * list is then emitted via the `_bookingsEntries` flow.
     *
     * @param bookingEntry The booking entry to remove from the list.
     */
    fun deleteBookingEntry(bookingEntry: BookingEntry) {
        val updatedList = _bookingsEntries.value.minus(bookingEntry)
        _bookingsEntries.value = updatedList
    }
}