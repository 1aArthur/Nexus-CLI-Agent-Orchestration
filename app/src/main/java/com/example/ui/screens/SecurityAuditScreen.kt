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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.local.SecurityAuditEntity
import com.example.domain.model.SecurityFinding
import com.example.ui.MainViewModel
import com.example.ui.components.CodeViewer
import com.example.ui.components.MasvsVulnerabilitySummaryCard
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalSurface

@Composable
fun SecurityAuditScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val audits by viewModel.securityAudits.collectAsState()
    val isAuditing by viewModel.isAuditing.collectAsState()
    val masvsScanResult by viewModel.masvsScanResult.collectAsState()
    val currentAuditedCode by viewModel.currentAuditedCode.collectAsState()

    var projectName by remember { mutableStateOf("CoreAuthService.kt") }
    var codeToAudit by remember(currentAuditedCode) { mutableStateOf(currentAuditedCode) }

    var selectedAuditTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("MASVS SCANNER", "AUTO-FIX WORKTREE", "CYCLONEDX SBOM")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TerminalBackground)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedAuditTab,
            containerColor = TerminalSurface,
            contentColor = NeonCyan,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedAuditTab]),
                    color = NeonCyan,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedAuditTab == index,
                    onClick = { selectedAuditTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedAuditTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Interactive Input Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DEVSECOPS SECURITY & SAST SCANNER",
                                color = NeonRed,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Icon(imageVector = Icons.Default.Shield, contentDescription = "Security", tint = NeonRed)
                        }
                        Text(
                            text = "OWASP MASVS / MASTG v2.0 baseline, CodeQL, Semgrep & Gitleaks integration",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = projectName,
                            onValueChange = { projectName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Component / Target Module", color = NeonCyan, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = TerminalBorder,
                                focusedContainerColor = Color(0xFF070B12),
                                unfocusedContainerColor = Color(0xFF070B12)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = codeToAudit,
                            onValueChange = {
                                codeToAudit = it
                                viewModel.runMasvsStaticAnalysis(projectName, it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .testTag("code_audit_input"),
                            label = { Text("Source Code / Configuration to Audit", color = NeonCyan, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = TerminalBorder,
                                focusedContainerColor = Color(0xFF070B12),
                                unfocusedContainerColor = Color(0xFF070B12)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.runMasvsStaticAnalysis(projectName, codeToAudit)
                                    viewModel.runSecurityAudit(projectName, codeToAudit)
                                },
                                enabled = !isAuditing && codeToAudit.isNotBlank(),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("run_security_audit_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonRed, contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (isAuditing) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("AUDITING MASVS...", fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(imageVector = Icons.Default.BugReport, contentDescription = "Audit", modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("RUN MASVS AUDIT", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Tab 0: MASVS Static Analysis Summary Card
            if (selectedAuditTab == 0) {
                masvsScanResult?.let { result ->
                    item {
                        MasvsVulnerabilitySummaryCard(
                            scanResult = result,
                            onApplyPatch = { finding ->
                                viewModel.applyMasvsPatch(finding)
                            },
                            onApplyAllPatches = {
                                viewModel.applyAllMasvsPatches()
                            },
                            onNarrateAdvisory = { text ->
                                viewModel.speakText(text)
                            }
                        )
                    }
                }
            }

            // Historical Audits List
            items(audits, key = { it.id }) { audit ->
                val findings = viewModel.getAuditFindings(audit)
                when (selectedAuditTab) {
                    0 -> VulnerabilitiesAuditCard(
                        audit = audit,
                        findings = findings,
                        onNarrate = { viewModel.speakText(it) },
                        onApproveFix = { viewModel.approveSecurityFix(audit) }
                    )
                    1 -> AutoFixWorktreeCard(
                        audit = audit,
                        onApproveFix = { viewModel.approveSecurityFix(audit) }
                    )
                    2 -> SbomCard(audit = audit)
                }
            }
        }
    }
}

@Composable
fun VulnerabilitiesAuditCard(
    audit: SecurityAuditEntity,
    findings: List<SecurityFinding>,
    onNarrate: (String) -> Unit,
    onApproveFix: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = audit.projectName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "OWASP: ${audit.masvsCategory}", color = NeonCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (audit.severityScore == "CRITICAL" || audit.severityScore == "HIGH") Color(0xFF7F1D1D) else Color(0xFF064E3B))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = audit.severityScore,
                            color = if (audit.severityScore == "CRITICAL" || audit.severityScore == "HIGH") NeonRed else NeonEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    IconButton(onClick = { onNarrate("Security report for ${audit.projectName}: Severity ${audit.severityScore}. ${audit.optimizationSuggestion}") }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Read", tint = NeonCyan, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            findings.forEach { finding ->
                FindingItemRow(finding = finding)
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!audit.autoFixApproved && audit.totalVulnerabilities > 0) {
                Button(
                    onClick = onApproveFix,
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald, contentColor = Color.Black),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Approve", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("APPROVE & APPLY AUTO-FIX PATCH", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            } else if (audit.autoFixApproved) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Approved", tint = NeonEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("AUTO-FIX VERIFIED & COMMITTED TO BRANCH", color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FindingItemRow(finding: SecurityFinding) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF070B12))
            .border(1.dp, TerminalBorder, RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "[${finding.id}] ${finding.title}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = finding.severity.name, color = Color(finding.severity.colorHex), fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
        Text(text = "${finding.masvsRef} • ${finding.cwe}", color = NeonAmber, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
        Text(text = finding.description, color = Color.LightGray, fontSize = 10.sp, lineHeight = 14.sp)
    }
}

@Composable
fun AutoFixWorktreeCard(
    audit: SecurityAuditEntity,
    onApproveFix: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "ISOLATED WORKTREE AUTO-PATCH: ${audit.projectName}",
                color = NeonEmerald,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = audit.optimizationSuggestion, color = Color.White, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(8.dp))
            CodeViewer(code = audit.patchedCode, language = "kotlin")
        }
    }
}

@Composable
fun SbomCard(audit: SecurityAuditEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "CYCLONEDX SBOM (SOFTWARE BILL OF MATERIALS)",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(8.dp))
            CodeViewer(code = audit.sbomJson, language = "json")
        }
    }
}
