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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ticket.bookMyTicket.data.remote.tmdb.dto.CastDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.CrewDto

// ─────────────────────────────────────────────
// ✅ Modern Section Heading
// ─────────────────────────────────────────────

@Composable
fun SectionHeading(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = Color.White,
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}

// ─────────────────────────────────────────────
// ✅ Modern Cast Section
// ─────────────────────────────────────────────

@Composable
fun CastCrew(casts: List<CastDto>) {

    if (casts.isEmpty()) return

    Column {

        SectionHeading("Top Cast")

        LazyRow(
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(casts.take(15), key = { it.id }) { cast ->
                PersonCard(
                    name = cast.name,
                    role = cast.character,
                    imagePath = cast.profile_path
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// ✅ Modern Crew Section
// ─────────────────────────────────────────────

@Composable
fun CrewSection(crew: List<CrewDto>) {

    if (crew.isEmpty()) return

    Column {

        SectionHeading("Crew")

        LazyRow(
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(crew.take(15), key = { it.id }) { member ->
                PersonCard(
                    name = member.name,
                    role = member.job,
                    imagePath = member.profile_path
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// ✅ Reusable Modern Person Card
// ─────────────────────────────────────────────

@Composable
fun PersonCard(
    name: String,
    role: String,
    imagePath: String?
) {

    val imagePrefix = "https://image.tmdb.org/t/p/w500"

    Column(
        modifier = Modifier.width(92.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ Profile Image with Border + Shadow Look
        Surface(
            shape = CircleShape,
            border = BorderStroke(
                1.dp,
                Color.White.copy(alpha = 0.15f)
            ),
            color = Color.DarkGray.copy(alpha = 0.35f),
            modifier = Modifier.size(78.dp)
        ) {
            AsyncImage(
                model = if (imagePath != null)
                    "$imagePrefix$imagePath"
                else null,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Name
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        // ✅ Role (Character / Job)
        Text(
            text = role,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.55f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
