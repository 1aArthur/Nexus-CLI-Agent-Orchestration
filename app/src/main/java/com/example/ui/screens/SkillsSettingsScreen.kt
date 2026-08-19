package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SkillEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalSurface

@Composable
fun SkillsSettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val skills by viewModel.skills.collectAsState()
    val geminiKey by viewModel.customGeminiKey.collectAsState()
    val exaKey by viewModel.customExaKey.collectAsState()
    val grokKey by viewModel.customGrokKey.collectAsState()

    var editGeminiKey by remember(geminiKey) { mutableStateOf(geminiKey) }
    var editExaKey by remember(exaKey) { mutableStateOf(exaKey) }
    var editGrokKey by remember(grokKey) { mutableStateOf(grokKey) }

    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // API Key Configuration Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "API KEYS & SECRETS PANEL",
                            color = NeonCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Icon(imageVector = Icons.Default.Key, contentDescription = "Keys", tint = NeonCyan)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Gemini Key
                    OutlinedTextField(
                        value = editGeminiKey,
                        onValueChange = { editGeminiKey = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gemini_key_input"),
                        label = { Text("Gemini API Key (Thinking & Pro Models)", color = NeonPurple, fontSize = 11.sp) },
                        placeholder = { Text("Enter custom Gemini API key...", fontSize = 11.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Exa Key
                    OutlinedTextField(
                        value = editExaKey,
                        onValueChange = { editExaKey = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exa_key_input"),
                        label = { Text("Exa Search Engine API Key", color = NeonAmber, fontSize = 11.sp) },
                        placeholder = { Text("Enter custom Exa Search key...", fontSize = 11.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonAmber,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Grok Key
                    OutlinedTextField(
                        value = editGrokKey,
                        onValueChange = { editGrokKey = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("grok_key_input"),
                        label = { Text("Grok TTS Voice API Key", color = NeonEmerald, fontSize = 11.sp) },
                        placeholder = { Text("Enter custom Grok TTS key...", fontSize = 11.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonEmerald,
                            unfocusedBorderColor = TerminalBorder,
                            focusedContainerColor = Color(0xFF070B12),
                            unfocusedContainerColor = Color(0xFF070B12)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.updateApiKeys(editGeminiKey, editExaKey, editGrokKey)
                            Toast.makeText(context, "API Keys updated for this session!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("save_api_keys_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAVE KEYS IN ENVIRONMENT", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Skills Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AGENT WORKFLOW SKILLS (${skills.size})",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Icon(imageVector = Icons.Default.Tune, contentDescription = "Skills", tint = NeonPurple)
            }
        }

        items(skills, key = { it.id }) { skill ->
            SkillItemCard(skill = skill, onToggle = { viewModel.toggleSkill(skill) })
        }
    }
}

@Composable
fun SkillItemCard(
    skill: SkillEntity,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (skill.enabled) NeonPurple.copy(alpha = 0.5f) else TerminalBorder
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = skill.name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Category: ${skill.category}",
                        color = NeonPurple,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Switch(
                    checked = skill.enabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeonCyan,
                        checkedTrackColor = Color(0xFF00363A),
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color(0xFF1E293B)
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = skill.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF070A12))
                    .padding(8.dp)
            ) {
                Text(
                    text = "System Prompt: ${skill.promptTemplate.take(90)}...",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
