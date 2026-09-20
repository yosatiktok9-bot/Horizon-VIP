package io.horizon.vip.ui.theme

import androidx.compose.ui.graphics.Color

object HorizonColors {
    val bgVoid = Color(0xFF05070A)
    val bgBase = Color(0xFF0A0E14)
    val bgSurface = Color(0xFF0F1419)
    val bgElevated = Color(0xFF141B23)
    val bgTint = Color(0xFF1A2430)

    val textHi = Color(0xFFF8FAFC)
    val textMid = Color(0xFF94A3B8)
    val textLo = Color(0xFF475569)
    val textDis = Color(0xFF1E293B)

    val neonMint = Color(0xFF00FFC6)
    val cyberCyan = Color(0xFF22D3EE)
    val plasmaViolet = Color(0xFFA78BFA)
    val amberAlert = Color(0xFFF59E0B)
    val roseAlert = Color(0xFFFB7185)
    val matrixGreen = Color(0xFF34D399)

    val okGreen = Color(0xFF10B981)
    val warnAmber = Color(0xFFF59E0B)
    val errRed = Color(0xFFEF4444)
    val infoBlue = Color(0xFF3B82F6)
    val purpleFlag = Color(0xFFA855F7)

    fun accent(name: String): Color = when (name) {
        "cyber_cyan" -> cyberCyan
        "plasma_violet" -> plasmaViolet
        "amber_alert" -> amberAlert
        "rose_alert" -> roseAlert
        "matrix_green" -> matrixGreen
        else -> neonMint
    }
}
