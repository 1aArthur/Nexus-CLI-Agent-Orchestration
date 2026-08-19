package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.local.PipelineRunEntity
import com.example.domain.model.NativeModuleSpec
import com.example.domain.model.PerformanceMetricsProfile
import com.example.domain.model.PipelineStageInfo
import com.example.ui.MainViewModel
import com.example.ui.components.CodeViewer
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalSurface

@Composable
fun CicdNativeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val pipelineRuns by viewModel.pipelineRuns.collectAsState()
    val isDeploying by viewModel.isDeploying.collectAsState()
    val liveStages by viewModel.currentPipelineStages.collectAsState()
    val nativeSpec by viewModel.latestNativeSpec.collectAsState()
    val perfProfile by viewModel.performanceProfile.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("PERFORMANCE LAB", "NATIVE NDK & RUST", "CI/CD DEPLOYMENT")

    var moduleName by remember { mutableStateOf("crypto_simd_fast") }
    var methodName by remember { mutableStateOf("processBuffer") }
    var selectedLanguage by remember { mutableStateOf("C++20") }
    var pipelineTarget by remember { mutableStateOf("Google Play Production") }

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
            0 -> PerformanceLabTab(
                profile = perfProfile,
                onRefresh = { viewModel.refreshPerformanceProfile() }
            )
            1 -> NativeNdkTab(
                moduleName = moduleName,
                methodName = methodName,
                language = selectedLanguage,
                nativeSpec = nativeSpec,
                onModuleNameChange = { moduleName = it },
                onMethodNameChange = { methodName = it },
                onLanguageChange = { selectedLanguage = it },
                onGenerate = { viewModel.generateNativeModule(moduleName, methodName, selectedLanguage) }
            )
            2 -> CicdDeploymentTab(
                pipelineTarget = pipelineTarget,
                isDeploying = isDeploying,
                liveStages = liveStages,
                pipelineRuns = pipelineRuns,
                onTargetChange = { pipelineTarget = it },
                onTrigger = { viewModel.triggerPipeline(pipelineTarget, "PRODUCTION") }
            )
        }
    }
}

@Composable
fun PerformanceLabTab(
    profile: PerformanceMetricsProfile,
    onRefresh: () -> Unit
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
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PERFORMANCE LAB & PROFILING GAUGES",
                            color = NeonEmerald,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        IconButton(onClick = onRefresh) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = NeonEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    MetricGaugeRow("CPU LOAD", "${profile.cpuUtilizationPercent}%", profile.cpuUtilizationPercent / 100f, NeonCyan)
                    MetricGaugeRow("RAM FOOTPRINT", "${profile.memoryUsageMb} MB", profile.memoryUsageMb / 1000f, NeonPurple)
                    MetricGaugeRow("APK ARTIFACT SIZE", "${profile.apkSizeMb} MB", profile.apkSizeMb.toFloat() / 20f, NeonEmerald)
                    MetricGaugeRow("DEX METHOD COUNT", "${profile.dexMethodCount} / 65K", profile.dexMethodCount / 65536f, NeonAmber)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF070B12))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("COLD START", color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                Text("${profile.coldStartTimeMs} ms", color = NeonCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF070B12))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("WARM START", color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                Text("${profile.warmStartTimeMs} ms", color = NeonEmerald, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF070B12))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("AVG GC PAUSE", color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                Text("${profile.gcPauseAverageMs} ms", color = NeonAmber, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricGaugeRow(name: String, value: String, fraction: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = name, color = Color.LightGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Text(text = value, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { fraction.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFF1E293B)
        )
    }
}

@Composable
fun NativeNdkTab(
    moduleName: String,
    methodName: String,
    language: String,
    nativeSpec: NativeModuleSpec?,
    onModuleNameChange: (String) -> Unit,
    onMethodNameChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onGenerate: () -> Unit
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
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "NATIVE NDK & RUST JNI ACCELERATOR",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Synthesizes C++20 / Rust SIMD core, JNI headers, CMakeLists.txt and Kotlin wrapper",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = moduleName,
                            onValueChange = onModuleNameChange,
                            modifier = Modifier.weight(1f),
                            label = { Text("Module Name", color = NeonCyan, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = TerminalBorder,
                                focusedContainerColor = Color(0xFF070B12),
                                unfocusedContainerColor = Color(0xFF070B12)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = methodName,
                            onValueChange = onMethodNameChange,
                            modifier = Modifier.weight(1f),
                            label = { Text("Native Method", color = NeonCyan, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = TerminalBorder,
                                focusedContainerColor = Color(0xFF070B12),
                                unfocusedContainerColor = Color(0xFF070B12)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onGenerate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("generate_native_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Code, contentDescription = "Synthesize", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SYNTHESIZE NATIVE NDK & CMAKE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (nativeSpec != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "CMakeLists.txt", color = NeonCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        CodeViewer(code = nativeSpec.cmakeListsContent, language = "cmake")

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "${nativeSpec.moduleName}_native.cpp (C++20)", color = NeonEmerald, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        CodeViewer(code = nativeSpec.cppSource, language = "cpp")

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "Kotlin JNI Wrapper", color = NeonPurple, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        CodeViewer(code = nativeSpec.kotlinWrapperCode, language = "kotlin")
                    }
                }
            }
        }
    }
}

@Composable
fun CicdDeploymentTab(
    pipelineTarget: String,
    isDeploying: Boolean,
    liveStages: List<PipelineStageInfo>,
    pipelineRuns: List<PipelineRunEntity>,
    onTargetChange: (String) -> Unit,
    onTrigger: () -> Unit
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
                        text = "CI/CD MULTI-TARGET DEPLOYMENT",
                        color = NeonPurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Automated OIDC token exchange, SAST verification, tests, and signed APK release",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pipelineTarget,
                        onValueChange = onTargetChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Deployment Target (Google Play / GitHub / Firebase)", color = NeonPurple, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onTrigger,
                        enabled = !isDeploying,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("trigger_pipeline_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isDeploying) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DEPLOYING...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = "Deploy", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TRIGGER CI/CD PIPELINE", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (liveStages.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF070B12)),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "LIVE PIPELINE STAGES", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(6.dp))
                        liveStages.forEach { stage ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = stage.name, color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                Text(
                                    text = stage.status,
                                    color = if (stage.status == "SUCCESS") NeonEmerald else if (stage.status == "RUNNING") NeonCyan else Color.Gray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        items(pipelineRuns, key = { it.id }) { run ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = run.pipelineName, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = run.status, color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Artifact: ${run.artifactName} | Duration: ${run.durationSeconds}s", color = NeonCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = run.logs.take(160) + "...", color = Color.LightGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
