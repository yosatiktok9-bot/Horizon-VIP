package io.horizon.vip.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object HorizonMotion {
    val micro = tween<Float>(120)
    val hover = tween<Float>(180)
    val content = tween<Float>(280)
    val screen = tween<Float>(380)
    val hero = spring<Float>(dampingRatio = 0.75f, stiffness = Spring.StiffnessMedium)
}
