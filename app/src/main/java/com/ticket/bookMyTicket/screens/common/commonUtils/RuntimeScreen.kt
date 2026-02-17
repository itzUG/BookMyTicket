package com.ticket.bookMyTicket.screens.common.commonUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowUI() {
    RuntimeScreen(23)
}

@Composable
fun RuntimeScreen(time: Int) {

    val hrs = time/60
    val min = time%60

    val formatedTime = if (hrs > 0) {
        "${hrs}h ${min}m"
    } else {
        "${min}m"
    }

    Row(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = "Runtime",
            tint = Color.Black,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = "$time min",
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
