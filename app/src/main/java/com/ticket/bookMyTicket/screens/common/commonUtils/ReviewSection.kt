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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticket.bookMyTicket.data.remote.tmdb.dto.ReviewDto

private val BgCard      = Color(0xFF111120)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GoldAccent  = Color(0xFFFFD166)
private val GlassWhite  = Color.White.copy(alpha = 0.05f)
private val GlassBorder = Color.White.copy(alpha = 0.10f)

@Composable
fun ReviewSection(reviews: List<ReviewDto>) {
    if (reviews.isEmpty()) return

    LazyRow(
        // Starts at 20dp to align with content, no right clipping
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()   // ← full screen width, scrolls edge to edge
    ) {
        items(reviews.take(6), key = { it.id }) { review ->
            ReviewCard(review)
        }
    }
}

@Composable
fun ReviewCard(review: ReviewDto) {
    val rating = review.author_details.rating

    Column(
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(BgCard)
            .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        // Author row
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF252545), Color(0xFF111120))
                        )
                    )
                    .border(1.dp, GlassBorder, CircleShape)
            ) {
                Text(
                    text = review.author.firstOrNull()?.uppercase() ?: "U",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.author,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Verified Review",
                    color = TextMuted,
                    fontSize = 10.sp,
                    letterSpacing = 0.3.sp
                )
            }

            // Rating badge
            if (rating != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldAccent.copy(alpha = 0.12f))
                        .border(1.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⭐", fontSize = 10.sp)
                        Spacer(Modifier.width(3.dp))
                        Text(
                            text = rating.toString(),
                            color = GoldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

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

        Spacer(Modifier.height(12.dp))

        // Review content
        Text(
            text = review.content,
            color = TextMuted,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}