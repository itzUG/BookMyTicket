package com.ticket.bookMyTicket.data.remote.tmdb.dto

data class Theatre(
    val id: String,
    val name: String,
    val city: String,
    val showTimes: List<String>
)


val dummyTheatres = listOf(

    // 🔹 CHENNAI
    Theatre(
        id = "CHN1",
        name = "PVR Phoenix Marketcity",
        city = "Chennai",
        showTimes = listOf("9:00 AM", "12:30 PM", "4:00 PM", "7:30 PM", "10:45 PM")
    ),
    Theatre(
        id = "CHN2",
        name = "INOX Marina Mall",
        city = "Chennai",
        showTimes = listOf("10:15 AM", "1:45 PM", "5:15 PM", "8:45 PM")
    ),
    Theatre(
        id = "CHN3",
        name = "AGS Cinemas OMR",
        city = "Chennai",
        showTimes = listOf("9:30 AM", "1:00 PM", "4:30 PM", "8:00 PM")
    ),
    Theatre(
        id = "CHN4",
        name = "Rohini Silver Screens",
        city = "Chennai",
        showTimes = listOf("10:00 AM", "2:00 PM", "6:00 PM", "10:00 PM")
    ),

    // 🔹 COIMBATORE
    Theatre(
        id = "CBE1",
        name = "KG Cinemas",
        city = "Coimbatore",
        showTimes = listOf("9:00 AM", "12:00 PM", "3:30 PM", "7:00 PM")
    ),
    Theatre(
        id = "CBE2",
        name = "INOX Prozone Mall",
        city = "Coimbatore",
        showTimes = listOf("10:30 AM", "2:00 PM", "5:30 PM", "9:00 PM")
    ),
    Theatre(
        id = "CBE3",
        name = "Broadway Cinemas",
        city = "Coimbatore",
        showTimes = listOf("9:45 AM", "1:15 PM", "4:45 PM", "8:15 PM")
    ),

    // 🔹 BANGALORE
    Theatre(
        id = "BLR1",
        name = "PVR Orion Mall",
        city = "Bangalore",
        showTimes = listOf("9:00 AM", "12:30 PM", "4:00 PM", "7:30 PM", "11:00 PM")
    ),
    Theatre(
        id = "BLR2",
        name = "INOX Garuda Mall",
        city = "Bangalore",
        showTimes = listOf("10:00 AM", "1:30 PM", "5:00 PM", "8:30 PM")
    ),
    Theatre(
        id = "BLR3",
        name = "Cinepolis Forum Mall",
        city = "Bangalore",
        showTimes = listOf("9:15 AM", "12:45 PM", "4:15 PM", "7:45 PM")
    ),

    // 🔹 HYDERABAD
    Theatre(
        id = "HYD1",
        name = "AMB Cinemas",
        city = "Hyderabad",
        showTimes = listOf("9:30 AM", "1:00 PM", "4:30 PM", "8:00 PM")
    ),
    Theatre(
        id = "HYD2",
        name = "PVR Nexus Mall",
        city = "Hyderabad",
        showTimes = listOf("10:15 AM", "2:00 PM", "5:45 PM", "9:30 PM")
    ),
    Theatre(
        id = "HYD3",
        name = "Prasads Multiplex",
        city = "Hyderabad",
        showTimes = listOf("9:00 AM", "12:15 PM", "3:45 PM", "7:15 PM", "10:45 PM")
    )
)


