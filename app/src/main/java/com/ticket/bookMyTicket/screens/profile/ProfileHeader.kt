package com.ticket.bookMyTicket.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val GoldAccent  = Color(0xFFFFD166)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GlassBorder = Color.White.copy(alpha = 0.09f)

@Composable
fun ProfileHeader(uiState: ProfileUiState) {
    val ticketCount = if (uiState is ProfileUiState.Success) uiState.items.size else 0
    val totalSpent  = if (uiState is ProfileUiState.Success)
        uiState.items.sumOf { it.ticket.amount } else 0
    val totalSeats  = if (uiState is ProfileUiState.Success)
        uiState.items.sumOf { it.ticket.seats.split(",").size } else 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // Avatar + name row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(listOf(Color(0xFF252545), Color(0xFF111120)))
                    )
                    .border(1.dp, GlassBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text = "My Account",
                    color = TextMuted,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Movie Lover",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Stats strip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard("Bookings",  ticketCount.toString(), Modifier.weight(1f))
            StatCard("Seats",     totalSeats.toString(),  Modifier.weight(1f))
            StatCard("Spent",     "₹$totalSpent",         Modifier.weight(1f), valueColor = GoldAccent)
        }

        Spacer(Modifier.height(24.dp))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, GlassBorder, GlassBorder, Color.Transparent)
                    )
                )
        )
    }
}
