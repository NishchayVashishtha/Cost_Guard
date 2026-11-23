package com.costguard.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.costguard.android.ui.screens.*
import com.costguard.android.ui.theme.CostGuardTheme
import com.costguard.android.ui.theme.ForestGreen
import com.costguard.android.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CostGuardTheme {
                val navController = rememberNavController()
                val uiState by viewModel.uiState.collectAsState()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute in listOf("dashboard", "traces", "alerts", "settings")) {
                            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                                NavigationBarItem(
                                    selected = currentRoute == "dashboard",
                                    onClick = { navController.navigate("dashboard") },
                                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                    label = { Text("Dashboard") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = ForestGreen, indicatorColor = ForestGreen.copy(alpha = 0.15f))
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "traces",
                                    onClick = { navController.navigate("traces") },
                                    icon = { Icon(Icons.Default.Analytics, contentDescription = "Traces") },
                                    label = { Text("Traces") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = ForestGreen, indicatorColor = ForestGreen.copy(alpha = 0.15f))
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "alerts",
                                    onClick = { navController.navigate("alerts") },
                                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
                                    label = { Text("Alerts") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = ForestGreen, indicatorColor = ForestGreen.copy(alpha = 0.15f))
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "settings",
                                    onClick = { navController.navigate("settings") },
                                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                    label = { Text("Settings") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = ForestGreen, indicatorColor = ForestGreen.copy(alpha = 0.15f))
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("dashboard") {
                            DashboardScreen(
                                state = uiState,
                                onRefresh = { viewModel.loadData() },
                                onNavigateToTraces = { navController.navigate("traces") },
                                onNavigateToAlerts = { navController.navigate("alerts") }
                            )
                        }
                        composable("traces") {
                            TracesScreen(
                                traces = uiState.traces,
                                onTraceClick = { traceId ->
                                    viewModel.selectTrace(traceId)
                                    navController.navigate("trace_detail")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("trace_detail") {
                            TraceDetailScreen(
                                trace = uiState.selectedTrace,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("alerts") {
                            PolicyAlertsScreen(
                                alerts = uiState.alerts,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                onSaveUrl = { newUrl -> viewModel.updateServerUrl(newUrl) },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
