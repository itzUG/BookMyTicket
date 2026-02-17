package com.ticket.bookMyTicket.data.remote.firebase.model

data class FirestoreBooking(
    val bookingId: String = "",
    val userId: String = "",
    val showId: String = "",
    val seatIds: List<String> = emptyList(),
    val amount: Int = 0,
    val createdAt: Long = 0L
)
