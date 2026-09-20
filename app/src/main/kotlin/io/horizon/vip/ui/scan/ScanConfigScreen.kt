package io.horizon.vip.ui.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.horizon.vip.ui.theme.HorizonColors

@Composable
fun ScanConfigScreen(vm: ScanViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val modes = listOf("full", "dns", "ports", "http", "tls")

    Column(
        Modifier
            .fillMaxSize()
            .background(HorizonColors.bgVoid)
            .padding(16.dp)
    ) {
        Text("SCAN", color = HorizonColors.neonMint, fontFamily = FontFamily.Monospace, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.target,
            onValueChange = vm::setTarget,
            label = { Text("target host / domain") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HorizonColors.neonMint,
                unfocusedBorderColor = HorizonColors.textLo,
                focusedTextColor = HorizonColors.textHi,
                unfocusedTextColor = HorizonColors.textHi,
                cursorColor = HorizonColors.neonMint,
                focusedLabelColor = HorizonColors.neonMint,
                unfocusedLabelColor = HorizonColors.textMid
            )
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            modes.forEach { m ->
                FilterChip(
                    selected = state.mode == m,
                    onClick = { vm.setMode(m) },
                    label = { Text(m.uppercase(), fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HorizonColors.neonMint.copy(alpha = 0.2f),
                        selectedLabelColor = HorizonColors.neonMint,
                        containerColor = HorizonColors.bgSurface,
                        labelColor = HorizonColors.textMid
                    )
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { vm.start() },
                enabled = !state.running && state.target.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = HorizonColors.neonMint, contentColor = HorizonColors.bgVoid)
            ) { Text(if (state.running) "RUNNING" else "START", fontFamily = FontFamily.Monospace) }
            Button(
                onClick = { vm.stop() },
                enabled = state.running,
                colors = ButtonDefaults.buttonColors(containerColor = HorizonColors.errRed)
            ) { Text("STOP", fontFamily = FontFamily.Monospace) }
        }
        Spacer(Modifier.height(12.dp))
        Text("CONSOLE", color = HorizonColors.textLo, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
        Spacer(Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, HorizonColors.bgElevated, RoundedCornerShape(6.dp))
                .background(HorizonColors.bgBase)
                .padding(8.dp)
        ) {
            items(state.logs) { line ->
                Text(line, color = HorizonColors.neonMint, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
            }
        }
        state.result?.let { r ->
            Spacer(Modifier.height(8.dp))
            Text(
                "findings=${r.findings.size}  ${r.finishedAt - r.startedAt}ms",
                color = HorizonColors.textHi,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        }
        state.error?.let {
            Text(it, color = HorizonColors.errRed, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        }
    }
}
