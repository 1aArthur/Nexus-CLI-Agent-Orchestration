package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.components.CosmicStarfieldBackground
import com.example.ui.components.MasvsVulnerabilitySummaryCard
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicBlack
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicBorderBright
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicMuted
import com.example.ui.theme.CosmicRed
import com.example.ui.theme.CosmicTextSecondary
import com.example.ui.theme.CosmicWhite

data class SecurityVulnItem(
    val id: String,
    val title: String,
    val location: String,
    val severity: String,
    val severityColor: Color,
    val icon: ImageVector
)

@Composable
fun SecurityCenterScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val masvsScanResult by viewModel.masvsScanResult.collectAsState()
    val isAuditing by viewModel.isAuditing.collectAsState()

    val vulnList = listOf(
        SecurityVulnItem(
            id = "vuln-1",
            title = "Hardcoded Secret",
            location = "config/api.ts:42",
            severity = "Crítica",
            severityColor = CosmicRed,
            icon = Icons.Default.Lock
        ),
        SecurityVulnItem(
            id = "vuln-2",
            title = "Outdated Dependency",
            location = "package.json",
            severity = "Alta",
            severityColor = CosmicAmber,
            icon = Icons.Default.Warning
        ),
        SecurityVulnItem(
            id = "vuln-3",
            title = "SQL Injection",
            location = "src/db/query.ts:88",
            severity = "Alta",
            severityColor = CosmicAmber,
            icon = Icons.Default.BugReport
        ),
        SecurityVulnItem(
            id = "vuln-4",
            title = "Insecure Permission",
            location = "AndroidManifest.xml",
            severity = "Média",
            severityColor = CosmicCyan,
            icon = Icons.Default.Security
        )
    )

    CosmicStarfieldBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        text = "Centro de Segurança",
                        color = CosmicWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    IconButton(
                        onClick = { viewModel.speakText("Score de segurança atual: 92 de 100. Status excelente.") },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CosmicCard)
                            .border(1.dp, CosmicBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configurações de Segurança",
                            tint = CosmicWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Security Score Radial Dial Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("security_score_card"),
                    colors = CardDefaults.cardColors(containerColor = CosmicCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderBright.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(120.dp)) {
                                val strokeWidth = 8.dp.toPx()
                                drawCircle(
                                    color = CosmicBorderBright.copy(alpha = 0.3f),
                                    style = Stroke(width = strokeWidth)
                                )
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            CosmicEmerald.copy(alpha = 0.7f),
                                            CosmicEmerald,
                                            CosmicCyan
                                        )
                                    ),
                                    startAngle = -90f,
                                    sweepAngle = 330f,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Shield",
                                tint = CosmicEmerald,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Score de Segurança",
                            color = CosmicMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "92",
                                color = CosmicWhite,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = " /100",
                                color = CosmicMuted,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }

                        Text(
                            text = "Excelente",
                            color = CosmicEmerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // 4 Severity Counters (Críticos, Altas, Médias, Baixas)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SeverityCountCard(
                        title = "Críticos",
                        count = "0",
                        color = CosmicRed,
                        modifier = Modifier.weight(1f)
                    )
                    SeverityCountCard(
                        title = "Altas",
                        count = "2",
                        color = CosmicAmber,
                        modifier = Modifier.weight(1f)
                    )
                    SeverityCountCard(
                        title = "Médias",
                        count = "7",
                        color = CosmicCyan,
                        modifier = Modifier.weight(1f)
                    )
                    SeverityCountCard(
                        title = "Baixas",
                        count = "15",
                        color = CosmicWhite,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Run Scan Trigger Button
            item {
                Button(
                    onClick = {
                        viewModel.runMasvsStaticAnalysis(
                            "CoreAuthService.kt",
                            viewModel.currentAuditedCode.value
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CosmicWhite,
                        contentColor = CosmicBlack
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isAuditing) {
                        CircularProgressIndicator(
                            color = CosmicBlack,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ANALISANDO OWASP MASVS...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = "Auto Fix",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("EXECUTAR VARREDURA MASVS v2.0", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // MASVS Detailed Inspection Component
            masvsScanResult?.let { result ->
                item {
                    MasvsVulnerabilitySummaryCard(
                        scanResult = result,
                        onApplyPatch = { viewModel.applyMasvsPatch(it) },
                        onApplyAllPatches = { viewModel.applyAllMasvsPatches() },
                        onNarrateAdvisory = { viewModel.speakText(it) }
                    )
                }
            }

            // Últimas Vulnerabilidades Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ÚLTIMAS VULNERABILIDADES",
                        color = CosmicMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Ver todas",
                        color = CosmicCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { viewModel.speakText("Exibindo todas as vulnerabilidades detectadas pelo SAST.") }
                    )
                }
            }

            items(vulnList, key = { it.id }) { vuln ->
                SecurityVulnRow(vuln = vuln)
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun SeverityCountCard(
    title: String,
    count: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CosmicCard),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = CosmicMuted,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = count,
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun SecurityVulnRow(vuln: SecurityVulnItem) {
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
                        .background(vuln.severityColor.copy(alpha = 0.12f))
                        .border(1.dp, vuln.severityColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = vuln.icon,
                        contentDescription = vuln.title,
                        tint = vuln.severityColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column {
                    Text(
                        text = vuln.title,
                        color = CosmicWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = vuln.location,
                        color = CosmicTextSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(vuln.severityColor)
                )
                Text(
                    text = vuln.severity,
                    color = vuln.severityColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Detalhes",
                    tint = CosmicMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
