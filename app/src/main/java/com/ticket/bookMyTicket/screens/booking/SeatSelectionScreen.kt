package com.ticket.bookMyTicket.screens.booking

import SeatItem
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ticket.bookMyTicket.utils.SeatGenerateHelper
import com.ticket.bookMyTicket.utils.TheatreLayoutRegistry

// Deep cinematic background colors
private val BgDark = Color(0xFF0A0A0F)
private val BgMid = Color(0xFF0F0F1A)
private val AccentRed = Color(0xFFE50914)
private val AccentGreen = Color(0xFF00E676)
private val GlassWhite = Color.White.copy(alpha = 0.05f)
private val GlassBorder = Color.White.copy(alpha = 0.10f)

@Composable
fun SeatSelectionScreen(
    navController: NavController,
    movieId: Int,
    theatreId: String,
    showTime: String,
    trailerId: String
) {
    val config = remember { TheatreLayoutRegistry.getConfig(theatreId) }
    val allSeats = remember { SeatGenerateHelper.generateSeats(theatreId) }
    var selectedSeats by remember { mutableStateOf(setOf<String>()) }

    val totalPrice = allSeats
        .filter { selectedSeats.contains(it.seatId) }
        .sumOf { it.price }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BgDark, BgMid, BgDark)
                )
            )
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ──────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = showTime,
                        color = Color.White.copy(alpha = 0.45f),
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Select Your Seats",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.weight(1f))
                if (selectedSeats.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(AccentGreen.copy(alpha = 0.15f))
                            .border(1.dp, AccentGreen.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${selectedSeats.size} selected",
                            color = AccentGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ── Trailer (rounded) ────────────────────────────────────
            MovieTrailer(videoId = trailerId) // replace with real trailer id

            Spacer(modifier = Modifier.height(20.dp))

            // ── Cinema Screen ────────────────────────────────────────
            CinemaScreen()

            Spacer(modifier = Modifier.height(20.dp))

            // ── Seat Map ─────────────────────────────────────────────
            // Outer: vertical scroll for all rows
            // Inner: horizontal scroll so every seat is reachable by sliding
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                val verticalScrollState = rememberScrollState()
                val horizontalScrollState = rememberScrollState()

                val groupedRows = remember(allSeats) {
                    allSeats.groupBy { it.row }.toList()
                }
                val totalRows = groupedRows.size

                // Outer: fills width, centers the scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(verticalScrollState)
                        .padding(bottom = 120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Horizontal scroll — wraps the grid tightly (wrapContentWidth)
                    // so it never forces left-align. The outer Column centres it.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(horizontalScrollState),
                        contentAlignment = Alignment.Center          // ← key: centres narrow grids
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .wrapContentWidth()                  // shrink-wrap to seat content
                                .padding(horizontal = 8.dp)
                        ) {
                            groupedRows.forEachIndexed { rowIndex, (_, rowSeats) ->

                                val totalSeats = rowSeats.size
                                val elevationFraction = rowIndex.toFloat() / (totalRows - 1).coerceAtLeast(1)

                                // ── Theatre elevation (row tilt + rise) ──────────────────
                                val rowRotationX     = elevationFraction * 18f
                                val rowElevationOffsetY = -(elevationFraction * 14f)
                                val rowScale         = 1f - (elevationFraction * 0.08f)

                                // ── Curve strength: back rows curve more than front rows ──
                                // Front row barely curves; recliner rows at the back have the
                                // most pronounced arc — matches real stadium geometry.
                                val curveDepthDp = 8f + elevationFraction * 18f  // 8dp → 26dp

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(vertical = 3.dp)
                                        .graphicsLayer {
                                            rotationX      = rowRotationX
                                            scaleX         = rowScale
                                            scaleY         = rowScale
                                            translationY   = rowElevationOffsetY
                                            cameraDistance = 8f * density
                                        }
                                ) {
                                    // Left row label — sits at the curve edge, arc it too
                                    Text(
                                        text = rowSeats.first().row,
                                        color = Color.White.copy(
                                            alpha = (0.5f - elevationFraction * 0.2f).coerceAtLeast(0.2f)
                                        ),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .graphicsLayer {
                                                // edge of U — rises up by full curveDepth
                                                translationY = -(curveDepthDp * density)
                                            }
                                    )

                                    Spacer(Modifier.width(6.dp))

                                    rowSeats.forEachIndexed { seatIndex, seat ->
                                        if (config.aisleAfterSeat != null && seatIndex == config.aisleAfterSeat) {
                                            Spacer(modifier = Modifier.width(18.dp))
                                        }

                                        // ── Per-seat parabolic curve offset ──────────────
                                        // Normalise seat position: -1.0 (far left) → 0.0 (centre) → +1.0 (far right)
                                        // Arc = curveDepthDp * normalised²  (parabola, always positive = sinks down at edges)
                                        val normPos = (seatIndex - (totalSeats - 1) / 2f) / ((totalSeats - 1) / 2f).coerceAtLeast(1f)
                                        // U-shape: edges pushed UP, centre stays at 0 — concave bowl
                                        val seatCurveOffsetDp = -(curveDepthDp * (normPos * normPos))

                                        val isSelected = selectedSeats.contains(seat.seatId)

                                        Box(
                                            modifier = Modifier.graphicsLayer {
                                                translationY = seatCurveOffsetDp * density
                                            }
                                        ) {
                                            SeatItem(
                                                seat = seat,
                                                isSelected = isSelected
                                            ) {
                                                selectedSeats =
                                                    if (isSelected) selectedSeats - seat.seatId
                                                    else selectedSeats + seat.seatId
                                            }
                                        }
                                    }

                                    Spacer(Modifier.width(6.dp))

                                    // Right row label — mirrors left edge
                                    Text(
                                        text = rowSeats.first().row,
                                        color = Color.White.copy(
                                            alpha = (0.5f - elevationFraction * 0.2f).coerceAtLeast(0.2f)
                                        ),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .graphicsLayer {
                                                translationY = -(curveDepthDp * density)
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            SeatLegend()
                        }
                    }
                }

                // Subtle fade edges to hint scrollability
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(32.dp)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, BgDark.copy(alpha = 0.85f))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .width(32.dp)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                listOf(BgDark.copy(alpha = 0.85f), Color.Transparent)
                            )
                        )
                )
            }
        }

        // ── Booking Bar (glassmorphic, slides up) ─────────────────
        AnimatedVisibility(
            visible = selectedSeats.isNotEmpty(),
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(350)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(250)
            ) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1A1A2E).copy(alpha = 0.97f),
                                Color(0xFF12121E).copy(alpha = 0.99f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                GlassBorder,
                                GlassBorder,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {

                    // Seat tags
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        selectedSeats.take(6).forEach { seatId ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GlassWhite)
                                    .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = seatId,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        if (selectedSeats.size > 6) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GlassWhite)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+${selectedSeats.size - 6}",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {
                            Text(
                                text = "TOTAL",
                                color = Color.White.copy(alpha = 0.45f),
                                fontSize = 10.sp,
                                letterSpacing = 1.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "₹$totalPrice",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Button(
                            onClick = {
                                println("Booking seats: $selectedSeats")
                                // navController.navigate("payment")
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentRed,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text(
                                text = "Book ${selectedSeats.size} ${if (selectedSeats.size == 1) "Seat" else "Seats"}  →",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }
        }
    }
}