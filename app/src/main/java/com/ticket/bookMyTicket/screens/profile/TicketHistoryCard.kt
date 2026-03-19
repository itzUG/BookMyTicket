package com.ticket.bookMyTicket.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest


private val BgDeep      = Color(0xFF080810)
private val BgCard      = Color(0xFF111120)
private val AccentRed   = Color(0xFFE50914)
private val AccentGreen = Color(0xFF00E676)
private val GoldAccent  = Color(0xFFFFD166)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GlassWhite  = Color.White.copy(alpha = 0.05f)
private val GlassBorder = Color.White.copy(alpha = 0.09f)

private const val TMDB_POSTER   = "https://image.tmdb.org/t/p/w342"
@Composable
fun TicketHistoryCard(item: TicketWithMovie) {
    val ticket = item.ticket
    val movie  = item.movie
    val seats  = ticket.seats.split(",")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Red left accent bar
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(56.dp)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(50))
                .background(
                    Brush.verticalGradient(
                        listOf(AccentRed.copy(0f), AccentRed, AccentRed.copy(0f))
                    )
                )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 3.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(BgCard)
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                .padding(12.dp)
        ) {
            // Movie poster
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1A1A30))
            ) {
                val posterPath = movie?.poster_path ?: movie?.backdrop_path
                if (posterPath != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$TMDB_POSTER$posterPath")
                            .crossfade(true)
                            .build(),
                        contentDescription = movie?.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black.copy(0.3f))
                                )
                            )
                    )
                } else {
                    // Fallback
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("🎬", fontSize = 24.sp)
                    }
                }
            }

            Spacer(Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie?.title ?: "Movie #${ticket.movieId}",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕐", fontSize = 11.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = ticket.showTime,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏛", fontSize = 11.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = ticket.theatreId,
                        color = TextMuted,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Seat chips (show max 4)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    seats.take(4).forEach { seat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AccentRed.copy(alpha = 0.10f))
                                .border(1.dp, AccentRed.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = seat.trim(),
                                color = AccentRed.copy(red = 1f, green = 0.45f, blue = 0.45f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    if (seats.size > 4) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GlassWhite)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "+${seats.size - 4}",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // Amount
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${ticket.amount}",
                    color = GoldAccent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AccentGreen.copy(alpha = 0.10f))
                        .border(1.dp, AccentGreen.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Paid",
                        color = AccentGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
