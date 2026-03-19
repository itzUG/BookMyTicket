package com.ticket.bookMyTicket.screens.moviedetails

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.screens.common.commonUtils.*
import com.ticket.bookMyTicket.screens.common.movieDetailsScreen.HeaderSection

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgDeep      = Color(0xFF080810)
private val BgCard      = Color(0xFF111120)
private val AccentRed   = Color(0xFFE50914)
private val GoldAccent  = Color(0xFFFFD166)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF7070A0)
private val GlassWhite  = Color.White.copy(alpha = 0.06f)
private val GlassBorder = Color.White.copy(alpha = 0.10f)

private const val TMDB_BACKDROP = "https://image.tmdb.org/t/p/w780"
private const val TMDB_POSTER   = "https://image.tmdb.org/t/p/w342"

// ── Root screen ────────────────────────────────────────────────────────────────
@Composable
fun MovieDetailsScreen(
    navController: NavController,
    movieId: Int,
    viewModel: MovieDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val movieDetailsUIState by viewModel.movieDetailsUIState.collectAsState()

    LaunchedEffect(movieId) { viewModel.fetchMovieDetails(movieId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        when (movieDetailsUIState) {
            is MovieDetailsFetchingUiState.Error   -> ErrorView((movieDetailsUIState as MovieDetailsFetchingUiState.Error).message)
            is MovieDetailsFetchingUiState.Loading -> LoadingView()
            is MovieDetailsFetchingUiState.Success -> {
                val movie = (movieDetailsUIState as MovieDetailsFetchingUiState.Success).movieDetails
                MovieDetailContent(movie = movie, navController = navController)
            }
        }
    }
}

// ── Main content ───────────────────────────────────────────────────────────────
@Composable
fun MovieDetailContent(movie: MovieDetailsDto, navController: NavController) {
    Log.d("POPULAR", "Movies: ${movie.credits}")

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp) // space for sticky Book Now
        ) {

            // ── Cinematic Hero ────────────────────────────────────────
            item { CinematicHero(movie) }

            // ── Quick stats strip ─────────────────────────────────────
            item {
                Spacer(Modifier.height(20.dp))
                QuickStatsStrip(movie)
            }

            // ── Overview ──────────────────────────────────────────────
            movie.overview?.takeIf { it.isNotBlank() }?.let { overview ->
                item {
                    SectionBlock(title = "Overview") {
                        Text(
                            text = overview,
                            color = TextMuted,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // ── Genres ────────────────────────────────────────────────
            movie.genres?.takeIf { it.isNotEmpty() }?.let { genres ->
                item {
                    SectionBlock(title = "Genres") {
                        GenresView(genres)
                    }
                }
            }

            // ── Cast ──────────────────────────────────────────────────
            movie.credits?.cast?.takeIf { it.isNotEmpty() }?.let { cast ->
                item {
                    SectionBlock(title = "Cast", fullBleed = true) {
                        CastCrew(cast)
                    }
                }
            }

            // ── Reviews ───────────────────────────────────────────────
            movie.reviews?.results?.takeIf { it.isNotEmpty() }?.let { reviews ->
                item {
                    SectionBlock(title = "Reviews", fullBleed = true) {
                        ReviewSection(reviews)
                    }
                }
            }

            // ── Videos / Trailers ─────────────────────────────────────
            movie.videos?.results?.takeIf { it.isNotEmpty() }?.let { videos ->
                item {
                    SectionBlock(title = "Trailers & Clips", fullBleed = true) {
                        VideoSectionModern(videos)
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }

        // ── Back button (floating) ────────────────────────────────────
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(BgDeep.copy(alpha = 0.7f))
                .border(1.dp, GlassBorder, CircleShape)
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        // ── Sticky Book Now CTA ───────────────────────────────────────
        BookNowBar(
            movie = movie,
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ── Cinematic Hero ─────────────────────────────────────────────────────────────
@Composable
fun CinematicHero(movie: MovieDetailsDto) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
    ) {
        // Backdrop
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("$TMDB_BACKDROP${movie.backdrop_path ?: movie.poster_path}")
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Deep gradient overlay — fades backdrop into BgDeep at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.35f to Color.Transparent,
                            0.65f to BgDeep.copy(alpha = 0.6f),
                            0.85f to BgDeep.copy(alpha = 0.90f),
                            1.0f  to BgDeep
                        )
                    )
                )
        )

        // Poster + title row at bottom of hero
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            // Poster thumbnail
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("$TMDB_POSTER${movie.poster_path}")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(90.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.width(16.dp))

            // Title block
            Column(modifier = Modifier.weight(1f)) {
                // Rating badge
                movie.vote_average?.let { rating ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldAccent.copy(alpha = 0.15f))
                            .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", rating),
                            color = GoldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }

                movie.title?.let {
                    Text(
                        text = it,
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                movie.overview?.takeIf { it.isNotBlank() }?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "\" $it \"",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// ── Quick stats strip ──────────────────────────────────────────────────────────
@Composable
fun QuickStatsStrip(movie: MovieDetailsDto) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        movie.release_date?.take(4)?.let { year ->
            StatChip(label = year, icon = "📅")
        }
        movie.runtime?.takeIf { it > 0 }?.let { mins ->
            StatChip(label = "${mins / 60}h ${mins % 60}m", icon = "⏱")
        }
//        movie.originalLanguage?.uppercase()?.let { lang ->
//            StatChip(label = lang, icon = "🌐")
//        }
//        movie.status?.let { status ->
//            StatChip(label = status, icon = "📡", highlight = status == "Released")
//        }
    }
}

@Composable
fun StatChip(label: String, icon: String, highlight: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (highlight) AccentRed.copy(alpha = 0.12f) else GlassWhite)
            .border(
                1.dp,
                if (highlight) AccentRed.copy(alpha = 0.35f) else GlassBorder,
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = icon, fontSize = 11.sp)
        Spacer(Modifier.width(5.dp))
        Text(
            text = label,
            color = if (highlight) AccentRed.copy(red = 1f, green = 0.5f, blue = 0.5f) else TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Section block wrapper ──────────────────────────────────────────────────────
@Composable
fun SectionBlock(
    title: String,
    fullBleed: Boolean = false,   // true = content stretches edge-to-edge, only heading is padded
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        // Heading always has horizontal padding
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.verticalGradient(listOf(AccentRed.copy(0f), AccentRed, AccentRed.copy(0f)))
                    )
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )
        }

        // Content: full-bleed has no extra padding; normal gets 20dp sides
        if (fullBleed) {
            content()
        } else {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                content()
            }
        }
    }
}

// ── Sticky Book Now bar ────────────────────────────────────────────────────────
@Composable
fun BookNowBar(
    movie: MovieDetailsDto,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Pulse animation on the button
    val infiniteTransition = rememberInfiniteTransition(label = "bookPulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue  = 1.0f,
        animationSpec = infiniteRepeatable(tween(1000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, BgDeep.copy(alpha = 0.95f), BgDeep)
                )
            )
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Glow behind button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp)
                .align(Alignment.Center)
                .blur(24.dp)
                .background(AccentRed.copy(alpha = glowAlpha * 0.35f), RoundedCornerShape(20.dp))
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Price hint
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "FROM",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "₹150",
                    color = GoldAccent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Book Now button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(2f)
                    .height(54.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFB00008), AccentRed, Color(0xFFFF3333))
                        )
                    )
                    .clickable { navController.navigate("booking/${movie.id}") }
            ) {
                Text(
                    text = "Book Now  →",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}