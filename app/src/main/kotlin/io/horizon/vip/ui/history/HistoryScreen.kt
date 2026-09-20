package io.horizon.vip.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.horizon.vip.data.db.ScanDao
import io.horizon.vip.data.db.ScanEntity
import io.horizon.vip.ui.theme.HorizonColors
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class HistoryViewModel @Inject constructor(scanDao: ScanDao) : ViewModel() {
    val scans: StateFlow<List<ScanEntity>> = scanDao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@Composable
fun HistoryScreen(vm: HistoryViewModel = hiltViewModel()) {
    val scans by vm.scans.collectAsState()
    val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    Column(
        Modifier.fillMaxSize().background(HorizonColors.bgVoid).padding(16.dp)
    ) {
        Text("HISTORY", color = HorizonColors.neonMint, fontFamily = FontFamily.Monospace, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        if (scans.isEmpty()) {
            Text("no scans yet", color = HorizonColors.textMid, fontFamily = FontFamily.Monospace)
        } else {
            LazyColumn {
                items(scans) { s ->
                    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text("${s.mode.uppercase()}  ${s.target}", color = HorizonColors.textHi, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                        Text(
                            "${fmt.format(Date(s.finishedAt))}  findings=${s.findingsCount}",
                            color = HorizonColors.textMid,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
