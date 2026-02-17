package com.ticket.bookMyTicket.response.model

data class Seat(
    val seatId: String,
    val row: String,
    val number: Int,
    val type: SeatType,
    val status: SeatStatus,
    val price: Int
)


enum class SeatType {
    REGULAR,
    PREMIUM,
    RECLINER
}

enum class SeatStatus {
    AVAILABLE,
    RESERVED,
    BOOKED
}

