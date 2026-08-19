package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TerminalCommandEntity
import com.example.domain.model.TerminalShellType
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalSurface
import kotlinx.coroutines.launch

/**
 * TerminalEmulator: High-performance CLI component with scrollable LazyColumn,
 * color-coded log parsing, interactive shell switching (Zsh, NuShell, Fish),
 * instant autosuggestions, and live syntax highlighting.
 */
@Composable
fun TerminalEmulator(
    history: List<TerminalCommandEntity>,
    activeShell: TerminalShellType,
    onShellChange: (TerminalShellType) -> Unit,
    onExecuteCommand: (String) -> Unit,
    onClearTerminal: () -> Unit,
    onNarrateOutput: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputCommand by remember { mutableStateOf("") }
    var historyIndex by remember { mutableIntStateOf(-1) }

    // Auto-scroll on new command execution
    LaunchedEffect(history.size) {
        if (history.isNotEmpty()) {
            listState.animateScrollToItem(history.size - 1)
        }
    }

    // Shell-specific knowledge base for instant autosuggestions
    val commandDatabase = remember(activeShell) {
        when (activeShell) {
            TerminalShellType.ZSH -> listOf(
                "nx init",
                "nx doctor",
                "nx autopilot",
                "nx autopilot \"Deploy Production Release\"",
                "nx workflow full",
                "nx debate \"Rust vs C++ NDK\"",
                "nx scan --strict",
                "nx fix",
                "nx performance",
                "nx benchmark",
                "nx native-lib ndk_math_engine",
                "nx search \"Android NDK SIMD\"",
                "nx research \"OWASP MASVS v2\"",
                "nx voice \"System Status Optimal\"",
                "nx sbom",
                "nx mcp",
                "nx secrets",
                "nx skills",
                "nx deploy PRODUCTION",
                "nx shell nu",
                "nx shell fish",
                "git status",
                "git log -n 5 --oneline",
                "ls -la",
                "pwd",
                "whoami",
                "nx clear"
            )
            TerminalShellType.NUSHELL -> listOf(
                "open metadata.json | get majorCapabilities",
                "open config.json | get devsecops",
                "open sbom.json | get components | where license == 'Apache-2.0'",
                "sys | get cpu, mem, disks",
                "ls | sort-by size -r | first 5",
                "http get https://api.status.cloud/health | from json",
                "open app/build.gradle.kts | lines | find dependencies",
                "nx autopilot",
                "nx scan",
                "nx performance",
                "nx doctor",
                "nx shell zsh",
                "nx shell fish",
                "nx clear"
            )
            TerminalShellType.FISH -> listOf(
                "fish_greeting",
                "fish_config",
                "nx autopilot \"Zero Bug Convergence\"",
                "nx scan --strict --all",
                "nx debate \"Kotlin Coroutines vs RxJava\"",
                "nx performance --benchmark",
                "nx search \"Compose Baseline Profiles\"",
                "nx voice \"Fish interactive terminal online\"",
                "abbr -a",
                "complete -c nx",
                "history | head -n 10",
                "type -a nx",
                "nx shell zsh",
                "nx shell nu",
                "nx clear"
            )
        }
    }

    // Instant autosuggestion: top match
    val topSuggestion by remember(inputCommand, activeShell) {
        derivedStateOf {
            if (inputCommand.isBlank()) null
            else commandDatabase.firstOrNull {
                it.startsWith(inputCommand.trim(), ignoreCase = true) && it != inputCommand.trim()
            }
        }
    }

    // Filtered list of suggestions for quick chips
    val matchingSuggestions by remember(inputCommand, activeShell) {
        derivedStateOf {
            if (inputCommand.isBlank()) {
                commandDatabase.take(8)
            } else {
                commandDatabase.filter {
                    it.contains(inputCommand.trim(), ignoreCase = true)
                }.take(6)
            }
        }
    }

    val isScrolledUp by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            history.isNotEmpty() && lastVisible < history.size - 2
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TerminalBackground)
    ) {
        // --- 1. Shell Environment Selector Header ---
        ShellEnvironmentSelector(
            activeShell = activeShell,
            onShellSelect = onShellChange,
            onClearTerminal = onClearTerminal,
            onCopyAll = {
                val fullText = buildString {
                    appendLine("=== NEXUS DEV ORCHESTRATOR TERMINAL LOG ===")
                    appendLine("Shell: ${activeShell.shellName.uppercase()}")
                    history.forEach { item ->
                        appendLine("$ ${item.command}  [${item.agentTag}]")
                        appendLine(item.output)
                        appendLine("---")
                    }
                }
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Terminal Logs", fullText))
                Toast.makeText(context, "Terminal output copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // --- 2. Main Terminal Window with Scrollable LazyColumn ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF04070D))
                .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("terminal_lazy_column"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Shell Welcome Banner
                item {
                    ShellWelcomeBanner(activeShell = activeShell)
                }

                // Log History Items
                items(history, key = { it.id }) { item ->
                    ColorCodedTerminalEntry(
                        item = item,
                        activeShell = activeShell,
                        onNarrate = { onNarrateOutput(it) },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Command Output", item.output))
                            Toast.makeText(context, "Output copied", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // Floating Scroll-to-Bottom Button
            if (isScrolledUp) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            if (history.isNotEmpty()) {
                                listState.animateScrollToItem(history.size - 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NeonCyan.copy(alpha = 0.9f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Scroll to bottom",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- 3. Instant Autosuggestion Chips Row ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "SUGGEST",
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            matchingSuggestions.forEach { suggestion ->
                SuggestionChip(
                    onClick = {
                        inputCommand = suggestion
                        onExecuteCommand(suggestion)
                        inputCommand = ""
                        historyIndex = -1
                    },
                    label = {
                        Text(
                            text = suggestion,
                            color = Color(0xFFE2E8F0),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = TerminalSurface,
                        labelColor = Color(0xFFE2E8F0)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                    shape = RoundedCornerShape(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- 4. Interactive CLI Input with Syntax Highlighting & Ghost Autosuggestion ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // History UP/DOWN cycling buttons
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(TerminalSurface)
                    .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
            ) {
                IconButton(
                    onClick = {
                        if (history.isNotEmpty()) {
                            if (historyIndex == -1) {
                                historyIndex = history.size - 1
                            } else if (historyIndex > 0) {
                                historyIndex--
                            }
                            inputCommand = history[historyIndex].command
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Previous Command",
                        tint = if (history.isNotEmpty()) NeonCyan else Color.DarkGray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (history.isNotEmpty() && historyIndex != -1) {
                            if (historyIndex < history.size - 1) {
                                historyIndex++
                                inputCommand = history[historyIndex].command
                            } else {
                                historyIndex = -1
                                inputCommand = ""
                            }
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Next Command",
                        tint = if (historyIndex != -1) NeonCyan else Color.DarkGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Input Text Box with Live Syntax Highlighting & Ghost Text
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                OutlinedTextField(
                    value = inputCommand,
                    onValueChange = {
                        inputCommand = it
                        historyIndex = -1
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("terminal_input_field"),
                    placeholder = {
                        Text(
                            text = when (activeShell) {
                                TerminalShellType.ZSH -> "nx autopilot / nx debate / nx scan..."
                                TerminalShellType.NUSHELL -> "open metadata.json | get ... / sys"
                                TerminalShellType.FISH -> "nx autopilot / fish_config..."
                            },
                            color = Color(0xFF64748B),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(activeShell.tagColorHex).copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = activeShell.promptPrefix,
                                color = Color(activeShell.tagColorHex),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    },
                    visualTransformation = CliSyntaxHighlightingTransformation(activeShell),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (inputCommand.isNotBlank()) {
                            onExecuteCommand(inputCommand)
                            inputCommand = ""
                            historyIndex = -1
                        }
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(activeShell.tagColorHex),
                        unfocusedBorderColor = TerminalBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = TerminalSurface,
                        unfocusedContainerColor = TerminalSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                // Inline Ghost Text for Top Autosuggestion
                if (topSuggestion != null && inputCommand.isNotBlank() && topSuggestion!!.startsWith(inputCommand, ignoreCase = true)) {
                    val remaining = topSuggestion!!.substring(inputCommand.length)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 78.dp + (inputCommand.length * 7).dp, end = 12.dp)
                            .clickable {
                                inputCommand = topSuggestion!!
                            }
                    ) {
                        Text(
                            text = remaining,
                            color = Color(0xFF475569),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1
                        )
                    }
                }
            }

            // Send / Execute Button
            IconButton(
                onClick = {
                    if (inputCommand.isNotBlank()) {
                        onExecuteCommand(inputCommand)
                        inputCommand = ""
                        historyIndex = -1
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(activeShell.tagColorHex))
                    .testTag("terminal_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Execute Command",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Shell selector bar allowing quick switching between Zsh, NuShell, and Fish Shell.
 */
@Composable
fun ShellEnvironmentSelector(
    activeShell: TerminalShellType,
    onShellSelect: (TerminalShellType) -> Unit,
    onClearTerminal: () -> Unit,
    onCopyAll: () -> Unit
) {
    val shells = TerminalShellType.entries.toTypedArray()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    shells.forEach { shell ->
                        val isSelected = activeShell == shell
                        val shellColor = Color(shell.tagColorHex)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) shellColor.copy(alpha = 0.2f) else Color(0xFF0B111E))
                                .border(
                                    1.dp,
                                    if (isSelected) shellColor else Color(0xFF1E293B),
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { onShellSelect(shell) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) shellColor else Color(0xFF475569))
                                )
                                Text(
                                    text = shell.promptBadge,
                                    color = if (isSelected) shellColor else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onCopyAll,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy all output",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onClearTerminal,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ClearAll,
                            contentDescription = "Clear terminal",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = activeShell.description,
                color = Color(0xFF64748B),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

/**
 * Shell welcome banner with ASCII art and mode info.
 */
@Composable
fun ShellWelcomeBanner(activeShell: TerminalShellType) {
    val bannerText = when (activeShell) {
        TerminalShellType.ZSH -> """
            ╔═════════════════════════════════════════════════════════════════════════╗
            ║  NEXUS DEV ORCHESTRATOR • ZSH 5.9 (POWERLEVEL10K PROMPT ACTIVE)          ║
            ║  Default DevOps shell • Type 'nx help' or tap suggestions below         ║
            ╚═════════════════════════════════════════════════════════════════════════╝
        """.trimIndent()
        TerminalShellType.NUSHELL -> """
            ╔═════════════════════════════════════════════════════════════════════════╗
            ║  NUSHELL v0.96.1 • STRUCTURED DATA EXPLORATION & CLOUD AUTOMATION       ║
            ║  Pipelines on tables/JSON • e.g. open metadata.json | get majorCaps      ║
            ╚═════════════════════════════════════════════════════════════════════════╝
        """.trimIndent()
        TerminalShellType.FISH -> """
            ╔═════════════════════════════════════════════════════════════════════════╗
            ║  FISH SHELL v3.7.0 • INTERACTIVE AUTOSUGGESTIONS & SYNTAX HIGHLIGHTING  ║
            ║  Fast, productive terminal • Type command initials to see ghost text   ║
            ╚═════════════════════════════════════════════════════════════════════════╝
        """.trimIndent()
    }

    Text(
        text = bannerText,
        color = Color(activeShell.tagColorHex),
        fontSize = 9.sp,
        fontFamily = FontFamily.Monospace,
        lineHeight = 13.sp
    )
}

/**
 * Color-coded single command log entry with rich ANSI/severity parsing,
 * execution timing, agent badge, copy, and TTS voice reader.
 */
@Composable
fun ColorCodedTerminalEntry(
    item: TerminalCommandEntity,
    activeShell: TerminalShellType,
    onNarrate: (String) -> Unit,
    onCopy: () -> Unit
) {
    val tagColor = when (item.agentTag) {
        "PLANNER" -> NeonCyan
        "ARCHITECT", "CLAUDE_CODE" -> NeonPurple
        "CODEX", "CODEX_NATIVE" -> NeonCyan
        "DEVSECOPS", "DEBUG_AGENT" -> NeonRed
        "PERFORMANCE", "AUTOPILOT" -> NeonEmerald
        "JUDGE", "EXA_RESEARCHER" -> NeonAmber
        "RELEASE_AGENT", "CI_CD" -> Color(0xFF6366F1)
        "NUSHELL" -> NeonEmerald
        "FISH" -> NeonAmber
        "ZSH" -> NeonCyan
        else -> Color(activeShell.tagColorHex)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Command execution header: prompt + command string + agent badge + timing
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = "${activeShell.promptPrefix} ",
                    color = Color(activeShell.tagColorHex),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = item.command,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.executionTimeMs > 0) {
                    Text(
                        text = "${item.executionTimeMs}ms",
                        color = Color(0xFF64748B),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(tagColor.copy(alpha = 0.2f))
                        .border(1.dp, tagColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.agentTag,
                        color = tagColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Output Box with Color-Coded Log Parsing
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF090D16))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                val formattedLog = remember(item.output, item.isError) {
                    parseColorCodedLogs(item.output, item.isError)
                }

                Text(
                    text = formattedLog,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 14.sp,
                    modifier = Modifier.weight(1f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { onNarrate(item.output) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read Output",
                            tint = NeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Output",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Intelligent log parser that builds AnnotatedString applying color codes to:
 * - [PASS], [OK], [SUCCESS], ✓ -> NeonEmerald
 * - [FAIL], [ERROR], [CRITICAL], ✗ -> NeonRed
 * - [WARN], [WARNING] -> NeonAmber
 * - [INFO], [DEBUG] -> NeonCyan
 * - Table lines and ASCII borders -> Slate / Cyan
 */
fun parseColorCodedLogs(rawText: String, isError: Boolean): AnnotatedString {
    if (isError) {
        return AnnotatedString(rawText, spanStyle = SpanStyle(color = NeonRed))
    }

    return buildAnnotatedString {
        val lines = rawText.lines()
        lines.forEachIndexed { index, line ->
            when {
                // Table Borders or ASCII Box
                line.startsWith("╭") || line.startsWith("├") || line.startsWith("╰") ||
                line.startsWith("╔") || line.startsWith("╠") || line.startsWith("╚") -> {
                    withStyle(SpanStyle(color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)) {
                        append(line)
                    }
                }
                // Table Headers
                line.contains("│ # │") || line.contains("│") && line.contains("name") -> {
                    withStyle(SpanStyle(color = Color(0xFFE2E8F0), fontWeight = FontWeight.Bold)) {
                        append(line)
                    }
                }
                // Table Rows
                line.startsWith("│") -> {
                    val parts = line.split("│")
                    parts.forEachIndexed { pIdx, part ->
                        if (pIdx > 0) append("│")
                        when {
                            part.contains("PASS") || part.contains("OPTIMAL") || part.contains("HEALTHY") || part.contains("READY") -> {
                                withStyle(SpanStyle(color = NeonEmerald)) { append(part) }
                            }
                            part.contains("FAIL") || part.contains("ERROR") -> {
                                withStyle(SpanStyle(color = NeonRed)) { append(part) }
                            }
                            part.contains("ACTIVE") || part.contains("STANDBY") -> {
                                withStyle(SpanStyle(color = NeonCyan)) { append(part) }
                            }
                            else -> {
                                withStyle(SpanStyle(color = Color(0xFFCBD5E1))) { append(part) }
                            }
                        }
                    }
                }
                // Success / Pass Indicators
                line.contains("[PASS]") || line.contains("✓") || line.contains("[SUCCESS]") || line.contains("[OPTIMAL]") -> {
                    withStyle(SpanStyle(color = NeonEmerald)) {
                        append(line)
                    }
                }
                // Error / Fail Indicators
                line.contains("[FAIL]") || line.contains("[ERROR]") || line.contains("[CRITICAL]") || line.contains("✗") -> {
                    withStyle(SpanStyle(color = NeonRed, fontWeight = FontWeight.Bold)) {
                        append(line)
                    }
                }
                // Warning Indicators
                line.contains("[WARN]") || line.contains("PATCH AVAILABLE") -> {
                    withStyle(SpanStyle(color = NeonAmber)) {
                        append(line)
                    }
                }
                // Headers / Tags
                line.startsWith("[") && line.contains("]") -> {
                    withStyle(SpanStyle(color = NeonCyan, fontWeight = FontWeight.Bold)) {
                        append(line)
                    }
                }
                // Tree Lines (├──, └──)
                line.contains("├──") || line.contains("└──") -> {
                    withStyle(SpanStyle(color = Color(0xFF818CF8))) {
                        append(line)
                    }
                }
                else -> {
                    withStyle(SpanStyle(color = Color(0xFF94A3B8))) {
                        append(line)
                    }
                }
            }
            if (index < lines.size - 1) append("\n")
        }
    }
}

/**
 * Custom VisualTransformation providing real-time syntax highlighting for CLI commands.
 */
class CliSyntaxHighlightingTransformation(
    private val activeShell: TerminalShellType
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val annotated = buildAnnotatedString {
            val tokens = raw.split(" ")
            var currentIndex = 0

            tokens.forEachIndexed { index, token ->
                val start = raw.indexOf(token, currentIndex).takeIf { it != -1 } ?: currentIndex
                currentIndex = start + token.length

                val style = when {
                    // Command Binaries
                    index == 0 && (token in listOf("nx", "git", "zsh", "nu", "nushell", "fish", "open", "ls", "sys", "curl", "pwd", "whoami", "chsh", "cd")) -> {
                        SpanStyle(color = Color(activeShell.tagColorHex), fontWeight = FontWeight.Bold)
                    }
                    // Subcommands / Actions
                    token in listOf("init", "doctor", "agents", "autopilot", "workflow", "debate", "scan", "security", "fix", "performance", "benchmark", "native-lib", "search", "research", "voice", "sbom", "memory", "mcp", "secrets", "skills", "deploy", "clear", "get", "where", "sort-by", "from", "to", "lines", "find", "first") -> {
                        SpanStyle(color = NeonPurple, fontWeight = FontWeight.SemiBold)
                    }
                    // Flags & Options
                    token.startsWith("-") -> {
                        SpanStyle(color = NeonAmber, fontWeight = FontWeight.Normal)
                    }
                    // Strings & Quotes
                    token.startsWith("\"") || token.startsWith("'") || token.endsWith("\"") || token.endsWith("'") -> {
                        SpanStyle(color = Color(0xFFFDE047), fontWeight = FontWeight.Normal)
                    }
                    // Pipes & Operators
                    token in listOf("|", ">", ">>", "&&", "||", ";") -> {
                        SpanStyle(color = Color(0xFFEC4899), fontWeight = FontWeight.Bold)
                    }
                    else -> {
                        SpanStyle(color = Color.White)
                    }
                }

                withStyle(style) {
                    append(token)
                }

                if (index < tokens.size - 1) {
                    append(" ")
                }
            }
        }

        return TransformedText(annotated, OffsetMapping.Identity)
    }
}
