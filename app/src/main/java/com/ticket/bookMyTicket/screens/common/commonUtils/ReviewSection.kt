package com.ticket.bookMyTicket.screens.common.commonUtils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ticket.bookMyTicket.data.remote.tmdb.dto.ReviewDto

// ─────────────────────────────────────────────
// ✅ Modern Horizontal Review Section
// ─────────────────────────────────────────────

@Composable
fun ReviewSection(reviews: List<ReviewDto>) {

    // If no reviews, don't show anything
    if (reviews.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
    ) {

        // ⭐ Section Heading
        Text(
            text = "User Reviews ⭐",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            modifier = Modifier.padding(start = 18.dp, bottom = 14.dp)
        )

        // ✅ Horizontal Scroll List
        LazyRow(
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            items(
                reviews.take(6), // show max 6 reviews
                key = { it.id }
            ) { review ->
                ReviewCardHorizontal(review)
            }
        }
    }
}

// ─────────────────────────────────────────────
// ✅ Review Card (Horizontal Netflix Style)
// ─────────────────────────────────────────────

@Composable
fun ReviewCardHorizontal(review: ReviewDto) {

    val rating = review.author_details.rating

    Card(
        modifier = Modifier
            .width(290.dp)
            .heightIn(min = 170.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.06f)
        ),
        border = BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {

            // ── Top Row: Avatar + Name + Rating ───────────────
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ✅ Avatar Circle
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(
                            Color.DarkGray.copy(alpha = 0.35f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.author.firstOrNull()?.uppercase() ?: "U",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // ✅ Author Name
                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = review.author,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Verified Review",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.45f)
                    )
                }

                // ✅ Rating Badge
                if (rating != null) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFFFC107).copy(alpha = 0.18f)
                    ) {
                        Text(
                            text = "⭐ $rating",
                            color = Color(0xFFFFC107),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Review Content ───────────────────────────────
            Text(
                text = review.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.75f),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
