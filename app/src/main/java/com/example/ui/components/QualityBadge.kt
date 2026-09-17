package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Badge4K
import com.example.ui.theme.BadgeHDR
import com.example.ui.theme.CinemaGold

@Composable
fun QualityBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = when {
        text.contains("4K", ignoreCase = true) -> Triple(
            Badge4K.copy(alpha = 0.2f),
            Badge4K,
            Badge4K.copy(alpha = 0.5f)
        )
        text.contains("HDR", ignoreCase = true) -> Triple(
            BadgeHDR.copy(alpha = 0.2f),
            BadgeHDR,
            BadgeHDR.copy(alpha = 0.5f)
        )
        else -> Triple(
            CinemaGold.copy(alpha = 0.15f),
            CinemaGold,
            CinemaGold.copy(alpha = 0.4f)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
