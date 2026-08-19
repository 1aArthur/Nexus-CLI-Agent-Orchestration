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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AgentDebateEntity
import com.example.data.local.AgentWorkflowEntity
import com.example.data.local.ProjectBrainEntity
import com.example.ui.MainViewModel
import com.example.ui.components.AgentStatusCard
import com.example.ui.components.CodeViewer
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalSurface

@Composable
fun OrchestratorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val claudeStatus by viewModel.claudeStatus.collectAsState()
    val codexStatus by viewModel.codexStatus.collectAsState()
    val workflows by viewModel.workflows.collectAsState()
    val debates by viewModel.debates.collectAsState()
    val adrs by viewModel.projectAdrs.collectAsState()
    val isOrchestrating by viewModel.isOrchestrating.collectAsState()
    val autopilotState by viewModel.autopilotState.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var goalInput by remember { mutableStateOf("Build a high-performance SIMD audio DSP engine with Rust/C++ NDK bindings and Room persistence") }
    var debateTopic by remember { mutableStateOf("Rust vs C++20 for Android NDK SIMD & Memory Safety") }

    val tabs = listOf("SWARM", "AUTOPILOT", "DEBATE", "PROJECT BRAIN")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TerminalBackground)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = TerminalSurface,
            contentColor = NeonCyan,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NeonCyan,
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

        when (selectedTab) {
            0 -> SwarmOrchestrationTab(
                claudeStatus = claudeStatus,
                codexStatus = codexStatus,
                workflows = workflows,
                isOrchestrating = isOrchestrating,
                goalInput = goalInput,
                onGoalChange = { goalInput = it },
                onStartOrchestration = { viewModel.startOrchestration(goalInput) },
                onSpeak = { viewModel.speakText(it) }
            )
            1 -> AutopilotTab(
                autopilotState = autopilotState,
                onRunAutopilot = { viewModel.runAutopilot(goalInput) },
                onSpeak = { viewModel.speakText(it) }
            )
            2 -> DebateTab(
                debates = debates,
                debateTopic = debateTopic,
                onTopicChange = { debateTopic = it },
                onRunDebate = { viewModel.runDebate(debateTopic) },
                onSpeak = { viewModel.speakText(it) }
            )
            3 -> ProjectBrainTab(
                adrs = adrs,
                onSpeak = { viewModel.speakText(it) }
            )
        }
    }
}

@Composable
fun SwarmOrchestrationTab(
    claudeStatus: com.example.domain.model.AgentLiveStatus,
    codexStatus: com.example.domain.model.AgentLiveStatus,
    workflows: List<AgentWorkflowEntity>,
    isOrchestrating: Boolean,
    goalInput: String,
    onGoalChange: (String) -> Unit,
    onStartOrchestration: () -> Unit,
    onSpeak: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        text = "MULTI-AGENT SWARM ORCHESTRATION",
                        color = NeonPurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Claude Code (Architect) + Codex (Native NDK) + DevSecOps Sentinel",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = goalInput,
                        onValueChange = onGoalChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("swarm_goal_input"),
                        label = { Text("Orchestration Goal / Spec", color = NeonCyan, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onStartOrchestration,
                        enabled = !isOrchestrating && goalInput.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("start_orchestration_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isOrchestrating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SWARM ORCHESTRATING...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Orchestrate", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("EXECUTE SWARM ENGINE", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    AgentStatusCard(status = claudeStatus, onSpeakThoughts = onSpeak)
                }
                Box(modifier = Modifier.weight(1f)) {
                    AgentStatusCard(status = codexStatus, onSpeakThoughts = onSpeak)
                }
            }
        }

        item {
            Text(
                text = "ORCHESTRATION ARTIFACTS (${workflows.size})",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        items(workflows, key = { it.id }) { workflow ->
            WorkflowHistoryCard(workflow = workflow, onNarrate = onSpeak)
        }
    }
}

@Composable
fun AutopilotTab(
    autopilotState: com.example.domain.model.AutopilotProgress,
    onRunAutopilot: () -> Unit,
    onSpeak: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AUTOPILOT ENGINEERING LOOP (12 PHASES)",
                            color = NeonEmerald,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = "Autopilot", tint = NeonEmerald)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Continuous automated cycle: Research -> Architecture -> Implementation -> Tests -> DevSecOps MASVS -> Performance -> Release until 0 Critical bugs & 0 Regressions.",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { if (autopilotState.totalSteps > 0) autopilotState.stepIndex.toFloat() / autopilotState.totalSteps else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonEmerald,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = autopilotState.currentStep,
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${autopilotState.stepIndex}/${autopilotState.totalSteps}",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onRunAutopilot,
                        enabled = !autopilotState.active,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("run_autopilot_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("LAUNCH AUTOPILOT FULL LOOP", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF070B12)),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "CONVERGENCE GATES STATUS",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ConvergenceRow("BUILD COMPILATION", autopilotState.buildPass)
                    ConvergenceRow("ROBOLECTRIC & UNIT TESTS", autopilotState.testsPass)
                    ConvergenceRow("DEVSECOPS OWASP MASVS v2.0", autopilotState.securityAcceptable)
                    ConvergenceRow("LINT & STATIC CHECKS", autopilotState.lintPass)
                    ConvergenceRow("CRITICAL BUGS = 0", autopilotState.criticalBugsCount == 0)
                    ConvergenceRow("REGRESSIONS = 0", autopilotState.regressionsCount == 0)
                }
            }
        }

        items(autopilotState.logs) { log ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF070A12))
                    .border(1.dp, TerminalBorder, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Text(text = log, color = NeonEmerald, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

@Composable
fun ConvergenceRow(name: String, passed: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, color = Color.LightGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (passed) Color(0xFF064E3B) else Color(0xFF7F1D1D))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = if (passed) "PASS" else "PENDING",
                color = if (passed) NeonEmerald else Color(0xFFF87171),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun DebateTab(
    debates: List<AgentDebateEntity>,
    debateTopic: String,
    onTopicChange: (String) -> Unit,
    onRunDebate: () -> Unit,
    onSpeak: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        text = "AGENT DEBATE ENGINE & JUDGE",
                        color = NeonAmber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Architect vs Security vs Performance Agents -> Final Consensus Judge",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = debateTopic,
                        onValueChange = onTopicChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("debate_topic_input"),
                        label = { Text("Debate Proposition / Architecture Choice", color = NeonAmber, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonAmber,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onRunDebate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("run_debate_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonAmber, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Gavel, contentDescription = "Judge", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("LAUNCH AGENT DEBATE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        items(debates, key = { it.id }) { debate ->
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
                        Text(
                            text = debate.topic,
                            color = NeonCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { onSpeak("Judge verdict: ${debate.judgeVerdict}") }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Speak", tint = NeonCyan, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF064E3B))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "DECISION: ${debate.chosenOption} (Confidence: ${debate.confidenceScore}%)",
                            color = NeonEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = debate.judgeVerdict,
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectBrainTab(
    adrs: List<ProjectBrainEntity>,
    onSpeak: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "PROJECT BRAIN - ARCHITECTURE DECISION RECORDS (${adrs.size})",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        items(adrs, key = { it.id }) { adr ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${adr.adrId}: ${adr.title}",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { onSpeak("${adr.adrId} decision: ${adr.decision}. Rationale: ${adr.rationale}") }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Speak", tint = NeonCyan, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Decision: ${adr.decision}", color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Rationale: ${adr.rationale}", color = Color.LightGray, fontSize = 10.sp, lineHeight = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Evidence: ${adr.evidence}", color = NeonAmber, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
fun WorkflowHistoryCard(
    workflow: AgentWorkflowEntity,
    onNarrate: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = workflow.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Tokens: ${workflow.tokenUsage} | Cost: $${String.format("%.4f", workflow.estimatedCostUsd)}",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onNarrate("Workflow summary: ${workflow.executionSummary}") }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Read", tint = NeonCyan, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = "Expand", tint = Color.Gray)
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(text = workflow.executionSummary, color = Color.LightGray, fontSize = 11.sp)
                    if (workflow.generatedCode.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CodeViewer(code = workflow.generatedCode, language = "kotlin")
                    }
                }
            }
        }
    }
}
