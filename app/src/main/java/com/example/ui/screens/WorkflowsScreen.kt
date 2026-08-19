package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.CosmicCardElevated
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicMuted
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.CosmicTextSecondary
import com.example.ui.theme.CosmicWhite

data class WorkflowTemplate(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color,
    val isFavorite: Boolean = false
)

@Composable
fun WorkflowsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Todos", "Favoritos", "Recentes")

    val templates = listOf(
        WorkflowTemplate(
            id = "wf-1",
            name = "Full Pipeline",
            description = "Pipeline completo (build, test, scan, deploy)",
            icon = Icons.Default.RocketLaunch,
            accentColor = CosmicEmerald,
            isFavorite = true
        ),
        WorkflowTemplate(
            id = "wf-2",
            name = "Security Audit",
            description = "Escaneamento completo de segurança",
            icon = Icons.Default.Security,
            accentColor = CosmicCyan,
            isFavorite = true
        ),
        WorkflowTemplate(
            id = "wf-3",
            name = "Bug Fix Flow",
            description = "Detecta e corrige bugs automaticamente",
            icon = Icons.Default.AutoFixHigh,
            accentColor = CosmicAmber
        ),
        WorkflowTemplate(
            id = "wf-4",
            name = "Release Flow",
            description = "Geração de release e deploy",
            icon = Icons.Default.RocketLaunch,
            accentColor = CosmicPurple
        ),
        WorkflowTemplate(
            id = "wf-5",
            name = "Performance Flow",
            description = "Análise e otimização de performance",
            icon = Icons.Default.Speed,
            accentColor = CosmicBlue
        ),
        WorkflowTemplate(
            id = "wf-6",
            name = "Research Flow",
            description = "Pesquisa e análise de tecnologias",
            icon = Icons.Default.Search,
            accentColor = CosmicEmerald
        )
    )

    val filteredList = templates.filter {
        (selectedFilter == 0 || (selectedFilter == 1 && it.isFavorite) || selectedFilter == 2) &&
        (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true))
    }

    CosmicStarfieldBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Bar
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
                        text = "Workflows",
                        color = CosmicWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    IconButton(
                        onClick = { viewModel.speakText("Menu de gerenciamento de workflows") },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CosmicCard)
                            .border(1.dp, CosmicBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configurações",
                            tint = CosmicWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Search Bar & Plus Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("search_workflows_input"),
                        placeholder = { Text("Buscar workflows...", color = CosmicMuted, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = CosmicMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CosmicCyan,
                            unfocusedBorderColor = CosmicBorder,
                            focusedContainerColor = CosmicCard,
                            unfocusedContainerColor = CosmicCard,
                            focusedTextColor = CosmicWhite,
                            unfocusedTextColor = CosmicWhite
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CosmicWhite)
                            .clickable {
                                viewModel.startOrchestration("Novo workflow customizado")
                                viewModel.speakText("Iniciando criação de novo workflow.")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar Workflow",
                            tint = CosmicBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Filter Chips (Todos, Favoritos, Recentes)
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

            // Workflow Cards List
            items(filteredList, key = { it.id }) { template ->
                WorkflowCardRow(
                    template = template,
                    onExecute = {
                        viewModel.startOrchestration(template.name)
                        viewModel.speakText("Iniciando execução do workflow ${template.name}.")
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
fun WorkflowCardRow(
    template: WorkflowTemplate,
    onExecute: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExecute() },
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = template.name,
                    color = CosmicWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = template.description,
                    color = CosmicTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CosmicCardElevated)
                    .border(1.dp, CosmicBorderBright, CircleShape)
                    .clickable { onExecute() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Executar",
                    tint = CosmicWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
