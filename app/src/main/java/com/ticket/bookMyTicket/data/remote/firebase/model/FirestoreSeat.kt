package com.ticket.bookMyTicket.data.remote.firebase.model

data class FirestoreSeat(
    val seatId: String = "",
    val status: String = "AVAILABLE",
    val userId: String? = null,
    val reservedAt: Long? = null
)
