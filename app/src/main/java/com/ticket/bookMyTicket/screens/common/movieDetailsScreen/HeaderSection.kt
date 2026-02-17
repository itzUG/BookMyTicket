package com.ticket.bookMyTicket.screens.common.movieDetailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto

@Composable
fun HeaderSection(movie: MovieDetailsDto) {

    val imagePrefix = "https://image.tmdb.org/t/p/w780"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
    ) {

        // ✅ 1. Backdrop Hero Image
        AsyncImage(
            model = "$imagePrefix${movie.backdrop_path}",
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ✅ 2. Gradient Overlay (Netflix style)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // ✅ 3. Content Layer (Poster + Info)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(18.dp),
            verticalAlignment = Alignment.Bottom
        ) {

            // 🎞 Poster Card
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .width(125.dp)
                    .height(185.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // 🎬 Movie Info
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Title
                Text(
                    text = movie.title ?: "Unknown Title",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Overview
                Text(
                    text = movie.overview ?:"Unknown Title",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ✅ Info Chips Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    MovieChip("⭐ ${movie.vote_average.toString().take(3)}")

                    movie.release_date?.let {
                        MovieChip(it.take(4))
                    }

                    movie.runtime?.let {
                        MovieChip("${it} min")
                    }
                }
            }
        }
    }
}

@Composable
fun MovieChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.12f)
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
