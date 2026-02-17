package com.ticket.bookMyTicket.screens.common.movieDetailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true , showSystemUi = true)
@Composable
fun dummyRatingBox() {
    MovieRatingBox(5.4)
}

@SuppressLint("DefaultLocale")
@Composable
fun MovieRatingBox(rating: Double) {

    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .height(30.dp)
            .background(Color(0xFF1C1C1C), shape)
            .border(1.dp, Color.Yellow, shape)
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(12.dp)
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = String.format("%.1f/10", rating),
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

