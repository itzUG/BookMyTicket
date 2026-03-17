import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ticket.bookMyTicket.response.model.Seat
import com.ticket.bookMyTicket.response.model.SeatStatus
import com.ticket.bookMyTicket.response.model.SeatType

@Composable
fun SeatItem(
    seat: Seat,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isBooked = seat.status == SeatStatus.BOOKED
    when (seat.type) {
        SeatType.RECLINER -> RecliverSofaSeat(seat, isSelected, isBooked, onClick)
        SeatType.PREMIUM  -> PremiumSeat(seat, isSelected, isBooked, onClick)
        SeatType.REGULAR  -> RegularSeat(seat, isSelected, isBooked, onClick)
    }
}

// ── RECLINER — Sofa/couch style ────────────────────────────────────────────────
@Composable
fun RecliverSofaSeat(
    seat: Seat,
    isSelected: Boolean,
    isBooked: Boolean,
    onClick: () -> Unit
) {
    val baseColor = when {
        isBooked   -> Color(0xFF2A2A2A)
        isSelected -> Color(0xFF1B5E20)
        else       -> Color(0xFF6A1B9A)
    }
    val cushionColor = when {
        isBooked   -> Color(0xFF333333)
        isSelected -> Color(0xFF2E7D32)
        else       -> Color(0xFF8E24AA)
    }
    val armrestColor = when {
        isBooked   -> Color(0xFF1E1E1E)
        isSelected -> Color(0xFF1B5E20)
        else       -> Color(0xFF4A148C)
    }
    val highlightColor = when {
        isBooked   -> Color(0xFF3A3A3A)
        isSelected -> Color(0xFF66BB6A)
        else       -> Color(0xFFBA68C8)
    }
    val glowColor = if (isSelected) Color(0xFF00E676) else Color(0xFF9B59B6)

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "recScale"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp, vertical = 3.dp)
            .scale(animatedScale)
            .then(
                if (isSelected) Modifier.shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = glowColor.copy(alpha = 0.5f),
                    spotColor = glowColor
                ) else Modifier
            )
            .clickable(enabled = !isBooked) { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            // Left Armrest
            Box(
                modifier = Modifier
                    .width(7.dp)
                    .height(34.dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp, topEnd = 2.dp, bottomEnd = 2.dp))
                    .background(Brush.verticalGradient(listOf(armrestColor.copy(0.9f), armrestColor)))
                    .border(0.5.dp, Color.White.copy(if (isBooked) 0.04f else 0.15f),
                        RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
            ) {
                Box(Modifier.fillMaxWidth().height(3.dp).background(Color.White.copy(0.18f)))
            }

            // Main Seat Body
            Column(
                modifier = Modifier.width(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Headrest bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(Brush.verticalGradient(listOf(highlightColor.copy(0.7f), baseColor)))
                )
                Spacer(Modifier.height(1.dp))
                // Seatback with cushion crease
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .background(Brush.verticalGradient(listOf(cushionColor, baseColor.copy(0.85f))))
                        .border(0.5.dp, Color.White.copy(if (isBooked) 0.03f else 0.12f), RoundedCornerShape(0.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.65f).height(2.dp)
                            .align(Alignment.TopCenter).offset(y = 3.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(if (isBooked) 0.04f else 0.22f))
                    )
                }
                Spacer(Modifier.height(1.dp))
                // Seat base
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                        .background(Brush.verticalGradient(listOf(baseColor, baseColor.copy(0.6f))))
                ) {
                    Box(Modifier.fillMaxWidth().height(2.dp).align(Alignment.BottomCenter)
                        .background(Color.Black.copy(0.4f)))
                }
            }

            // Right Armrest
            Box(
                modifier = Modifier
                    .width(7.dp)
                    .height(34.dp)
                    .clip(RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp, topEnd = 6.dp, bottomEnd = 6.dp))
                    .background(Brush.verticalGradient(listOf(armrestColor.copy(0.9f), armrestColor)))
                    .border(0.5.dp, Color.White.copy(if (isBooked) 0.04f else 0.15f),
                        RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
            ) {
                Box(Modifier.fillMaxWidth().height(3.dp).background(Color.White.copy(0.18f)))
            }
        }
    }
}

// ── PREMIUM SEAT ───────────────────────────────────────────────────────────────
@Composable
fun PremiumSeat(
    seat: Seat,
    isSelected: Boolean,
    isBooked: Boolean,
    onClick: () -> Unit
) {
    val targetColor = when {
        isBooked   -> Color(0xFF2A2A2A)
        isSelected -> Color(0xFF00E676)
        else       -> Color(0xFF2980B9)
    }
    val animatedColor by animateColorAsState(targetColor, spring(), label = "premColor")
    val animatedScale by animateFloatAsState(
        if (isSelected) 1.12f else 1f, spring(dampingRatio = 0.5f), label = "premScale"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 2.dp, vertical = 3.dp)
            .scale(animatedScale)
            .width(29.dp)
            .then(if (isSelected) Modifier.shadow(8.dp, RoundedCornerShape(7.dp), spotColor = Color(0xFF00E676)) else Modifier)
            .clickable(enabled = !isBooked) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Headrest bump
        Box(
            modifier = Modifier
                .fillMaxWidth(0.55f).height(5.dp)
                .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                .background(animatedColor.copy(0.8f))
        )
        // Seatback
        Box(
            modifier = Modifier
                .fillMaxWidth().height(22.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(Brush.verticalGradient(listOf(animatedColor.copy(0.85f), animatedColor)))
                .border(0.5.dp, Color.White.copy(if (isBooked) 0.03f else 0.15f),
                    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f).height(2.dp)
                    .align(Alignment.TopCenter).offset(y = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(if (isBooked) 0.04f else 0.2f))
            )
        }
        // Base
        Box(
            modifier = Modifier
                .fillMaxWidth().height(7.dp)
                .clip(RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp))
                .background(animatedColor.copy(0.7f))
        )
    }
}

// ── REGULAR SEAT ───────────────────────────────────────────────────────────────
@Composable
fun RegularSeat(
    seat: Seat,
    isSelected: Boolean,
    isBooked: Boolean,
    onClick: () -> Unit
) {
    val targetColor = when {
        isBooked   -> Color(0xFF2A2A2A)
        isSelected -> Color(0xFF00E676)
        else       -> Color(0xFF4A5568)
    }
    val animatedColor by animateColorAsState(targetColor, spring(), label = "regColor")
    val animatedScale by animateFloatAsState(
        if (isSelected) 1.15f else 1f, spring(dampingRatio = 0.5f), label = "regScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(2.dp)
            .scale(animatedScale)
            .width(26.dp).height(26.dp)
            .then(if (isSelected) Modifier.shadow(8.dp, RoundedCornerShape(6.dp), spotColor = Color(0xFF00E676)) else Modifier)
            .clip(RoundedCornerShape(6.dp))
            .background(Brush.verticalGradient(listOf(animatedColor.copy(0.85f), animatedColor)))
            .border(
                if (isSelected) 1.5.dp else 0.5.dp,
                if (isSelected) Color.White.copy(0.7f) else Color.White.copy(0.08f),
                RoundedCornerShape(6.dp)
            )
            .clickable(enabled = !isBooked) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.65f).height(2.dp)
                .align(Alignment.TopCenter).offset(y = 4.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(if (isBooked) 0.04f else 0.22f))
        )
    }
}