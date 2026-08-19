package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class DrawerMenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val isHighlighted: Boolean = false
)

@Composable
fun CosmicNavigationDrawerContent(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        DrawerMenuItem("dashboard", "Dashboard", Icons.Default.Home),
        DrawerMenuItem("agents_monitor", "Agentes", Icons.Default.Groups),
        DrawerMenuItem("workflows", "Workflows", Icons.Default.RocketLaunch),
        DrawerMenuItem("security_center", "Segurança", Icons.Default.Shield),
        DrawerMenuItem("terminal", "Terminal CLI", Icons.Default.Terminal),
        DrawerMenuItem("cicd_native", "CI / CD", Icons.Default.Build),
        DrawerMenuItem("tools_hub", "Bibliotecas Nativas", Icons.Default.Extension),
        DrawerMenuItem("skills", "Skills & MCP", Icons.Default.Tune),
        DrawerMenuItem("exa_search", "Pesquisa (Exa)", Icons.Default.Search),
        DrawerMenuItem("settings", "Configurações", Icons.Default.Settings)
    )

    ModalDrawerSheet(
        modifier = modifier
            .width(300.dp)
            .fillMaxHeight(),
        drawerContainerColor = CosmicBlack,
        drawerContentColor = CosmicWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CosmicCardElevated)
                        .border(1.dp, CosmicBorderBright, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Nexus Logo",
                        tint = CosmicWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = "NEXUS DEV",
                        color = CosmicWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "AI • DEVOPS • DEVSECOPS",
                        color = CosmicMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
            Spacer(modifier = Modifier.height(14.dp))

            // Navigation List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(menuItems, key = { it.route }) { item ->
                    val isSelected = currentRoute == item.route
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) CosmicWhite else Color.Transparent)
                            .clickable { onNavigate(item.route) }
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) CosmicBlack else CosmicTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = item.title,
                            color = if (isSelected) CosmicBlack else CosmicTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // Bottom Status
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CosmicEmerald)
                )
                Text(
                    text = "NEXUS AI • Online",
                    color = CosmicTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
