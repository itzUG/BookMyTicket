package com.ticket.bookMyTicket.screens.booking

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.*
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.ticket.bookMyTicket.data.db.BookedTicketEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

// ── Design tokens (matches app theme) ─────────────────────────────────────────
private val BgDeep      = Color(0xFF080810)
private val BgCard      = Color(0xFF111120)
private val AccentRed   = Color(0xFFE50914)
private val AccentGreen = Color(0xFF00E676)
private val GoldAccent  = Color(0xFFFFD166)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GlassWhite  = Color.White.copy(alpha = 0.05f)
private val GlassBorder = Color.White.copy(alpha = 0.10f)

@Composable
fun PaymentScreen(
    navController: NavController,
    amount: Int,
    seats: List<String>,
    movieId: Int,
    theatreId: String,
    showTime: String
) {
    var isProcessing by remember { mutableStateOf(false) }
    var isDone       by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Navigate after success animation
    if (isProcessing) {
        LaunchedEffect(Unit) {
            delay(2200)
            isDone = true
            delay(600)

            val db = DatabaseProvider.getDatabase(context)
            val dao = db.bookedTicketDao()

            withContext(Dispatchers.IO) {
                dao.insertTicket(
                    BookedTicketEntity(
                        movieId = movieId,
                        theatreId = theatreId,
                        showTime = showTime,
                        seats = seats.joinToString(","),
                        amount = amount
                    )
                )
            }
            delay(600)
            navController.navigate(
                "ticket_confirm/$amount/${seats.joinToString(",")}/$movieId/$theatreId/$showTime"
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {

        // ── Ambient glows ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(340.dp)
                .offset(x = (-80).dp, y = (-40).dp)
                .blur(130.dp)
                .background(AccentRed.copy(alpha = 0.08f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .blur(110.dp)
                .background(Color(0xFF1A1AFF).copy(alpha = 0.06f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .statusBarsPadding()
        ) {

            Spacer(Modifier.height(16.dp))

            // ── Page title ────────────────────────────────────────────
            Text(
                text = "CHECKOUT",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Payment",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(28.dp))

            // ── Order summary card ────────────────────────────────────
            OrderSummaryCard(
                seats      = seats,
                showTime   = showTime,
                theatreId  = theatreId,
                amount     = amount
            )

            Spacer(Modifier.height(20.dp))

            // ── Payment method card ───────────────────────────────────
            PaymentMethodCard()

            Spacer(Modifier.height(20.dp))

            // ── Secure badge ──────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(AccentGreen, CircleShape)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "256-bit SSL secured · PCI DSS compliant",
                    color = TextMuted,
                    fontSize = 11.sp,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Pay button ────────────────────────────────────────────
            PayButton(
                amount      = amount,
                isProcessing = isProcessing,
                isDone      = isDone,
                onClick     = { if (!isProcessing) isProcessing = true }
            )

            Spacer(Modifier.height(32.dp))
        }

        // ── Full-screen processing overlay ────────────────────────────
        AnimatedVisibility(
            visible = isProcessing,
            enter = fadeIn(tween(400)),
            exit  = fadeOut(tween(300))
        ) {
            ProcessingOverlay(isDone = isDone)
        }
    }
}

// ── Order Summary Card ─────────────────────────────────────────────────────────
@Composable
fun OrderSummaryCard(
    seats: List<String>,
    showTime: String,
    theatreId: String,
    amount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(BgCard)
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Order Summary",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AccentGreen.copy(alpha = 0.12f))
                    .border(1.dp, AccentGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${seats.size} ${if (seats.size == 1) "SEAT" else "SEATS"}",
                    color = AccentGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Seat chips
        val chunkedSeats = seats.chunked(5)
        chunkedSeats.forEach { rowChunk ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                rowChunk.forEach { seat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GlassWhite)
                            .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = seat,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Divider with ticket perforation feel
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.Center)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, GlassBorder, GlassBorder, Color.Transparent)
                        )
                    )
            )
            // Left notch
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = (-10).dp)
                    .background(BgDeep, CircleShape)
            )
            // Right notch
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 10.dp)
                    .background(BgDeep, CircleShape)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Show details
        SummaryRow(label = "Show Time", value = showTime)
        SummaryRow(label = "Theatre",   value = theatreId)
        SummaryRow(label = "Subtotal",  value = "₹${(amount * 0.85).toInt()}")
        SummaryRow(label = "Taxes & Fees", value = "₹${amount - (amount * 0.85).toInt()}")

        Spacer(Modifier.height(12.dp))

        // Total
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "₹$amount",
                color = GoldAccent,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextMuted,   fontSize = 13.sp)
        Text(text = value, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

// ── Payment Method Card ────────────────────────────────────────────────────────
@Composable
fun PaymentMethodCard() {
    var selected by remember { mutableStateOf(0) }

    val methods = listOf(
        Triple("UPI",         "GPay · PhonePe · Paytm", "⚡"),
        Triple("Credit Card", "Visa · Mastercard · Amex", "💳"),
        Triple("Net Banking", "All major banks",          "🏦"),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(BgCard)
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "Payment Method",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(14.dp))

        methods.forEachIndexed { index, (name, subtitle, emoji) ->
            val isSelected = selected == index
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) AccentRed else GlassBorder,
                animationSpec = tween(200), label = "border"
            )
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) AccentRed.copy(alpha = 0.08f) else GlassWhite,
                animationSpec = tween(200), label = "bg"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                    .clickable { selected = index }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(text = emoji, fontSize = 22.sp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name,     color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = subtitle, color = TextMuted,   fontSize = 11.sp)
                }
                // Radio dot
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            if (isSelected) AccentRed else TextMuted.copy(alpha = 0.4f),
                            CircleShape
                        )
                ) {
                    androidx.compose.animation.AnimatedVisibility(visible = isSelected) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(AccentRed, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

// ── Pay Button ─────────────────────────────────────────────────────────────────
@Composable
fun PayButton(
    amount: Int,
    isProcessing: Boolean,
    isDone: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isProcessing) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "btnScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .height(58.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFB00008), AccentRed, Color(0xFFFF4444))
                )
            )
            .clickable(enabled = !isProcessing) { onClick() }
    ) {
        AnimatedContent(
            targetState = isDone,
            transitionSpec = { scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut() },
            label = "btnContent"
        ) { done ->
            if (done) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Done",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Text(
                    text = if (isProcessing) "Processing…" else "Pay  ₹$amount",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// ── Processing Overlay ─────────────────────────────────────────────────────────
@Composable
fun ProcessingOverlay(isDone: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep.copy(alpha = 0.92f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = isDone,
                transitionSpec = { scaleIn(tween(400)) + fadeIn() togetherWith scaleOut() + fadeOut() },
                label = "overlayIcon"
            ) { done ->
                if (done) {
                    // Success checkmark
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(90.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(AccentGreen.copy(0.3f), Color.Transparent)
                                ),
                                CircleShape
                            )
                            .border(2.dp, AccentGreen, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = AccentGreen,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                } else {
                    // Spinning ring
                    SpinningRing()
                }
            }

            Spacer(Modifier.height(28.dp))

            AnimatedContent(
                targetState = isDone,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut() },
                label = "overlayText"
            ) { done ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (done) "Payment Successful!" else "Processing Payment",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (done) "Generating your ticket…" else "Please don't close the app",
                        color = TextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SpinningRing() {
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing)),
        label = "angle"
    )
    Box(
        modifier = Modifier
            .size(90.dp)
            .rotate(angle)
            .drawBehind {
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(Color.Transparent, AccentRed, Color.Transparent)
                    ),
                    startAngle = 0f,
                    sweepAngle = 280f,
                    useCenter   = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5.dp.toPx())
                )
            }
    )
}