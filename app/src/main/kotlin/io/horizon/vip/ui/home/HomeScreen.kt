package io.horizon.vip.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizon.vip.ui.theme.HorizonColors

@Composable
fun HomeScreen(onStartScan: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HorizonColors.bgVoid)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "HORIZON VIP",
            color = HorizonColors.neonMint,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "on-device recon // kotlin engine",
            color = HorizonColors.textMid,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
        Spacer(Modifier.height(32.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, HorizonColors.neonMint.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text("MODULES", color = HorizonColors.textLo, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
            Spacer(Modifier.height(8.dp))
            listOf("DNS resolve", "TCP port scan", "HTTP probe", "TLS/SNI cert").forEach {
                Text("  > $it", color = HorizonColors.textHi, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onStartScan,
            colors = ButtonDefaults.buttonColors(
                containerColor = HorizonColors.neonMint,
                contentColor = HorizonColors.bgVoid
            ),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("OPEN SCANNER", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }
}
