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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val customGeminiKey by viewModel.customGeminiKey.collectAsState()
    val customExaKey by viewModel.customExaKey.collectAsState()

    var showKeyDialog by remember { mutableStateOf(false) }
    var keyDialogTitle by remember { mutableStateOf("") }
    var keyDialogValue by remember { mutableStateOf("") }
    var keyDialogTarget by remember { mutableStateOf("") }

    var isVoiceEnabled by remember { mutableStateOf(true) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }

    CosmicStarfieldBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Configurações",
                    color = CosmicWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // User Profile Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("user_profile_card"),
                    colors = CardDefaults.cardColors(containerColor = CosmicCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(CosmicCardElevated)
                                    .border(1.5.dp, CosmicBorderBright, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Nexus User",
                                    tint = CosmicWhite,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "NEXUS USER",
                                    color = CosmicWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "nexus.dev@example.com",
                                    color = CosmicTextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CosmicPurple.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Plano Pro",
                                        color = CosmicPurple,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.speakText("Perfil Nexus Developer Pro ativo.") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CosmicCardElevated,
                                contentColor = CosmicWhite
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                        ) {
                            Text("Gerenciar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // APIS E SERVIÇOS Section
            item {
                Text(
                    text = "APIS E SERVIÇOS",
                    color = CosmicMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CosmicCard),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                ) {
                    Column {
                        SettingApiRow(
                            title = "Exa API Key",
                            maskedValue = if (customExaKey != null) "••••••••••••" else "Padrão Integrado",
                            icon = Icons.Default.Search,
                            onClick = {
                                keyDialogTitle = "Configurar Exa API Key"
                                keyDialogValue = customExaKey ?: ""
                                keyDialogTarget = "EXA"
                                showKeyDialog = true
                            }
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
                        SettingApiRow(
                            title = "xAI (Grok TTS) API Key",
                            maskedValue = "••••••••••••",
                            icon = Icons.Default.VolumeUp,
                            onClick = {
                                keyDialogTitle = "Configurar Grok Voice Key"
                                keyDialogValue = ""
                                keyDialogTarget = "GROK"
                                showKeyDialog = true
                            }
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
                        SettingApiRow(
                            title = "Claude / Gemini API Key",
                            maskedValue = if (customGeminiKey != null) "••••••••••••" else "Padrão Integrado",
                            icon = Icons.Default.Key,
                            onClick = {
                                keyDialogTitle = "Configurar Gemini / Claude Key"
                                keyDialogValue = customGeminiKey ?: ""
                                keyDialogTarget = "GEMINI"
                                showKeyDialog = true
                            }
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
                        SettingApiRow(
                            title = "GitHub Token",
                            maskedValue = "••••••••••••",
                            icon = Icons.Default.Code,
                            onClick = {
                                keyDialogTitle = "Configurar GitHub Personal Access Token"
                                keyDialogValue = ""
                                keyDialogTarget = "GITHUB"
                                showKeyDialog = true
                            }
                        )
                    }
                }
            }

            // GERAL Section
            item {
                Text(
                    text = "GERAL",
                    color = CosmicMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CosmicCard),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder)
                ) {
                    Column {
                        SettingGeneralRow(
                            title = "Tema",
                            status = "Escuro",
                            icon = Icons.Default.DarkMode,
                            onClick = { viewModel.speakText("Tema escuro com estrelas brancas em movimento ativado.") }
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
                        SettingGeneralRow(
                            title = "Voz (Grok TTS)",
                            status = if (isVoiceEnabled) "Ativado" else "Desativado",
                            icon = Icons.Default.RecordVoiceOver,
                            onClick = {
                                isVoiceEnabled = !isVoiceEnabled
                                viewModel.speakText(if (isVoiceEnabled) "Voz Grok TTS ativada" else "Voz Grok TTS desativada")
                            }
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
                        SettingGeneralRow(
                            title = "Notificações",
                            status = if (isNotificationsEnabled) "Ativado" else "Desativado",
                            icon = Icons.Default.Notifications,
                            onClick = { isNotificationsEnabled = !isNotificationsEnabled }
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CosmicBorder))
                        SettingGeneralRow(
                            title = "Idioma",
                            status = "Português",
                            icon = Icons.Default.Language,
                            onClick = { viewModel.speakText("Idioma configurado para Português.") }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    if (showKeyDialog) {
        AlertDialog(
            onDismissRequest = { showKeyDialog = false },
            containerColor = CosmicCard,
            title = {
                Text(text = keyDialogTitle, color = CosmicWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                OutlinedTextField(
                    value = keyDialogValue,
                    onValueChange = { keyDialogValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Chave de API", color = CosmicCyan, fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CosmicCyan,
                        unfocusedBorderColor = CosmicBorder,
                        focusedTextColor = CosmicWhite,
                        unfocusedTextColor = CosmicWhite,
                        focusedContainerColor = CosmicBlack,
                        unfocusedContainerColor = CosmicBlack
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (keyDialogTarget == "EXA") viewModel.setCustomExaKey(keyDialogValue)
                        if (keyDialogTarget == "GEMINI") viewModel.setCustomGeminiKey(keyDialogValue)
                        showKeyDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CosmicWhite, contentColor = CosmicBlack)
                ) {
                    Text("Salvar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showKeyDialog = false }) {
                    Text("Cancelar", color = CosmicMuted)
                }
            }
        )
    }
}

@Composable
fun SettingApiRow(
    title: String,
    maskedValue: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CosmicMuted,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                color = CosmicWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = maskedValue,
                color = CosmicTextSecondary,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Configurar",
                tint = CosmicMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun SettingGeneralRow(
    title: String,
    status: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CosmicMuted,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                color = CosmicWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = status,
                color = CosmicTextSecondary,
                fontSize = 12.sp
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Abrir",
                tint = CosmicMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
