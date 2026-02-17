package com.ticket.bookMyTicket.screens.common.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import kotlin.math.absoluteValue
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDto

// ------------------ SECTION WITH BLUR BACKGROUND ------------------

@Composable
fun UpcomingMovieSection(
    items: List<MovieDto>,
    onMovieClick: (Int) -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { items.size }
    )

    Box(
        modifier = Modifier
            .height(340.dp)
            .fillMaxHeight()
    ) {

        // -------- BLURRED BACKGROUND --------
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w780${items[pagerState.currentPage].poster_path}",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .blur(16.dp)
                .graphicsLayer { alpha = 0.6f }
        )

        // -------- DARK OVERLAY --------
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.75f),
                            Color.Transparent
                        )
                    )
                )
        )

        UpComingMovieCard(
            items = items,
            pagerState = pagerState,
            onMovieClick = { movieId ->
                onMovieClick(movieId)
            }
        )
    }
}

@Composable
fun UpComingMovieCard(
    items: List<MovieDto>,
    pagerState: PagerState,
    onMovieClick: (Int) -> Unit
) {

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 100.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) { page ->

        val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).coerceIn(-1f, 1f).absoluteValue

        val scale = lerp(
            start = 0.88f,
            stop = 1f,
            fraction = 1f - pageOffset
        )

        val rotation = lerp(
            start = 12f,
            stop = 0f,
            fraction = 1f - pageOffset
        )

        var alpha = lerp(
            start = 0.6f,
            stop = 1f,
            fraction = 1f - pageOffset
        )

        var translationX = lerp(
            start = 60f,
            stop = 0f,
            fraction = 1f - pageOffset
        )

        Card(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale

                    rotationY =
                        if (page < pagerState.currentPage) -rotation else rotation

                    translationX =
                        if (page < pagerState.currentPage) -translationX else translationX

                    alpha = alpha

                    cameraDistance = 30f * density
                }

                .height(300.dp)
                .width(230.dp),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            onClick = {
                onMovieClick(items[page].id)
            }
        ) {

            Box {

                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w780${items[page].poster_path}",
                    contentDescription = items[page].title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                Text(
                    text = items[page].title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                )
            }
        }
    }
}
