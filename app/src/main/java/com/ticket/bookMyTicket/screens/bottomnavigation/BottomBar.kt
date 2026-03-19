package com.ticket.bookMyTicket.screens.bottomnavigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ticket.bookMyTicket.navigation.Routes

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgDeep      = Color(0xFF080810)
private val BgBar       = Color(0xFF0E0E1C)
private val AccentRed   = Color(0xFFE50914)
private val TextPrimary = Color(0xFFF0F0FF)
private val TextMuted   = Color(0xFF5A5A80)
private val GlassBorder = Color.White.copy(alpha = 0.08f)

data class NavItem(
    val label: String,
    val route: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
)

@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {

    val items = listOf(
        NavItem("Home",    Routes.HOME,    Icons.Filled.Home,   Icons.Outlined.Home),
        NavItem("Profile", Routes.PROFILE, Icons.Filled.Person, Icons.Outlined.Person),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // Glow behind the bar
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp)
                .align(Alignment.Center)
                .blur(28.dp)
                .background(AccentRed.copy(alpha = 0.18f), RoundedCornerShape(50))
        )

        // The floating pill bar
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(BgBar)
                .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
                .padding(horizontal = 12.dp)
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                NavPillItem(
                    item = item,
                    isSelected = isSelected,
                    modifier = Modifier.weight(1f)
                ) {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.HOME)
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavPillItem(
    item: NavItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Animate icon scale on selection
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "iconScale"
    )

    // Animate label alpha
    val labelAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(200),
        label = "labelAlpha"
    )

    // Animate icon color
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else TextMuted,
        animationSpec = tween(200),
        label = "iconColor"
    )

    // Pill background width expands when selected
    val pillWidthFraction by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium),
        label = "pillWidth"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        // Active pill background
        if (pillWidthFraction > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(44.dp)
                    .scale(pillWidthFraction)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                AccentRed.copy(alpha = 0.85f * pillWidthFraction),
                                Color(0xFFFF3333).copy(alpha = 0.75f * pillWidthFraction)
                            )
                        )
                    )
            )
        }

        // Icon + label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) item.iconFilled else item.iconOutlined,
                contentDescription = item.label,
                tint = iconColor,
                modifier = Modifier
                    .size(22.dp)
                    .scale(iconScale)
            )

            // Label slides in next to icon when selected
            if (labelAlpha > 0f) {
                Spacer(Modifier.width(6.dp))
                Text(
                    text = item.label,
                    color = Color.White.copy(alpha = labelAlpha),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp,
                    maxLines = 1
                )
            }
        }
    }
}