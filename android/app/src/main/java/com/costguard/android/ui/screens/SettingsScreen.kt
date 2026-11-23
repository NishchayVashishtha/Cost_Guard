package com.costguard.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costguard.android.data.api.ApiClient
import com.costguard.android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSaveUrl: (String) -> Unit,
    onBack: () -> Unit
) {
    var urlText by remember { mutableStateOf(ApiClient.getBaseUrl()) }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CostGuard Settings", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Backend Server Endpoint", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkForest)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Configure the CostGuard REST API URL. For Android Emulator use 10.0.2.2:8001; for physical devices use your LAN IP (e.g. 192.168.1.X:8001) or OpenShift Route URL.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = urlText,
                        onValueChange = { urlText = it },
                        label = { Text("Server Base URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            onSaveUrl(urlText)
                            saveMessage = "Endpoint updated and telemetry reconnected!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save & Reconnect")
                    }

                    if (saveMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(saveMessage!!, fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About CostGuard Mobile", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkForest)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Version: 1.0.0 (AI DevOps Edition)", fontSize = 13.sp, color = Color.Gray)
                    Text("Author: Nishchay Vashishtha", fontSize = 13.sp, color = Color.Gray)
                    Text("Course: AI DevOps (CSE 4011)", fontSize = 13.sp, color = Color.Gray)
                    Text("Standards: OpenTelemetry 1.25.0, W3C TraceContext", fontSize = 13.sp, color = Color.Gray)
                }
            }
        }
    }
}
