package com.costguard.android.ui.screens

import androidx.compose.foundation.background
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
import com.costguard.android.data.model.SpanItem
import com.costguard.android.data.model.TraceItem
import com.costguard.android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraceDetailScreen(
    trace: TraceItem?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trace Waterfall", fontWeight = FontWeight.Bold) },
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
        if (trace == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Trace details unavailable", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(BackgroundLight)
                    .padding(14.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(trace.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("ID: ${trace.traceId}", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = ForestGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Duration: ${trace.latencyMs} ms", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            StatusBadge(status = trace.status)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Span Waterfall (OpenTelemetry Hierarchy)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkForest)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(trace.spans) { span ->
                        SpanRow(span = span, totalTraceLatency = trace.latencyMs)
                    }
                }
            }
        }
    }
}

@Composable
fun SpanRow(span: SpanItem, totalTraceLatency: Double) {
    val isChild = span.parentSpanId != null
    val kindColor = when (span.kind.lowercase()) {
        "workflow" -> ForestGreen
        "retrieval" -> AccentSky
        "llm" -> AccentPurple
        "evaluation" -> Color(0xFFF59E0B)
        else -> Color(0xFF64748B)
    }

    val widthRatio = if (totalTraceLatency > 0) (span.latencyMs / totalTraceLatency).toFloat().coerceIn(0.08f, 1.0f) else 0.5f

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = if (isChild) 16.dp else 0.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .background(kindColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(span.kind.uppercase(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(span.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Text("${span.latencyMs} ms", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = kindColor)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Flame bar
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthRatio)
                    .height(6.dp)
                    .background(kindColor.copy(alpha = 0.85f), RoundedCornerShape(3.dp))
            )
        }
    }
}
