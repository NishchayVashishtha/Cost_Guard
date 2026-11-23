package com.costguard.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costguard.android.ui.theme.*
import com.costguard.android.ui.viewmodel.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onRefresh: () -> Unit,
    onNavigateToTraces: () -> Unit,
    onNavigateToAlerts: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("CostGuard Mobile", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(
                            text = if (state.serverStatus == "ok") "Server: Connected (Port 8001)" else "Server: ${state.serverStatus}",
                            fontSize = 12.sp,
                            color = if (state.serverStatus == "ok") Color(0xFFC7E2D3) else Color(0xFFFFCDD2)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Telemetry")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Policy Regression Banner if alerts exist
            if (state.alerts.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEF)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToAlerts
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = AccentRose)
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CI/CD Policy Regression Alert", fontWeight = FontWeight.Bold, color = AccentRose, fontSize = 14.sp)
                            Text(state.alerts.first().message, fontSize = 12.sp, color = Color(0xFF4A1521), maxLines = 2)
                        }
                    }
                }
            }

            Text("Real-Time Telemetry & Economics", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = DarkForest)

            // KPI Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(
                    modifier = Modifier.weight(1f),
                    title = "P95 Latency",
                    value = "${state.metrics.p95LatencyMs}",
                    unit = "ms",
                    subtitle = "Mean: ${state.metrics.meanLatencyMs} ms",
                    accentColor = ForestGreen
                )
                KpiCard(
                    modifier = Modifier.weight(1f),
                    title = "Streaming TTFT",
                    value = "${state.metrics.avgTtftMs}",
                    unit = "ms",
                    subtitle = "First token arrival",
                    accentColor = AccentSky
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(
                    modifier = Modifier.weight(1f),
                    title = "Throughput",
                    value = "${state.metrics.tokensPerSecond}",
                    unit = "tok/s",
                    subtitle = "${state.metrics.totalTokens} total tokens",
                    accentColor = AccentPurple
                )
                KpiCard(
                    modifier = Modifier.weight(1f),
                    title = "Regression Rate",
                    value = "${state.metrics.errorRatePercent}",
                    unit = "%",
                    subtitle = "${state.metrics.totalTraces} traces tracked",
                    accentColor = if (state.metrics.errorRatePercent > 0) AccentRose else ForestGreen
                )
            }

            // Outcome Economics Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Unit Economics (Outcome Economics)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkForest)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "CostGuard gates regressions by measuring Effective Cost per Successful Outcome (ECSO = Cost / Success Rate).",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Spend", fontSize = 11.sp, color = Color.Gray)
                            Text("$${String.format("%.5f", state.metrics.totalCostUsd)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ForestGreen)
                        }
                        Column {
                            Text("Active Spans", fontSize = 11.sp, color = Color.Gray)
                            Text("${state.metrics.totalSpans} OTel Spans", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AccentPurple)
                        }
                        Column {
                            Text("Health", fontSize = 11.sp, color = Color.Gray)
                            Text(state.serverStatus.uppercase(), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (state.serverStatus == "ok") ForestGreen else AccentRose)
                        }
                    }
                }
            }

            // Open Traces Button
            Button(
                onClick = onNavigateToTraces,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("View Distributed Traces Waterfall (${state.traces.size})", fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun KpiCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    subtitle: String,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(accentColor, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = DarkForest)
                Spacer(modifier = Modifier.width(4.dp))
                Text(unit, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 2.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 11.sp, color = Color.Gray)
        }
    }
}
