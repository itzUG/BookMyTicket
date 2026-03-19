package com.ticket.bookMyTicket.screens.booking

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import java.io.FileOutputStream

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgDeep      = Color(0xFF080810)
private val BgCard      = Color(0xFF111120)
private val AccentRed   = Color(0xFFE50914)
private val AccentGreen = Color(0xFF00E676)
private val GoldAccent  = Color(0xFFFFD166)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GlassWhite  = Color.White.copy(alpha = 0.06f)
private val GlassBorder = Color.White.copy(alpha = 0.10f)

// TMDB image base URL
private const val TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/w500"

@Composable
fun TicketConfirmScreen(
    amount: Int,
    seats: List<String>,
    movieId: Int,
    theatreId: String,
    showTime: String,
    viewModel: TicketConfirmViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    // Entrance animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // Ambient glow — green for success feel
        Box(
            modifier = Modifier
                .size(360.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-80).dp)
                .blur(140.dp)
                .background(AccentGreen.copy(alpha = 0.06f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 60.dp)
                .blur(110.dp)
                .background(AccentRed.copy(alpha = 0.07f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            // ── Success header ────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(initialOffsetY = { -30 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Pulsing green checkmark
                    SuccessBadge()
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = "Booking Confirmed!",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Your tickets are ready. Enjoy the show! 🍿",
                        color = TextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Cinema Ticket Card ────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 200)) +
                        slideInVertically(
                            initialOffsetY = { 60 },
                            animationSpec = tween(500, delayMillis = 200, easing = EaseOutCubic)
                        )
            ) {
                TicketCard(
                    uiState   = uiState,
                    seats     = seats,
                    amount    = amount,
                    showTime  = showTime,
                    theatreId = theatreId
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── Action buttons ────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(400, delayMillis = 500))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Share button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(GlassWhite)
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                            .clickable {
                                val movieTitle = (uiState as? TicketConfirmUiState.Success)
                                    ?.movieDetails?.title ?: "Movie"
                                shareTicket(context, seats, amount, showTime, movieTitle)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Share Ticket",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Download PDF button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFB00008), AccentRed)
                                )
                            )
                            .clickable {
                                val movieTitle = (uiState as? TicketConfirmUiState.Success)
                                    ?.movieDetails?.title ?: "Movie"
                                generatePdf(context, seats, amount, showTime, movieTitle, theatreId)
                            }
                    ) {
                        Text(
                            text = "⬇  Download PDF Ticket",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ── Ticket Card ────────────────────────────────────────────────────────────────
@Composable
fun TicketCard(
    uiState: TicketConfirmUiState,
    seats: List<String>,
    amount: Int,
    showTime: String,
    theatreId: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(BgCard)
            .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
    ) {
        // ── Movie poster banner ───────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
        ) {
            when (uiState) {
                is TicketConfirmUiState.Success -> {
                    val posterPath = uiState.movieDetails.poster_path
                        ?: uiState.movieDetails.backdrop_path

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$TMDB_IMAGE_BASE$posterPath")
                            .crossfade(true)
                            .build(),
                        contentDescription = uiState.movieDetails.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay so text is readable
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        BgCard.copy(alpha = 0.7f),
                                        BgCard
                                    )
                                )
                            )
                    )

                    // Movie title overlaid on poster bottom
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        uiState.movieDetails.title?.let {
                            Text(
                                text = it,
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        uiState.movieDetails.overview?.takeIf { it.isNotBlank() }?.let {
                            Text(
                                text = it,
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                is TicketConfirmUiState.Loading -> {
                    // Shimmer placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF1A1A30),
                                        Color(0xFF252545),
                                        Color(0xFF1A1A30)
                                    )
                                )
                            )
                    )
                    CircularProgressIndicator(
                        color = AccentRed,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is TicketConfirmUiState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1A1A30))
                    ) {
                        Text("🎬", fontSize = 48.sp)
                    }
                }
            }
        }

        // ── Ticket body ───────────────────────────────────────────────
        Column(modifier = Modifier.padding(20.dp)) {

            // Seats
            Text(
                text = "SEATS",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                seats.take(8).forEach { seat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AccentRed.copy(alpha = 0.12f))
                            .border(1.dp, AccentRed.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = seat,
                            color = AccentRed.copy(red = 1f, green = 0.4f, blue = 0.4f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (seats.size > 8) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GlassWhite)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text("+${seats.size - 8}", color = TextMuted, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // Ticket perforation divider
            TicketDivider()

            Spacer(Modifier.height(18.dp))

            // Details grid
            Row(modifier = Modifier.fillMaxWidth()) {
                TicketDetailItem(
                    label = "SHOW TIME",
                    value = showTime,
                    modifier = Modifier.weight(1f)
                )
                TicketDetailItem(
                    label = "THEATRE",
                    value = theatreId,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TicketDetailItem(
                    label = "AMOUNT PAID",
                    value = "₹$amount",
                    valueColor = GoldAccent,
                    modifier = Modifier.weight(1f)
                )
                TicketDetailItem(
                    label = "TICKETS",
                    value = "${seats.size} ${if (seats.size == 1) "seat" else "seats"}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(18.dp))

            // Perforation again above QR
            TicketDivider()

            Spacer(Modifier.height(18.dp))

            // QR code placeholder (use a real QR library like zxing-android-embedded for production)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // QR box
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(6.dp)
                ) {
                    // Static QR pattern placeholder — replace with real QR in production
                    QrPlaceholder()
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Scan at Entry",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Show this QR code\nat the theatre gate",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    // Booking ID
                    Text(
                        text = "BK${System.currentTimeMillis().toString().takeLast(8)}",
                        color = AccentGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}

// ── QR Placeholder ─────────────────────────────────────────────────────────────
// Replace with ZXing or QRCode Kotlin library in production
@Composable
fun QrPlaceholder() {
    val dark = Color(0xFF111111)
    val light = Color.White

    // Simplified QR corner pattern to indicate it's a QR code
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(light)
            .padding(4.dp)
    ) {
        // Top-left finder pattern
        FinderPattern(modifier = Modifier.align(Alignment.TopStart).size(22.dp))
        // Top-right finder pattern
        FinderPattern(modifier = Modifier.align(Alignment.TopEnd).size(22.dp))
        // Bottom-left finder pattern
        FinderPattern(modifier = Modifier.align(Alignment.BottomStart).size(22.dp))
        // Centre data cells hint
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.Center)
                .background(dark, RoundedCornerShape(2.dp))
        )
        Text(
            text = "QR",
            color = dark,
            fontSize = 7.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun FinderPattern(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFF111111), RoundedCornerShape(3.dp))
            .padding(3.dp)
            .background(Color.White, RoundedCornerShape(2.dp))
            .padding(3.dp)
            .background(Color(0xFF111111), RoundedCornerShape(1.dp))
    )
}

// ── Success badge ──────────────────────────────────────────────────────────────
@Composable
fun SuccessBadge() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue  = 1.12f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(72.dp)
    ) {
        // Outer pulse ring
        Box(
            modifier = Modifier
                .size(72.dp)
                .graphicsLayer { scaleX = pulseScale; scaleY = pulseScale }
                .background(AccentGreen.copy(alpha = 0.12f), CircleShape)
                .border(1.5.dp, AccentGreen.copy(alpha = 0.3f), CircleShape)
        )
        // Inner solid circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp)
                .background(AccentGreen.copy(alpha = 0.2f), CircleShape)
                .border(2.dp, AccentGreen, CircleShape)
        ) {
            Text("✓", color = AccentGreen, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

// ── Ticket divider (perforation style) ────────────────────────────────────────
@Composable
fun TicketDivider() {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Dashed line
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
        // Left notch cut
        Box(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-10).dp)
                .background(BgDeep, CircleShape)
        )
        // Right notch cut
        Box(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 10.dp)
                .background(BgDeep, CircleShape)
        )
    }
}

// ── Ticket detail item ─────────────────────────────────────────────────────────
@Composable
fun TicketDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = TextPrimary
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── Share ──────────────────────────────────────────────────────────────────────
fun shareTicket(
    context: Context,
    seats: List<String>,
    amount: Int,
    showTime: String,
    movieTitle: String
) {
    val text = """
🎬 $movieTitle — Booking Confirmed!

🪑 Seats   : ${seats.joinToString(", ")}
🕐 Show    : $showTime
💳 Paid    : ₹$amount

Enjoy the movie! 🍿
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Ticket"))
}

// ── Generate PDF ───────────────────────────────────────────────────────────────
fun generatePdf(
    context: Context,
    seats: List<String>,
    amount: Int,
    showTime: String,
    movieTitle: String,
    theatreId: String
) {
    val pdfDocument = PdfDocument()
    val pageInfo    = PdfDocument.PageInfo.Builder(400, 700, 1).create()
    val page        = pdfDocument.startPage(pageInfo)
    val canvas      = page.canvas

    // Background
    val bgPaint = Paint().apply { color = android.graphics.Color.parseColor("#0A0A0F") }
    canvas.drawRect(0f, 0f, 400f, 700f, bgPaint)

    // Header bar
    val redPaint = Paint().apply { color = android.graphics.Color.parseColor("#E50914") }
    canvas.drawRect(0f, 0f, 400f, 8f, redPaint)

    // Title
    val titlePaint = Paint().apply {
        color    = android.graphics.Color.WHITE
        textSize = 22f
        isFakeBoldText = true
    }
    canvas.drawText("🎬 $movieTitle", 24f, 60f, titlePaint)

    // Divider
    val divPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#252545")
        strokeWidth = 1f
    }
    canvas.drawLine(24f, 80f, 376f, 80f, divPaint)

    val labelPaint = Paint().apply {
        color    = android.graphics.Color.parseColor("#7070A0")
        textSize = 12f
    }
    val valuePaint = Paint().apply {
        color    = android.graphics.Color.WHITE
        textSize = 15f
        isFakeBoldText = true
    }
    val goldPaint = Paint().apply {
        color    = android.graphics.Color.parseColor("#FFD166")
        textSize = 18f
        isFakeBoldText = true
    }

    // Details
    val rows = listOf(
        "SEATS"        to seats.joinToString(", "),
        "SHOW TIME"    to showTime,
        "THEATRE"      to theatreId,
        "AMOUNT PAID"  to "₹$amount"
    )

    rows.forEachIndexed { i, (label, value) ->
        val y = 120f + i * 60f
        canvas.drawText(label, 24f, y, labelPaint)
        val paint = if (label == "AMOUNT PAID") goldPaint else valuePaint
        canvas.drawText(value, 24f, y + 22f, paint)
        canvas.drawLine(24f, y + 36f, 376f, y + 36f, divPaint)
    }

    // QR placeholder text
    val qrHintPaint = Paint().apply {
        color    = android.graphics.Color.parseColor("#7070A0")
        textSize = 11f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("[ QR Code — show at theatre gate ]", 200f, 420f, qrHintPaint)
    canvas.drawRect(150f, 430f, 250f, 530f, divPaint)

    // Footer
    val footerPaint = Paint().apply {
        color    = android.graphics.Color.parseColor("#7070A0")
        textSize = 10f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("Generated by BookMyTicket · Thank you!", 200f, 660f, footerPaint)

    pdfDocument.finishPage(page)

    val file = File(
        context.getExternalFilesDir(null),
        "ticket_${movieTitle.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
    )
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_LONG).show()
}