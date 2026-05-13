package com.gokulahealth.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AgriGradients {
    val PrimaryGradient = Brush.verticalGradient(
        colors = listOf(ForestGreen, Color(0xFF4A8B6A))
    )
    
    val SecondaryGradient = Brush.horizontalGradient(
        colors = listOf(EarthBrown, Color(0xFFA67C52))
    )
    
    val AccentGradient = Brush.linearGradient(
        colors = listOf(HayAccent, Color(0xFFF2C94C))
    )
    
    val SurfaceGradient = Brush.verticalGradient(
        colors = listOf(MilkCream, IvoryBack)
    )

    val DarkSurfaceGradient = Brush.verticalGradient(
        colors = listOf(MossCard, SoilBlack)
    )
}
