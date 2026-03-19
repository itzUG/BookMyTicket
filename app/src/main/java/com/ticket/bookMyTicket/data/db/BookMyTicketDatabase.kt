package com.ticket.bookMyTicket.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookedTicketEntity::class] , version = 1)
abstract class BookMyTicketDatabase: RoomDatabase(){
    abstract fun bookedTicketDao(): BookedTicketDao
}