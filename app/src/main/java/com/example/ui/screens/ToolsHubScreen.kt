package com.example.ui.screens

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.McpToolEntity
import com.example.data.remote.ExaResult
import com.example.ui.MainViewModel
import com.example.ui.components.AudioWaveformPlayer
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalSurface

@Composable
fun ToolsHubScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Exa Search", "Grok TTS Voice", "MCP Gateway Hub", "AI Diagram Gen")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .padding(12.dp)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = TerminalSurface,
            contentColor = NeonAmber,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NeonAmber,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> ExaSearchTab(viewModel)
            1 -> GrokTtsTab(viewModel)
            2 -> McpGatewayTab(viewModel)
            3 -> DiagramGeneratorTab(viewModel)
        }
    }
}

@Composable
fun ExaSearchTab(viewModel: MainViewModel) {
    val results by viewModel.exaResults.collectAsState()
    val isSearching by viewModel.isSearchingExa.collectAsState()
    var query by remember { mutableStateOf("Android NDK C++20 SIMD optimizations") }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonAmber.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "EXA NEURAL CODE & WEB SEARCH",
                        color = NeonAmber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Real-time web grounding, GitHub repositories & official docs",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exa_search_input"),
                        placeholder = { Text("Search code patterns, CVEs, or NDK architecture...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonAmber,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.searchExa(query) },
                        enabled = !isSearching && query.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("execute_exa_search_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonAmber, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SEARCHING EXA ENGINE...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("EXECUTE NEURAL SEARCH", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        items(results) { result ->
            ExaResultCard(result = result) { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun McpGatewayTab(viewModel: MainViewModel) {
    val mcpTools by viewModel.mcpTools.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MODEL CONTEXT PROTOCOL (MCP) GATEWAY",
                            color = NeonPurple,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Icon(imageVector = Icons.Default.Hub, contentDescription = "MCP", tint = NeonPurple)
                    }
                    Text(
                        text = "Unified tools bridge with granular agent permission policies (READ, WRITE, EXECUTE, DEPLOY, SECRET)",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        items(mcpTools, key = { it.id }) { tool ->
            McpToolCard(tool = tool, onToggle = { viewModel.toggleMcpTool(tool) })
        }
    }
}

@Composable
fun McpToolCard(tool: McpToolEntity, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (tool.enabled) NeonPurple.copy(alpha = 0.4f) else TerminalBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = tool.toolName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = tool.description, color = Color.Gray, fontSize = 11.sp)
                }
                Switch(
                    checked = tool.enabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = Color(0xFF00363A))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                PermissionBadge("READ", tool.readAllowed)
                PermissionBadge("WRITE", tool.writeAllowed)
                PermissionBadge("EXEC", tool.executeAllowed)
                PermissionBadge("DEPLOY", tool.deployAllowed)
                PermissionBadge("SECRET", tool.secretAccessAllowed)
            }
        }
    }
}

@Composable
fun PermissionBadge(name: String, allowed: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (allowed) Color(0xFF064E3B) else Color(0xFF1E293B))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$name: ${if (allowed) "✓" else "✕"}",
            color = if (allowed) NeonEmerald else Color.Gray,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun GrokTtsTab(viewModel: MainViewModel) {
    var speechText by remember { mutableStateOf("Nexus Dev Orchestrator Autopilot successfully verified 12 phases. Zero critical vulnerabilities detected.") }
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val currentUtterance by viewModel.currentUtterance.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "GROK / xAI VOICE STUDIO (TTS)",
                        color = NeonPurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = speechText,
                        onValueChange = { speechText = it },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AudioWaveformPlayer(
                        isPlaying = isSpeaking,
                        currentUtterance = currentUtterance,
                        onStop = { viewModel.stopSpeaking() }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.speakText(speechText) },
                            modifier = Modifier.weight(1f).height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("SYNTHESIZE", fontWeight = FontWeight.Bold)
                        }

                        if (isSpeaking) {
                            Button(
                                onClick = { viewModel.stopSpeaking() },
                                modifier = Modifier.height(44.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonRed, contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiagramGeneratorTab(viewModel: MainViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "AI ARCHITECTURE DIAGRAM ENGINE",
                color = NeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Generates C4 & DAG system architecture diagrams via Gemini 3.1 Pro Image models.",
                color = Color.LightGray,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun ExaResultCard(result: ExaResult, onOpenUrl: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = result.title ?: "Technical Resource", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = { onOpenUrl(result.url ?: "https://developer.android.com") }) {
                    Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = "Open", tint = NeonAmber, modifier = Modifier.size(18.dp))
                }
            }
            Text(text = result.url ?: "", color = NeonCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = result.text ?: "", color = Color.LightGray, fontSize = 10.sp, lineHeight = 14.sp)
        }
    }
}
