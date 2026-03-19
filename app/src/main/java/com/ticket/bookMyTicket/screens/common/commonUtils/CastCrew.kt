package com.ticket.bookMyTicket.screens.common.commonUtils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ticket.bookMyTicket.data.remote.tmdb.dto.CastDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.CrewDto

private val BgCard      = Color(0xFF111120)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GlassBorder = Color.White.copy(alpha = 0.10f)

private const val TMDB_PROFILE = "https://image.tmdb.org/t/p/w185"

// ── Cast Section ───────────────────────────────────────────────────────────────
@Composable
fun CastCrew(casts: List<CastDto>) {
    if (casts.isEmpty()) return

    LazyRow(
        // 20dp start aligns with screen content; 20dp end so last card fully scrolls into view
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()   // ← fills full screen width, no clipping
    ) {
        items(casts.take(15), key = { it.id }) { cast ->
            PersonCard(
                name      = cast.name,
                role      = cast.character,
                imagePath = cast.profile_path
            )
        }
    }
}

// ── Crew Section ───────────────────────────────────────────────────────────────
@Composable
fun CrewSection(crew: List<CrewDto>) {
    if (crew.isEmpty()) return

    LazyRow(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(crew.take(15), key = { it.id }) { member ->
            PersonCard(
                name      = member.name,
                role      = member.job,
                imagePath = member.profile_path
            )
        }
    }
}

// ── Person Card ────────────────────────────────────────────────────────────────
@Composable
fun PersonCard(
    name: String,
    role: String,
    imagePath: String?
) {
    Column(
        modifier = Modifier.width(86.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile image
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A1A30))
                .border(1.dp, GlassBorder, CircleShape)
        ) {
            if (imagePath != null) {
                AsyncImage(
                    model = "$TMDB_PROFILE$imagePath",
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                // Initials fallback
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = name.firstOrNull()?.uppercase() ?: "?",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = name,
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = role,
            color = TextMuted,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}