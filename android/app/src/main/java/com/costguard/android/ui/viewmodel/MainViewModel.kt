package com.costguard.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costguard.android.data.api.ApiClient
import com.costguard.android.data.model.PolicyAlert
import com.costguard.android.data.model.TelemetryMetrics
import com.costguard.android.data.model.TraceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val metrics: TelemetryMetrics = TelemetryMetrics(),
    val traces: List<TraceItem> = emptyList(),
    val alerts: List<PolicyAlert> = emptyList(),
    val selectedTrace: TraceItem? = null,
    val serverStatus: String = "Connecting...",
    val errorMessage: String? = null
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
        seedSampleAlerts()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val api = ApiClient.getService()
                val health = try { api.getHealth().status } catch (e: Exception) { "offline" }
                val metrics = try { api.getTelemetryMetrics() } catch (e: Exception) { TelemetryMetrics() }
                val traces = try { api.getTraces(limit = 20) } catch (e: Exception) { emptyList() }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    serverStatus = health,
                    metrics = metrics,
                    traces = traces
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    serverStatus = "offline",
                    errorMessage = e.localizedMessage ?: "Failed to connect to CostGuard backend"
                )
            }
        }
    }

    fun selectTrace(traceId: String) {
        val found = _uiState.value.traces.find { it.traceId == traceId }
        _uiState.value = _uiState.value.copy(selectedTrace = found)
    }

    private fun seedSampleAlerts() {
        val alerts = listOf(
            PolicyAlert(
                title = "Policy Regression Gate Blocked",
                prNumber = "PR #42",
                severity = "CRITICAL",
                message = "Candidate prompt inflation increased effective cost per task by 134.3%, breaching policy limit (<=25%).",
                deltaCostPercent = 134.3,
                timestamp = "10 mins ago"
            ),
            PolicyAlert(
                title = "Streaming TTFT Warning",
                prNumber = "PR #39",
                severity = "WARNING",
                message = "Time to first token increased by 35ms in RAG retrieval step.",
                deltaCostPercent = 12.0,
                timestamp = "1 hour ago"
            )
        )
        _uiState.value = _uiState.value.copy(alerts = alerts)
    }

    fun updateServerUrl(url: String) {
        ApiClient.updateBaseUrl(url)
        loadData()
    }
}
