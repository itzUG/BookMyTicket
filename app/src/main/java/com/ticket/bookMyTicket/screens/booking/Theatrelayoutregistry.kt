package com.ticket.bookMyTicket.utils

data class TheatreLayoutConfig(
    val rows: List<String>,
    val seatsPerRow: Int,
    val aisleAfterSeat: Int?,
    val recliners: Set<String>,
    val premium: Set<String>,
    val bookedSeatIds: Set<String>
)

object TheatreLayoutRegistry {

    fun getConfig(theatreId: String): TheatreLayoutConfig = when (theatreId) {

        // ── CHENNAI ────────────────────────────────────────────────────────────

        // PVR Phoenix — Large multiplex, 12 rows, 14 seats, aisle after seat 6
        "CHN1" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J","K","L"),
            seatsPerRow = 14,
            aisleAfterSeat = 6,
            recliners = setOf("K","L"),
            premium = setOf("G","H","I","J"),
            bookedSeatIds = setOf("A3","A4","B7","B8","C2","D10","E5","E6","G3","H9","K2","K3","L6")
        )

        // INOX Marina — Mid-size, 10 rows, 12 seats, aisle after seat 5
        "CHN2" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J"),
            seatsPerRow = 12,
            aisleAfterSeat = 5,
            recliners = setOf("I","J"),
            premium = setOf("E","F","G","H"),
            bookedSeatIds = setOf("B3","B4","C7","D2","E11","F5","H8","I1","I2","J7","J8")
        )

        // AGS Cinemas OMR — Smaller indie, 8 rows, 10 seats, no aisle
        "CHN3" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H"),
            seatsPerRow = 10,
            aisleAfterSeat = null,
            recliners = setOf("H"),
            premium = setOf("E","F","G"),
            bookedSeatIds = setOf("A5","B3","C8","D1","E6","F4","G9","H2","H3")
        )

        // Rohini Silver Screens — Classic Tamil cinema, 10 rows, 16 seats, aisle after seat 7
        "CHN4" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J"),
            seatsPerRow = 16,
            aisleAfterSeat = 7,
            recliners = setOf("I","J"),
            premium = setOf("E","F","G","H"),
            bookedSeatIds = setOf("A1","A2","B9","C5","C6","D12","E3","F8","G14","H7","I4","I5","J10","J11")
        )

        // ── COIMBATORE ─────────────────────────────────────────────────────────

        // KG Cinemas — Compact, 8 rows, 10 seats, aisle after seat 4
        "CBE1" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H"),
            seatsPerRow = 10,
            aisleAfterSeat = 4,
            recliners = setOf("G","H"),
            premium = setOf("D","E","F"),
            bookedSeatIds = setOf("A3","B7","C2","D5","E9","F1","G4","G5","H8")
        )

        // INOX Prozone — Modern mall multiplex, 10 rows, 12 seats, aisle after seat 5
        "CBE2" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J"),
            seatsPerRow = 12,
            aisleAfterSeat = 5,
            recliners = setOf("I","J"),
            premium = setOf("F","G","H"),
            bookedSeatIds = setOf("B2","B3","C8","D11","E4","F6","G3","H10","I1","J7","J8")
        )

        // Broadway Cinemas — Retro single-screen, 9 rows, 14 seats, aisle after seat 6
        "CBE3" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I"),
            seatsPerRow = 14,
            aisleAfterSeat = 6,
            recliners = setOf("H","I"),
            premium = setOf("E","F","G"),
            bookedSeatIds = setOf("A7","B4","B5","C12","D2","E9","F6","G13","H3","H4","I10")
        )

        // ── BANGALORE ─────────────────────────────────────────────────────────

        // PVR Orion — Premium multiplex, 12 rows, 16 seats, two aisles (aisle after seat 5 and 10)
        // Represented as aisle after seat 7 (centre aisle)
        "BLR1" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J","K","L"),
            seatsPerRow = 16,
            aisleAfterSeat = 7,
            recliners = setOf("J","K","L"),
            premium = setOf("F","G","H","I"),
            bookedSeatIds = setOf("A3","B9","C5","D13","E2","F8","G11","H4","I7","J1","J2","K6","K7","L12")
        )

        // INOX Garuda — 10 rows, 12 seats, aisle after seat 5
        "BLR2" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J"),
            seatsPerRow = 12,
            aisleAfterSeat = 5,
            recliners = setOf("I","J"),
            premium = setOf("E","F","G","H"),
            bookedSeatIds = setOf("A4","B10","C3","D7","E1","F9","G5","H12","I6","J2","J3")
        )

        // Cinepolis Forum — Boutique feel, 8 rows, 10 seats, aisle after seat 4
        "BLR3" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H"),
            seatsPerRow = 10,
            aisleAfterSeat = 4,
            recliners = setOf("G","H"),
            premium = setOf("D","E","F"),
            bookedSeatIds = setOf("A2","B6","C9","D3","E7","F1","G4","G5","H8","H9")
        )

        // ── HYDERABAD ─────────────────────────────────────────────────────────

        // AMB Cinemas — Luxury, 10 rows, 12 seats, large recliner section
        "HYD1" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J"),
            seatsPerRow = 12,
            aisleAfterSeat = 5,
            recliners = setOf("H","I","J"),          // 3 recliner rows!
            premium = setOf("E","F","G"),
            bookedSeatIds = setOf("A5","B3","C8","D11","E2","F7","G10","H1","H2","I5","I6","J9")
        )

        // PVR Nexus — 11 rows, 14 seats, aisle after seat 6
        "HYD2" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J","K"),
            seatsPerRow = 14,
            aisleAfterSeat = 6,
            recliners = setOf("J","K"),
            premium = setOf("F","G","H","I"),
            bookedSeatIds = setOf("A6","B2","C11","D4","E8","F13","G3","H7","I10","J1","J2","K5","K6")
        )

        // Prasads Multiplex — Iconic Hyderabad, 12 rows, 18 seats, aisle after seat 8
        "HYD3" -> TheatreLayoutConfig(
            rows = listOf("A","B","C","D","E","F","G","H","I","J","K","L"),
            seatsPerRow = 18,
            aisleAfterSeat = 8,
            recliners = setOf("K","L"),
            premium = setOf("G","H","I","J"),
            bookedSeatIds = setOf("A9","B5","B6","C14","D2","E11","F7","G16","H3","H4","I9","J13","K1","K2","L8","L9")
        )

        // Default fallback — 10 rows, 10 seats
        else -> TheatreLayoutConfig(
            rows = ('A'..'J').map { it.toString() },
            seatsPerRow = 10,
            aisleAfterSeat = 4,
            recliners = setOf("I","J"),
            premium = setOf("E","F","G","H"),
            bookedSeatIds = setOf("B3","C7","E5","F2","H9")
        )
    }
}