package com.ticket.bookMyTicket.utils

import com.ticket.bookMyTicket.response.model.Seat
import com.ticket.bookMyTicket.response.model.SeatStatus
import com.ticket.bookMyTicket.response.model.SeatType

object SeatGenerateHelper {

    /**
     * Generates the seat list for a specific theatre using its layout config.
     * Row order: first row = closest to screen (Regular), last rows = Recliners at the back.
     */
    fun generateSeats(theatreId: String): List<Seat> {
        val config = TheatreLayoutRegistry.getConfig(theatreId)
        val seats = mutableListOf<Seat>()

        config.rows.forEach { row ->
            for (number in 1..config.seatsPerRow) {
                val type = when (row) {
                    in config.recliners -> SeatType.RECLINER
                    in config.premium   -> SeatType.PREMIUM
                    else                -> SeatType.REGULAR
                }

                val price = when (type) {
                    SeatType.REGULAR  -> 150
                    SeatType.PREMIUM  -> 250
                    SeatType.RECLINER -> 400
                }

                val seatId = "$row$number"
                val status = if (config.bookedSeatIds.contains(seatId))
                    SeatStatus.BOOKED else SeatStatus.AVAILABLE

                seats.add(
                    Seat(
                        seatId  = seatId,
                        row     = row,
                        number  = number,
                        type    = type,
                        status  = status,
                        price   = price
                    )
                )
            }
        }
        return seats
    }
}