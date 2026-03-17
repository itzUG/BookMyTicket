package com.ticket.bookMyTicket.screens.booking

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.Theatre
import com.ticket.bookMyTicket.data.remote.tmdb.dto.VideoDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.VideoResponseDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.dummyTheatres
import com.ticket.bookMyTicket.screens.common.commonUtils.ErrorView
import com.ticket.bookMyTicket.screens.common.commonUtils.LoadingView
import com.ticket.bookMyTicket.screens.common.movieDetailsScreen.HeaderSection
import kotlinx.coroutines.delay


private val BgDeep       = Color(0xFF080810)
private val BgCard       = Color(0xFF111120)
private val BgCardBorder = Color(0xFF252540)
private val AccentRed    = Color(0xFFE50914)
private val AccentRedDim = Color(0xFF8B040B)
private val GoldAccent   = Color(0xFFFFD166)
private val TextPrimary  = Color(0xFFF0F0FF)
private val TextMuted    = Color(0xFF7070A0)
private val GlassWhite   = Color.White.copy(alpha = 0.06f)
private val GlassBorder  = Color.White.copy(alpha = 0.10f)


@Composable
fun SelectCinemaScreen(
    navController: NavController,
    movieId: Int,
    viewModel: SelectCinemaViewModel = viewModel()
) {
    val movieDetailsUIState by viewModel.movieDetailsUIState.collectAsState()
    LaunchedEffect(movieId) { viewModel.fetchMovieDetails(movieId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // Ambient background glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-60).dp, y = 80.dp)
                .blur(120.dp)
                .background(AccentRed.copy(alpha = 0.07f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = 200.dp)
                .blur(100.dp)
                .background(Color(0xFF1A1AFF).copy(alpha = 0.05f), CircleShape)
        )

        MovieDetailsLazyScreen(navController, movieId, movieDetailsUIState)
    }
}

// ── State router ───────────────────────────────────────────────────────────────
@Composable
fun MovieDetailsLazyScreen(
    navController: NavController,
    movieId: Int,
    movieDetailsState: SelectCinemaViewModelUiState
) {
    when (movieDetailsState) {
        is SelectCinemaViewModelUiState.Error   -> ErrorView(movieDetailsState.message)
        is SelectCinemaViewModelUiState.Loading -> LoadingView()
        is SelectCinemaViewModelUiState.Success -> DetailScreenUI(movieDetailsState.movieDetails, navController)
    }
}

// ── Main content ───────────────────────────────────────────────────────────────
@Composable
fun DetailScreenUI(movie: MovieDetailsDto, navController: NavController) {

    val cities = dummyTheatres.map { it.city }.distinct()
    var selectedCity by remember { mutableStateOf(cities.first()) }
    val theatres = remember(selectedCity) { dummyTheatres.filter { it.city == selectedCity } }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {

        // Movie hero header
        item { HeaderSection(movie) }

        // Section heading
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AccentRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Choose Your City",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp
                )
            }
        }

        // City pill selector
        item {
            CitySelector(
                cities = cities,
                selectedCity = selectedCity,
                onCitySelected = { selectedCity = it }
            )
        }

        // Theatres count badge
        item {
            AnimatedContent(
                targetState = selectedCity,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "cityLabel"
            ) { city ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "${theatres.size} theatres",
                        color = TextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "in $city",
                        color = TextPrimary.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Theatre cards
        itemsIndexed(theatres) { index, theatre ->
            val trailerId = movie.videos?.results
                ?.firstOrNull { it.site == "YouTube" }
                ?.key

            TheatreCard(
                theatre = theatre,
                movieId = movie.id,
                index = index,
                navController = navController,
                trailer = trailerId ?: ""
            )
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

// ── City Selector ──────────────────────────────────────────────────────────────
@Composable
fun CitySelector(
    cities: List<String>,
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(cities) { city ->
            val isSelected = city == selectedCity

            val bgColor by animateColorAsState(
                targetValue = if (isSelected) AccentRed else GlassWhite,
                animationSpec = tween(250),
                label = "cityBg"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) AccentRed else GlassBorder,
                animationSpec = tween(250),
                label = "cityBorder"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else TextMuted,
                animationSpec = tween(250),
                label = "cityText"
            )
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0.95f,
                animationSpec = spring(dampingRatio = 0.6f),
                label = "cityScale"
            )

            Box(
                modifier = Modifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .clip(RoundedCornerShape(50))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(50))
                    .clickable { onCitySelected(city) }
                    .padding(horizontal = 18.dp, vertical = 9.dp)
            ) {
                Text(
                    text = city,
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

// ── Theatre Card ───────────────────────────────────────────────────────────────
@Composable
fun TheatreCard(
    theatre: Theatre,
    movieId: Int?,
    index: Int,
    navController: NavController,
    trailer: String
) {
    // Staggered entrance animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(theatre.id) {
        delay(index * 80L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + slideInVertically(
            initialOffsetY = { 40 },
            animationSpec = tween(350, easing = EaseOutCubic)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            // Card glow on left edge — accent line
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(60.dp)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.verticalGradient(
                            listOf(AccentRed.copy(alpha = 0f), AccentRed, AccentRed.copy(alpha = 0f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp) // offset past the accent line
                    .clip(RoundedCornerShape(20.dp))
                    .background(BgCard)
                    .border(1.dp, BgCardBorder, RoundedCornerShape(20.dp))
                    .padding(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 18.dp)
            ) {

                // Theatre name + badge row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = theatre.name,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFF00E676), CircleShape)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = "${theatre.showTimes.size} shows today",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Format badge (could be driven by data later)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(AccentRedDim, AccentRed)
                                )
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "DOLBY",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

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

                Spacer(Modifier.height(14.dp))

                // Showtime chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(theatre.showTimes) { timeIndex, time ->
                        ShowtimeChip(
                            time = time,
                            index = timeIndex,
                            onClick = {
                                navController.navigate("seat/$movieId/${theatre.id}/$time/$trailer")
                            }
                        )
                    }
                }
            }
        }
    }
}

// ── Showtime Chip ──────────────────────────────────────────────────────────────
@Composable
fun ShowtimeChip(
    time: String,
    index: Int,
    onClick: () -> Unit
) {
    // Alternate chip styles to add visual rhythm
    val isHighlighted = index == 0  // first show of the day gets a gold accent

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isHighlighted)
                    Modifier.background(
                        Brush.linearGradient(listOf(Color(0xFF3A2800), Color(0xFF2A1800)))
                    ).border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                else
                    Modifier.background(GlassWhite)
                        .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = time,
                color = if (isHighlighted) GoldAccent else TextPrimary.copy(alpha = 0.85f),
                fontSize = 13.sp,
                fontWeight = if (isHighlighted) FontWeight.SemiBold else FontWeight.Normal,
                letterSpacing = 0.2.sp
            )
            if (isHighlighted) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "FIRST SHOW",
                    color = GoldAccent.copy(alpha = 0.7f),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}