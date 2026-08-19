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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import com.example.ui.MainViewModel
import com.example.ui.components.CosmicStarfieldBackground
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicBlack
import com.example.ui.theme.CosmicBlue
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicBorderBright
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicMuted
import com.example.ui.theme.CosmicNeonCyan
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.CosmicTextSecondary
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.CosmicWhite

data class MonitoredAgent(
    val id: String,
    val name: String,
    val role: String,
    val status: String,
    val progress: Int,
    val icon: ImageVector,
    val accentColor: Color,
    val isRunning: Boolean
)

@Composable
fun AgentMonitorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Todos", "Executando", "Inativos")

    val claudeStatus by viewModel.claudeStatus.collectAsState()
    val codexStatus by viewModel.codexStatus.collectAsState()

    val agents = listOf(
        MonitoredAgent(
            id = "agent-planner",
            name = "Planner Agent",
            role = "Planejamento de tarefas",
            status = "Executando",
            progress = 32,
            icon = Icons.Default.Psychology,
            accentColor = CosmicCyan,
            isRunning = true
        ),
        MonitoredAgent(
            id = "agent-research",
            name = "Research Agent",
            role = "Pesquisa e análise",
            status = "Executando",
            progress = 68,
            icon = Icons.Default.Search,
            accentColor = CosmicEmerald,
            isRunning = true
        ),
        MonitoredAgent(
            id = "agent-claude",
            name = "Claude Agent",
            role = "Análise e implementação",
            status = if (claudeStatus.state.name == "IDLE") "Executando" else claudeStatus.state.name,
            progress = 55,
            icon = Icons.Default.AutoAwesome,
            accentColor = CosmicViolet,
            isRunning = true
        ),
        MonitoredAgent(
            id = "agent-codex",
            name = "Codex Agent",
            role = "Geração de código",
            status = if (codexStatus.state.name == "IDLE") "Executando" else codexStatus.state.name,
            progress = 71,
            icon = Icons.Default.Code,
            accentColor = CosmicPurple,
            isRunning = true
        ),
        MonitoredAgent(
            id = "agent-security",
            name = "Security Agent",
            role = "Escaneamento de segurança",
            status = "Executando",
            progress = 92,
            icon = Icons.Default.Security,
            accentColor = CosmicEmerald,
            isRunning = true
        ),
        MonitoredAgent(
            id = "agent-test",
            name = "Test Agent",
            role = "Executando testes",
            status = "Executando",
            progress = 45,
            icon = Icons.Default.BugReport,
            accentColor = CosmicAmber,
            isRunning = true
        ),
        MonitoredAgent(
            id = "agent-perf",
            name = "Performance Agent",
            role = "Análise de performance",
            status = "Executando",
            progress = 67,
            icon = Icons.Default.Speed,
            accentColor = CosmicBlue,
            isRunning = true
        ),
        MonitoredAgent(
            id = "agent-deploy",
            name = "Deploy Agent",
            role = "Implantação e release",
            status = "Aguardando",
            progress = 0,
            icon = Icons.Default.RocketLaunch,
            accentColor = CosmicMuted,
            isRunning = false
        )
    )

    val filteredAgents = when (selectedFilter) {
        1 -> agents.filter { it.isRunning }
        2 -> agents.filter { !it.isRunning }
        else -> agents
    }

    CosmicStarfieldBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CosmicCard)
                            .border(1.dp, CosmicBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = CosmicWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = "Monitor de Agentes",
                        color = CosmicWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    IconButton(
                        onClick = { viewModel.speakText("Todos os agentes estão sendo monitorados em tempo real.") },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CosmicCard)
                            .border(1.dp, CosmicBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filtros",
                            tint = CosmicWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Filter Tabs (Todos, Executando, Inativos)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEachIndexed { index, label ->
                        val isSelected = selectedFilter == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CosmicWhite else CosmicCard)
                                .border(
                                    1.dp,
                                    if (isSelected) CosmicWhite else CosmicBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedFilter = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) CosmicBlack else CosmicTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Agent Rows List
            items(filteredAgents, key = { it.id }) { agent ->
                MonitoredAgentRow(
                    agent = agent,
                    onClick = {
                        viewModel.speakText("Agente ${agent.name}: ${agent.role}, progresso atual ${agent.progress} por cento.")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun MonitoredAgentRow(
    agent: MonitoredAgent,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("agent_card_${agent.id}"),
        colors = CardDefaults.cardColors(containerColor = CosmicCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                            .background(agent.accentColor.copy(alpha = 0.12f))
                            .border(1.dp, agent.accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = agent.icon,
                            contentDescription = agent.name,
                            tint = agent.accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = agent.name,
                            color = CosmicWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = agent.role,
                            color = CosmicTextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = agent.status,
                        color = if (agent.isRunning) CosmicEmerald else CosmicMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "${agent.progress}%",
                        color = CosmicWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { agent.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (agent.isRunning) CosmicEmerald else CosmicBorderBright,
                trackColor = CosmicBorder
            )
        }
    }
}
