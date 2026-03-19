package com.ticket.bookMyTicket.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookedTicketDao {

    @Insert
    suspend fun insertTicket(ticketEntity: BookedTicketEntity)

    @Query("SELECT * FROM tickets ORDER BY id DESC")
    suspend fun getAllTickets(): List<BookedTicketEntity>

}
