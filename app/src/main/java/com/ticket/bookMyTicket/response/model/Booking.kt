package com.ticket.bookMyTicket.response.model

data class Booking(
    val bookingId: String,
    val userId: String,
    val showId: String,
    val seats: List<Seat>,
    val totalAmount: Int,
    val bookingTime: Long,
    val status: BookingStatus
)


enum class BookingStatus {
    CONFIRMED,
    CANCELLED
}
