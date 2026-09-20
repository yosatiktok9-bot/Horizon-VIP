package io.horizon.vip.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAccent = compositionLocalOf { HorizonColors.neonMint }

private val HorizonDarkScheme = darkColorScheme(
    primary = HorizonColors.neonMint,
    onPrimary = HorizonColors.bgVoid,
    background = HorizonColors.bgVoid,
    surface = HorizonColors.bgSurface,
    onBackground = HorizonColors.textHi,
    onSurface = HorizonColors.textHi,
    error = HorizonColors.errRed
)

@Composable
fun HorizonTheme(
    accent: Color = HorizonColors.neonMint,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAccent provides accent) {
        MaterialTheme(
            colorScheme = HorizonDarkScheme.copy(primary = accent),
            typography = HorizonTypography,
            content = content
        )
    }
}
