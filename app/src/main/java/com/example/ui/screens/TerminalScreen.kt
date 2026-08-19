package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.MainViewModel
import com.example.ui.components.AudioWaveformPlayer
import com.example.ui.components.TelemetryBar
import com.example.ui.components.TerminalEmulator
import com.example.ui.theme.TerminalBackground

@Composable
fun TerminalScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val history by viewModel.terminalHistory.collectAsState()
    val telemetry by viewModel.telemetry.collectAsState()
    val activeShell by viewModel.activeShell.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val currentUtterance by viewModel.currentUtterance.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .padding(12.dp)
    ) {
        TelemetryBar(telemetry = telemetry)

        Spacer(modifier = Modifier.height(8.dp))

        if (isSpeaking) {
            AudioWaveformPlayer(
                isPlaying = isSpeaking,
                currentUtterance = currentUtterance,
                onStop = { viewModel.stopSpeaking() }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Dedicated TerminalEmulator Component with Zsh, NuShell, Fish, scrollable LazyColumn, autosuggestions & syntax highlighting
        TerminalEmulator(
            history = history,
            activeShell = activeShell,
            onShellChange = { viewModel.setActiveShell(it) },
            onExecuteCommand = { viewModel.executeTerminalCommand(it) },
            onClearTerminal = { viewModel.clearTerminal() },
            onNarrateOutput = { viewModel.speakText(it) },
            modifier = Modifier.weight(1f)
        )
    }
}
