package com.costguard.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costguard.android.data.model.TraceItem
import com.costguard.android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TracesScreen(
    traces: List<TraceItem>,
    onTraceClick: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OpenTelemetry Traces (${traces.size})", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(traces) { trace ->
                TraceCard(trace = trace, onClick = { onTraceClick(trace.traceId) })
            }
        }
    }
}

@Composable
fun TraceCard(trace: TraceItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trace.traceId,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen,
                    fontSize = 13.sp
                )
                StatusBadge(status = trace.status)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(trace.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Latency: ${trace.latencyMs} ms", fontSize = 12.sp, color = Color.Gray)
                Text("${trace.spanCount} Spans", fontSize = 12.sp, color = AccentPurple)
                Text("${trace.totalTokens} Toks", fontSize = 12.sp, color = Color.Gray)
                Text("$${String.format("%.5f", trace.totalCostUsd)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val isOk = status.lowercase() == "ok"
    val bgColor = if (isOk) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (isOk) Color(0xFF2E7D32) else Color(0xFFC62828)

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
