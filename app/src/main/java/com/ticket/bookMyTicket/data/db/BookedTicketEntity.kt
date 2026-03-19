package com.ticket.bookMyTicket.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class BookedTicketEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val movieId: Int,
    val theatreId: String,
    val showTime: String,
    val seats: String,
    val amount: Int
)