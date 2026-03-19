package com.ticket.bookMyTicket.screens.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel


private val BgDeep      = Color(0xFF080810)
private val BgCard      = Color(0xFF111120)
private val AccentRed   = Color(0xFFE50914)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GlassBorder = Color.White.copy(alpha = 0.09f)



@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // Ambient glow
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-40).dp)
                .blur(120.dp)
                .background(Color(0xFF1A1AFF).copy(alpha = 0.05f), CircleShape)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // ── Profile header ────────────────────────────────────────
            item { ProfileHeader(uiState) }

            // ── Content ───────────────────────────────────────────────
            when (uiState) {
                is ProfileUiState.Loading -> {
                    item { LoadingState() }
                }
                is ProfileUiState.Empty -> {
                    item { EmptyState() }
                }
                is ProfileUiState.Error -> {
                    item {
                        ErrorState((uiState as ProfileUiState.Error).message) {
                            viewModel.loadTickets()
                        }
                    }
                }
                is ProfileUiState.Success -> {
                    val items = (uiState as ProfileUiState.Success).items

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp).height(16.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(AccentRed.copy(0f), AccentRed, AccentRed.copy(0f))
                                        )
                                    )
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Booking History",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AccentRed.copy(alpha = 0.12f))
                                    .border(1.dp, AccentRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${items.size} tickets",
                                    color = AccentRed.copy(red = 1f, green = 0.5f, blue = 0.5f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    itemsIndexed(items) { index, item ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(item.ticket.id) {
                            kotlinx.coroutines.delay(index * 70L)
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(300)) + slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = tween(350, easing = EaseOutCubic)
                            )
                        ) {
                            TicketHistoryCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier, valueColor: Color = TextPrimary) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BgCard)
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 8.dp)
    ) {
        Text(
            text = value,
            color = valueColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            color = TextMuted,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp, bottom = 40.dp)
    ) {
        Text("🎟", fontSize = 52.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No bookings yet",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Your ticket history will appear here",
            color = TextMuted,
            fontSize = 13.sp
        )
    }
}

@Composable
fun LoadingState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp)
    ) {
        CircularProgressIndicator(
            color = AccentRed,
            strokeWidth = 2.dp,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Loading your tickets…",
            color = TextMuted,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp, bottom = 40.dp, start = 32.dp, end = 32.dp)
    ) {
        Text("⚠️", fontSize = 40.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Something went wrong",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = message,
            color = TextMuted,
            fontSize = 12.sp
        )
        Spacer(Modifier.height(20.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(AccentRed.copy(alpha = 0.12f))
                .border(1.dp, AccentRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable { onRetry() }
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Try again",
                color = AccentRed.copy(red = 1f, green = 0.5f, blue = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}