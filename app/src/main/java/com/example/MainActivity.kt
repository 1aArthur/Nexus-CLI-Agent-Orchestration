package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.MainViewModel
import com.example.ui.components.CosmicAutopilotSheet
import com.example.ui.components.CosmicNavigationDrawerContent
import com.example.ui.components.CosmicOrbNavButton
import com.example.ui.screens.AgentMonitorScreen
import com.example.ui.screens.CicdNativeScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExaSearchScreen
import com.example.ui.screens.OrchestratorScreen
import com.example.ui.screens.SecurityCenterScreen
import com.example.ui.screens.SecurityAuditScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SkillsSettingsScreen
import com.example.ui.screens.TerminalScreen
import com.example.ui.screens.ToolsHubScreen
import com.example.ui.screens.WorkflowsScreen
import com.example.ui.theme.CosmicBlack
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicMuted
import com.example.ui.theme.CosmicTextSecondary
import com.example.ui.theme.CosmicWhite
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

data class BottomBarTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AgenticAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgenticAppContent(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var showAutopilotSheet by remember { mutableStateOf(false) }

    val bottomTabs = listOf(
        BottomBarTab("dashboard", "Dashboard", Icons.Filled.Home, Icons.Outlined.Home),
        BottomBarTab("agents_monitor", "Agentes", Icons.Filled.Groups, Icons.Outlined.Groups),
        // Central Cosmic Orb placed here
        BottomBarTab("terminal", "Terminal", Icons.Filled.Terminal, Icons.Outlined.Terminal),
        BottomBarTab("settings", "Configurações", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CosmicNavigationDrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    coroutineScope.launch {
                        drawerState.close()
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = CosmicBlack,
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CosmicBlack)
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Cosmic Bottom Navigation Bar Shell
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(CosmicCard)
                            .border(1.dp, CosmicBorder)
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left 2 tabs
                        bottomTabs.take(2).forEach { tab ->
                            CosmicBottomBarItem(
                                tab = tab,
                                isSelected = currentRoute == tab.route,
                                onClick = {
                                    if (currentRoute != tab.route) {
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }

                        // Central Spacer for Cosmic Orb
                        Box(modifier = Modifier.size(54.dp))

                        // Right 2 tabs
                        bottomTabs.drop(2).forEach { tab ->
                            CosmicBottomBarItem(
                                tab = tab,
                                isSelected = currentRoute == tab.route,
                                onClick = {
                                    if (currentRoute != tab.route) {
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // Floating Cosmic Glowing Orb Button
                    CosmicOrbNavButton(
                        onClick = { showAutopilotSheet = true }
                    )
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
                        viewModel = viewModel,
                        onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                        onNavigateToWorkflows = { navController.navigate("workflows") },
                        onNavigateToAgents = { navController.navigate("agents_monitor") },
                        onNavigateToSecurity = { navController.navigate("security_center") },
                        onNavigateToTerminal = { navController.navigate("terminal") }
                    )
                }
                composable("agents_monitor") {
                    AgentMonitorScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("workflows") {
                    WorkflowsScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("security_center") {
                    SecurityCenterScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("terminal") {
                    TerminalScreen(viewModel = viewModel)
                }
                composable("settings") {
                    SettingsScreen(viewModel = viewModel)
                }
                composable("exa_search") {
                    ExaSearchScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("cicd_native") {
                    CicdNativeScreen(viewModel = viewModel)
                }
                composable("tools_hub") {
                    ToolsHubScreen(viewModel = viewModel)
                }
                composable("skills") {
                    SkillsSettingsScreen(viewModel = viewModel)
                }
                composable("orchestrator") {
                    OrchestratorScreen(viewModel = viewModel)
                }
                composable("devsecops") {
                    SecurityAuditScreen(viewModel = viewModel)
                }
            }
        }

        if (showAutopilotSheet) {
            CosmicAutopilotSheet(
                onDismiss = { showAutopilotSheet = false },
                onSelectAction = { actionId ->
                    when (actionId) {
                        "full_autopilot" -> {
                            viewModel.startOrchestration("Autopilot Swarm Completo")
                            navController.navigate("agents_monitor")
                        }
                        "security_scan" -> {
                            viewModel.runMasvsStaticAnalysis(
                                "CoreAuthService.kt",
                                viewModel.currentAuditedCode.value
                            )
                            navController.navigate("security_center")
                        }
                        "exa_research" -> navController.navigate("exa_search")
                        "terminal_exec" -> navController.navigate("terminal")
                    }
                }
            )
        }
    }
}

@Composable
fun CosmicBottomBarItem(
    tab: BottomBarTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("bottom_tab_${tab.route}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
            contentDescription = tab.title,
            tint = if (isSelected) CosmicWhite else CosmicMuted,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = tab.title,
            color = if (isSelected) CosmicWhite else CosmicMuted,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontFamily = FontFamily.Monospace
        )
    }
}
