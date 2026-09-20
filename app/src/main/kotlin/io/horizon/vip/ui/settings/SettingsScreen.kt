package io.horizon.vip.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizon.vip.ui.theme.HorizonColors

@Composable
fun SettingsScreen() {
    Column(
        Modifier.fillMaxSize().background(HorizonColors.bgVoid).padding(16.dp)
    ) {
        Text("SETTINGS", color = HorizonColors.neonMint, fontFamily = FontFamily.Monospace, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))
        Text("engine: kotlin / okhttp / coroutines", color = HorizonColors.textHi, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Text("python: offline (chaquopy optional)", color = HorizonColors.textMid, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Text("native cpp: optional hot path", color = HorizonColors.textMid, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Spacer(Modifier.height(12.dp))
        Text("no telemetry · no account · on-device only", color = HorizonColors.neonMint, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
    }
}
