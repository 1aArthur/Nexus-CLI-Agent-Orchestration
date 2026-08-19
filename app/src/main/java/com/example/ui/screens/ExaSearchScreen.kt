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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCardElevated
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicMuted
import com.example.ui.theme.CosmicTextSecondary
import com.example.ui.theme.CosmicWhite

data class ExaSearchMockItem(
    val id: String,
    val title: String,
    val url: String,
    val tag: String,
    val tagColor: Color,
    val icon: ImageVector
)

@Composable
fun ExaSearchScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("Melhores práticas para segurança Android 2024") }
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Todos", "Documentação", "GitHub", "Notícias")

    val isSearching by viewModel.isSearchingExa.collectAsState()
    val exaResults by viewModel.exaResults.collectAsState()

    val defaultResults = listOf(
        ExaSearchMockItem(
            id = "res-1",
            title = "Android Security Best Practices",
            url = "developer.android.com • 2024",
            tag = "Docs",
            tagColor = CosmicCyan,
            icon = Icons.Default.Description
        ),
        ExaSearchMockItem(
            id = "res-2",
            title = "OWASP MASVS v2.0 Guidelines",
            url = "owasp.org • 2024",
            tag = "Docs",
            tagColor = CosmicCyan,
            icon = Icons.Default.Description
        ),
        ExaSearchMockItem(
            id = "res-3",
            title = "Android Security Checklist",
            url = "github.com/OWASP/owasp-mastg",
            tag = "GitHub",
            tagColor = CosmicWhite,
            icon = Icons.Default.Code
        ),
        ExaSearchMockItem(
            id = "res-4",
            title = "Top 10 Android Security Risks",
            url = "blog.sonarsource.com • 2024",
            tag = "Blog",
            tagColor = CosmicAmber,
            icon = Icons.Default.Language
        ),
        ExaSearchMockItem(
            id = "res-5",
            title = "Secure Coding Practices for Android",
            url = "medium.com • 2024",
            tag = "Blog",
            tagColor = CosmicAmber,
            icon = Icons.Default.Language
        )
    )

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
                        text = "Exa Search",
                        color = CosmicWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    IconButton(
                        onClick = { viewModel.speakText("Motor de busca neural Exa conectado.") },
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

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("exa_search_input"),
                    label = { Text("Pesquisar na web com Exa", color = CosmicMuted, fontSize = 11.sp) },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.searchExa(searchQuery) }) {
                            if (isSearching) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = CosmicCyan, strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Pesquisar", tint = CosmicCyan)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CosmicCyan,
                        unfocusedBorderColor = CosmicBorder,
                        focusedContainerColor = CosmicCard,
                        unfocusedContainerColor = CosmicCard,
                        focusedTextColor = CosmicWhite,
                        unfocusedTextColor = CosmicWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Filter Tabs (Todos, Documentação, GitHub, Notícias)
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
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Section Results Title
            item {
                Text(
                    text = "RESULTADOS (${if (exaResults.isNotEmpty()) exaResults.size else 24})",
                    color = CosmicMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Render Results
            if (exaResults.isNotEmpty()) {
                items(exaResults, key = { it.url ?: it.hashCode().toString() }) { res ->
                    val resultTitle = res.title ?: "Sem título"
                    val resultUrl = res.url ?: "https://nexus.ai"
                    ExaResultRow(
                        title = resultTitle,
                        url = resultUrl,
                        tag = "Exa Neural",
                        tagColor = CosmicCyan,
                        icon = Icons.Default.Language,
                        onClick = { viewModel.speakText(resultTitle) }
                    )
                }
            } else {
                items(defaultResults, key = { it.id }) { item ->
                    ExaResultRow(
                        title = item.title,
                        url = item.url,
                        tag = item.tag,
                        tagColor = item.tagColor,
                        icon = item.icon,
                        onClick = { viewModel.speakText(item.title) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun ExaResultRow(
    title: String,
    url: String,
    tag: String,
    tagColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CosmicCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = tag,
                        tint = CosmicWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        color = CosmicWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = url,
                        color = CosmicTextSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CosmicCardElevated)
                    .border(1.dp, CosmicBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tag,
                    color = tagColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
