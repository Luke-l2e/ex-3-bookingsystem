package com.example.aufgabe3.model

import java.time.LocalDate

/**
 * Represents a booking entry.
 *
 * This data class encapsulates the details of a booking, including the arrival and departure
 * dates, as well as the associated name. It serves as the model for storing and managing
 * booking information.
 *
 * @property arrivalDate The date of arrival for the booking.
 * @property departureDate The date of departure for the booking.
 * @property name The name associated with the booking.
 */
data class BookingEntry(val arrivalDate: LocalDate, val departureDate: LocalDate, val name: String)
