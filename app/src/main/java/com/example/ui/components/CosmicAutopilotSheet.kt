package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicBlack
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

data class AutopilotAction(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CosmicAutopilotSheet(
    onDismiss: () -> Unit,
    onSelectAction: (String) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val actions = listOf(
        AutopilotAction(
            id = "full_autopilot",
            title = "Autopilot Swarm Completo",
            description = "Claude + Codex executando pipeline ponta a ponta",
            icon = Icons.Default.RocketLaunch,
            accentColor = CosmicCyan
        ),
        AutopilotAction(
            id = "security_scan",
            title = "Varredura OWASP MASVS v2.0",
            description = "Auditoria de segurança estática e auto-patch",
            icon = Icons.Default.Security,
            accentColor = CosmicEmerald
        ),
        AutopilotAction(
            id = "exa_research",
            title = "Pesquisa Neural Exa",
            description = "Indexação de documentação e CVEs recentes",
            icon = Icons.Default.Search,
            accentColor = CosmicAmber
        ),
        AutopilotAction(
            id = "terminal_exec",
            title = "Terminal CLI Interativo",
            description = "Acessar console Zsh/NuShell e monitor de logs",
            icon = Icons.Default.Terminal,
            accentColor = CosmicPurple
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CosmicBlack,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(CosmicBorderBright)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CosmicCardElevated)
                        .border(1.dp, CosmicBorderBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Cosmic Swarm",
                        tint = CosmicWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = "COMANDOS RÁPIDOS & SWARM",
                        color = CosmicWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Orquestração neural de alta velocidade",
                        color = CosmicMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                actions.forEach { action ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CosmicCard)
                            .border(1.dp, CosmicBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                onSelectAction(action.id)
                                onDismiss()
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(action.accentColor.copy(alpha = 0.12f))
                                .border(1.dp, action.accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.title,
                                tint = action.accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = action.title,
                                color = CosmicWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = action.description,
                                color = CosmicTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
