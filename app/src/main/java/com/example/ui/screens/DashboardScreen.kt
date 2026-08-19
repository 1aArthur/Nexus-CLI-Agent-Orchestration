package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.TelemetrySnapshot
import com.example.ui.MainViewModel
import com.example.ui.components.CosmicRadarScanner
import com.example.ui.components.CosmicResourceGauge
import com.example.ui.components.CosmicStarfieldBackground
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicBlack
import com.example.ui.theme.CosmicBlue
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicBorderBright
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCardElevated
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicDark
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicMuted
import com.example.ui.theme.CosmicNeonCyan
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.CosmicTextSecondary
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.CosmicWhite

data class RealtimeActivityItem(
    val id: String,
    val agentName: String,
    val description: String,
    val timestamp: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateToWorkflows: () -> Unit,
    onNavigateToAgents: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToTerminal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val workflows by viewModel.workflows.collectAsState()

    val activities = listOf(
        RealtimeActivityItem(
            id = "act-1",
            agentName = "Security Agent",
            description = "Escaneamento MASVS v2.0 concluído",
            timestamp = "09:41:22",
            icon = Icons.Default.Security,
            accentColor = CosmicEmerald
        ),
        RealtimeActivityItem(
            id = "act-2",
            agentName = "Codex Agent",
            description = "Gerou patch SIMD para 3 arquivos C++20",
            timestamp = "09:41:18",
            icon = Icons.Default.Code,
            accentColor = CosmicPurple
        ),
        RealtimeActivityItem(
            id = "act-3",
            agentName = "Claude Agent",
            description = "Analisando arquitetura de autenticação",
            timestamp = "09:41:16",
            icon = Icons.Default.AutoAwesome,
            accentColor = CosmicCyan
        ),
        RealtimeActivityItem(
            id = "act-4",
            agentName = "Exa Research",
            description = "Pesquisa concluída: 24 fontes indexadas",
            timestamp = "09:41:12",
            icon = Icons.Default.Search,
            accentColor = CosmicAmber
        ),
        RealtimeActivityItem(
            id = "act-5",
            agentName = "Build System",
            description = "Compilação NDK concluída com sucesso",
            timestamp = "09:41:08",
            icon = Icons.Default.Build,
            accentColor = CosmicBlue
        )
    )

    CosmicStarfieldBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top App Bar
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CosmicCard)
                            .border(1.dp, CosmicBorder, CircleShape)
                            .testTag("dashboard_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Lateral",
                            tint = CosmicWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = "Dashboard",
                        color = CosmicWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = CosmicCyan,
                                modifier = Modifier.size(6.dp)
                            )
                        }
                    ) {
                        IconButton(
                            onClick = { viewModel.speakText("Nexus Dev Orchestrator operacional. Todos os agentes ativos.") },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CosmicCard)
                                .border(1.dp, CosmicBorder, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificações",
                                tint = CosmicWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // System Status Card with Radar Scanner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("system_status_card"),
                    colors = CardDefaults.cardColors(containerColor = CosmicCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderBright.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "STATUS DO SISTEMA",
                                color = CosmicMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Operacional",
                                color = CosmicWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Todos os sistemas funcionando normalmente",
                                color = CosmicTextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }

                        CosmicRadarScanner(
                            size = 58.dp,
                            radarColor = CosmicCyan
                        )
                    }
                }
            }

            // 4 Top Metrics Grid (AGENTES, WORKFLOWS, CPU, MEMÓRIA)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricMiniCard(
                        title = "AGENTES",
                        value = "12",
                        subLabel = "Ativos",
                        color = CosmicWhite,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToAgents
                    )
                    MetricMiniCard(
                        title = "WORKFLOWS",
                        value = "${workflows.size.coerceAtLeast(8)}",
                        subLabel = "Executando",
                        color = CosmicCyan,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToWorkflows
                    )
                    MetricMiniCard(
                        title = "CPU",
                        value = "${telemetry.cpuPercent.toInt()}%",
                        subLabel = "Uso atual",
                        color = CosmicEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        title = "MEMÓRIA",
                        value = "${telemetry.memoryUsageMb.toInt()}MB",
                        subLabel = "Utilizado",
                        color = CosmicViolet,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Atividade em Tempo Real Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ATIVIDADE EM TEMPO REAL",
                        color = CosmicMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Ver todos",
                        color = CosmicCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToAgents() }
                    )
                }
            }

            // Real-Time Activity Items
            items(activities, key = { it.id }) { item ->
                RealtimeActivityRow(activity = item)
            }

            // Uso de Recursos (Resource Usage Gauges)
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CosmicCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "USO DE RECURSOS",
                                color = CosmicMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Ver detalhes",
                                color = CosmicCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onNavigateToTerminal() }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CosmicResourceGauge(
                                title = "CPU",
                                percent = telemetry.cpuPercent.toInt().coerceIn(15, 95),
                                activeColor = CosmicCyan
                            )
                            CosmicResourceGauge(
                                title = "RAM",
                                percent = 61,
                                activeColor = CosmicPurple
                            )
                            CosmicResourceGauge(
                                title = "DISCO",
                                percent = 47,
                                activeColor = CosmicEmerald
                            )
                        }
                    }
                }
            }

            // Últimos Workflows Card
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ÚLTIMOS WORKFLOWS",
                        color = CosmicMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Ver todos",
                        color = CosmicCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToWorkflows() }
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToWorkflows() },
                    colors = CardDefaults.cardColors(containerColor = CosmicCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CosmicEmerald.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RocketLaunch,
                                    contentDescription = "Workflow",
                                    tint = CosmicEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Full Pipeline",
                                    color = CosmicWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Sucesso",
                                        tint = CosmicEmerald,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "Sucesso",
                                        color = CosmicEmerald,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        Text(
                            text = "09:40:55",
                            color = CosmicMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun MetricMiniCard(
    title: String,
    value: String,
    subLabel: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CosmicCard),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = CosmicMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = color,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = subLabel,
                color = CosmicTextSecondary,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
fun RealtimeActivityRow(activity: RealtimeActivityItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CosmicCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(activity.accentColor.copy(alpha = 0.12f))
                        .border(1.dp, activity.accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = activity.icon,
                        contentDescription = activity.agentName,
                        tint = activity.accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column {
                    Text(
                        text = activity.agentName,
                        color = CosmicWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activity.description,
                        color = CosmicTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = activity.timestamp,
                    color = CosmicMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(activity.accentColor)
                )
            }
        }
    }
}
