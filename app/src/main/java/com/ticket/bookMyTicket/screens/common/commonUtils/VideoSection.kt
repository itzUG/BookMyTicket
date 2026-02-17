package com.ticket.bookMyTicket.screens.common.commonUtils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
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
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ticket.bookMyTicket.data.remote.tmdb.dto.VideoDto

// ─────────────────────────────────────────────
// 🎬 Netflix Style Video Section
// ─────────────────────────────────────────────

@Composable
fun VideoSectionModern(videos: List<VideoDto>) {

    val context = LocalContext.current

    val youtubeVideos = videos.filter {
        it.site == "YouTube" && it.key.isNotBlank()
    }

    if (youtubeVideos.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {

        // ⭐ Section Heading
        Text(
            text = "Trailers & Videos",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            modifier = Modifier.padding(start = 18.dp, bottom = 14.dp)
        )

        // ✅ Horizontal Scroll Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(youtubeVideos.take(10), key = { it.key }) { video ->
                ModernTrailerCard(
                    video = video,
                    onClick = { openYouTube(context, video.key) }
                )
            }
        }
    }
}


// ─────────────────────────────────────────────
// 🎥 Modern Trailer Card
// ─────────────────────────────────────────────

@Composable
fun ModernTrailerCard(
    video: VideoDto,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    val thumbnail =
        "https://img.youtube.com/vi/${video.key}/hqdefault.jpg"

    Column(
        modifier = Modifier
            .width(250.dp)
            .clickable { onClick() }
    ) {

        // 🎥 Thumbnail Card
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {

                // ✅ Thumbnail Image
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = video.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // ✅ Soft Dark Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(alpha = 0.25f)
                        )
                )

                // ✅ Proper Play Button (Modern White)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Play Trailer",
                        tint = Color.Black,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 🎬 Video Title BELOW card
        Text(
            text = video.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        // ✅ Video Type Subtitle
        Text(
            text = video.type,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.55f)
        )
    }
}


// ─────────────────────────────────────────────
// Helper: Redirect to YouTube Safely
// ─────────────────────────────────────────────

private fun openYouTube(context: Context, key: String) {
    val url = "https://www.youtube.com/watch?v=$key"
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "YouTube app not found", Toast.LENGTH_SHORT).show()
    }
}
