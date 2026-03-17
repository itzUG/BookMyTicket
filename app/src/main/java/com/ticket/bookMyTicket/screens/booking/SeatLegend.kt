package com.ticket.bookMyTicket.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeatLegend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        LegendItem(Color(0xFF00E676), "Selected")
        LegendSofaItem(Color(0xFF8E24AA), "Recliner")
        LegendItem(Color(0xFF2980B9), "Premium")
        LegendItem(Color(0xFF4A5568), "Regular")
        LegendItem(Color(0xFF2A2A2A), "Booked")
    }
}

/** Mini sofa icon in the legend to match the actual recliner seat shape */
@Composable
fun LegendSofaItem(color: Color, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            // Left armrest
            Box(
                modifier = Modifier.width(3.dp).height(10.dp)
                    .clip(RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp))
                    .background(color.copy(alpha = 0.7f))
            )
            Column(modifier = Modifier.width(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                // Headrest
                Box(modifier = Modifier.fillMaxWidth().height(3.dp)
                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    .background(color.copy(alpha = 0.5f)))
                // Seatback
                Box(modifier = Modifier.fillMaxWidth().height(5.dp).background(color))
                // Base
                Box(modifier = Modifier.fillMaxWidth().height(2.dp)
                    .clip(RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
                    .background(color.copy(alpha = 0.6f)))
            }
            // Right armrest
            Box(
                modifier = Modifier.width(3.dp).height(10.dp)
                    .clip(RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp))
                    .background(color.copy(alpha = 0.7f))
            )
        }
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
    }
}