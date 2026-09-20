package io.horizon.vip.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.horizon.vip.data.db.ScanDao
import io.horizon.vip.data.db.ScanEntity
import io.horizon.vip.engine.ScanEngine
import io.horizon.vip.engine.ScanResult
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScanUiState(
    val mode: String = "full",
    val target: String = "",
    val running: Boolean = false,
    val progress: Float = 0f,
    val logs: List<String> = emptyList(),
    val result: ScanResult? = null,
    val error: String? = null
)

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val engine: ScanEngine,
    private val scanDao: ScanDao
) : ViewModel() {

    private val _state = MutableStateFlow(ScanUiState())
    val state: StateFlow<ScanUiState> = _state.asStateFlow()

    private var job: Job? = null
    private var logJob: Job? = null

    init {
        logJob = viewModelScope.launch {
            engine.log.collect { line ->
                _state.update { it.copy(logs = (it.logs + line).takeLast(200)) }
            }
        }
    }

    fun setMode(mode: String) {
        _state.update { it.copy(mode = mode) }
    }

    fun setTarget(target: String) {
        _state.update { it.copy(target = target) }
    }

    fun start() {
        val target = _state.value.target.trim()
        if (target.isEmpty() || _state.value.running) return
        job?.cancel()
        job = viewModelScope.launch {
            _state.update {
                it.copy(running = true, progress = 0.1f, logs = emptyList(), result = null, error = null)
            }
            try {
                val result = engine.runScan(_state.value.mode, target)
                scanDao.insert(
                    ScanEntity(
                        mode = result.mode,
                        target = result.target,
                        profile = "default",
                        startedAt = result.startedAt,
                        finishedAt = result.finishedAt,
                        findingsCount = result.findings.size,
                        resultJson = engine.toJson(result)
                    )
                )
                _state.update {
                    it.copy(running = false, progress = 1f, result = result)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(running = false, error = e.message ?: "scan failed")
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
        _state.update { it.copy(running = false) }
    }
}
