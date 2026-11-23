package com.costguard.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costguard.android.data.model.PolicyAlert
import com.costguard.android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyAlertsScreen(
    alerts: List<PolicyAlert>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CI/CD Policy Alerts", fontWeight = FontWeight.Bold) },
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
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(alerts) { alert ->
                AlertCard(alert = alert)
            }
        }
    }
}

@Composable
fun AlertCard(alert: PolicyAlert) {
    val isCritical = alert.severity == "CRITICAL"
    val badgeBg = if (isCritical) Color(0xFFFFEBEE) else Color(0xFFFFF8E1)
    val badgeColor = if (isCritical) AccentRose else Color(0xFFF57F17)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = if (isCritical) Icons.Default.Block else Icons.Default.Warning,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(alert.prNumber, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkForest)
                }
                Box(
                    modifier = Modifier
                        .background(badgeBg, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(alert.severity, color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(alert.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(6.dp))
            Text(alert.message, fontSize = 13.sp, color = Color(0xFF475569), lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Delta Cost: +${alert.deltaCostPercent}%",
                    fontWeight = FontWeight.Bold,
                    color = AccentRose,
                    fontSize = 12.sp
                )
                Text(alert.timestamp, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}
