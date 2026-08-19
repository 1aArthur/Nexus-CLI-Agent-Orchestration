package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.audio.AudioNarrationManager
import com.example.data.local.AgentDebateEntity
import com.example.data.local.AgentTaskEntity
import com.example.data.local.AgentWorkflowEntity
import com.example.data.local.AppDatabase
import com.example.data.local.McpToolEntity
import com.example.data.local.PipelineRunEntity
import com.example.data.local.ProjectBrainEntity
import com.example.data.local.SecurityAuditEntity
import com.example.data.local.SkillEntity
import com.example.data.local.TerminalCommandEntity
import com.example.data.remote.ExaResult
import com.example.data.repository.AgentOrchestratorRepository
import com.example.data.repository.CicdDeployRepository
import com.example.data.repository.DevSecOpsRepository
import com.example.data.repository.ExaSearchRepository
import com.example.data.repository.TerminalRepository
import com.example.data.security.MasvsStaticAnalyzer
import com.example.domain.model.AgentLiveStatus
import com.example.domain.model.AgentState
import com.example.domain.model.AgentType
import com.example.domain.model.AutopilotProgress
import com.example.domain.model.MasvsScanResult
import com.example.domain.model.NativeModuleSpec
import com.example.domain.model.PerformanceMetricsProfile
import com.example.domain.model.PipelineStageInfo
import com.example.domain.model.SecurityFinding
import com.example.domain.model.TelemetrySnapshot
import com.example.domain.model.TerminalShellType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val audioManager = AudioNarrationManager(application)

    private val orchestratorRepository = AgentOrchestratorRepository(
        database.workflowDao(),
        database.skillDao(),
        database.projectBrainDao(),
        database.agentDebateDao()
    )
    private val devSecOpsRepository = DevSecOpsRepository(database.securityAuditDao())
    private val cicdRepository = CicdDeployRepository(database.pipelineDao())
    private val exaRepository = ExaSearchRepository()
    private val terminalRepository = TerminalRepository(database.terminalDao())

    // --- StateFlows from Room ---
    val terminalHistory: StateFlow<List<TerminalCommandEntity>> = terminalRepository.commandHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workflows: StateFlow<List<AgentWorkflowEntity>> = orchestratorRepository.allWorkflows
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val securityAudits: StateFlow<List<SecurityAuditEntity>> = devSecOpsRepository.allAudits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pipelineRuns: StateFlow<List<PipelineRunEntity>> = cicdRepository.allPipelineRuns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val skills: StateFlow<List<SkillEntity>> = database.skillDao().getAllSkills()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val debates: StateFlow<List<AgentDebateEntity>> = orchestratorRepository.allDebates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projectAdrs: StateFlow<List<ProjectBrainEntity>> = orchestratorRepository.allAdrs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mcpTools: StateFlow<List<McpToolEntity>> = database.mcpToolDao().getAllTools()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Live Agent Status ---
    private val _claudeStatus = MutableStateFlow(
        AgentLiveStatus(
            agentType = AgentType.CLAUDE_CODE,
            state = AgentState.IDLE,
            currentTask = "Ready for architecture orchestration",
            thoughts = "Standing by. Models: gemini-3.1-pro-preview with HIGH Thinking mode.",
            activeTokens = 1042
        )
    )
    val claudeStatus: StateFlow<AgentLiveStatus> = _claudeStatus.asStateFlow()

    private val _codexStatus = MutableStateFlow(
        AgentLiveStatus(
            agentType = AgentType.CODEX,
            state = AgentState.IDLE,
            currentTask = "Ready for C++/Rust/JNI code generation",
            thoughts = "Compiler pipeline ready. Native NDK toolchain armed.",
            activeTokens = 840
        )
    )
    val codexStatus: StateFlow<AgentLiveStatus> = _codexStatus.asStateFlow()

    // --- Autopilot Progress State ---
    private val _autopilotState = MutableStateFlow(AutopilotProgress())
    val autopilotState: StateFlow<AutopilotProgress> = _autopilotState.asStateFlow()

    // --- Performance Lab Profile ---
    private val _performanceProfile = MutableStateFlow(cicdRepository.generatePerformanceProfile())
    val performanceProfile: StateFlow<PerformanceMetricsProfile> = _performanceProfile.asStateFlow()

    // --- Live Telemetry Snapshot ---
    private val _telemetry = MutableStateFlow(TelemetrySnapshot())
    val telemetry: StateFlow<TelemetrySnapshot> = _telemetry.asStateFlow()

    // --- API Keys State ---
    private val _customGeminiKey = MutableStateFlow("")
    val customGeminiKey: StateFlow<String> = _customGeminiKey.asStateFlow()

    private val _customExaKey = MutableStateFlow("")
    val customExaKey: StateFlow<String> = _customExaKey.asStateFlow()

    private val _customGrokKey = MutableStateFlow("")
    val customGrokKey: StateFlow<String> = _customGrokKey.asStateFlow()

    fun setCustomGeminiKey(key: String) {
        _customGeminiKey.value = key
    }

    fun setCustomExaKey(key: String) {
        _customExaKey.value = key
    }

    fun setCustomGrokKey(key: String) {
        _customGrokKey.value = key
    }

    // --- Active CI/CD Live Stages ---
    private val _currentPipelineStages = MutableStateFlow<List<PipelineStageInfo>>(emptyList())
    val currentPipelineStages: StateFlow<List<PipelineStageInfo>> = _currentPipelineStages.asStateFlow()

    // --- Native Spec Generated ---
    private val _latestNativeSpec = MutableStateFlow<NativeModuleSpec?>(null)
    val latestNativeSpec: StateFlow<NativeModuleSpec?> = _latestNativeSpec.asStateFlow()

    // --- Exa Search Results ---
    private val _exaResults = MutableStateFlow<List<ExaResult>>(emptyList())
    val exaResults: StateFlow<List<ExaResult>> = _exaResults.asStateFlow()

    // --- Active Terminal Shell Environment (Zsh, NuShell, Fish) ---
    private val _activeShell = MutableStateFlow(TerminalShellType.ZSH)
    val activeShell: StateFlow<TerminalShellType> = _activeShell.asStateFlow()

    fun setActiveShell(shell: TerminalShellType) {
        _activeShell.value = shell
    }

    private val _isSearchingExa = MutableStateFlow(false)
    val isSearchingExa: StateFlow<Boolean> = _isSearchingExa.asStateFlow()

    // --- Audio Narration State ---
    val isSpeaking: StateFlow<Boolean> = audioManager.isPlaying
    val currentUtterance: StateFlow<String> = audioManager.currentUtterance

    // --- Generation & Operations Loading ---
    private val _isOrchestrating = MutableStateFlow(false)
    val isOrchestrating: StateFlow<Boolean> = _isOrchestrating.asStateFlow()

    private val _isAuditing = MutableStateFlow(false)
    val isAuditing: StateFlow<Boolean> = _isAuditing.asStateFlow()

    // --- MASVS Static Analysis Engine State ---
    private val _masvsScanResult = MutableStateFlow<MasvsScanResult?>(null)
    val masvsScanResult: StateFlow<MasvsScanResult?> = _masvsScanResult.asStateFlow()

    private val _currentAuditedCode = MutableStateFlow(
        """
        class CoreAuthService(private val context: Context) {
            // Hardcoded secret for test environment (MASVS-STORAGE-1)
            val apiKey = "sk-live-99482180491823abce"
            
            fun authenticate(userId: String) {
                // Dynamic SQL injection (MASVS-CODE-1)
                val query = "SELECT * FROM users WHERE id = " + userId
                
                // Insecure file creation mode (MASVS-STORAGE-2)
                val file = context.openFileOutput("session.json", Context.MODE_WORLD_READABLE)
                
                // Cleartext HTTP endpoint (MASVS-NETWORK-1)
                val endpoint = "http://auth.internal.corp/verify"
                
                // Weak cipher (MASVS-CRYPTO-1)
                val cipher = javax.crypto.Cipher.getInstance("DES")
            }
        }
        """.trimIndent()
    )
    val currentAuditedCode: StateFlow<String> = _currentAuditedCode.asStateFlow()

    private val _isDeploying = MutableStateFlow(false)
    val isDeploying: StateFlow<Boolean> = _isDeploying.asStateFlow()

    init {
        startTelemetryHeartbeat()
        runMasvsStaticAnalysis("CoreAuthService.kt", _currentAuditedCode.value)
    }

    private fun startTelemetryHeartbeat() {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(3000)
                val cpu = Random.nextInt(18, 38).toFloat()
                val mem = Random.nextInt(320, 360).toFloat()
                val activeThreads = Random.nextInt(4, 9)
                val latency = Random.nextInt(28, 62).toLong()
                _telemetry.value = TelemetrySnapshot(
                    cpuPercent = cpu,
                    memoryUsageMb = mem,
                    activeThreads = activeThreads,
                    totalRequests = _telemetry.value.totalRequests + 1,
                    latencyMs = latency,
                    totalTokens = _telemetry.value.totalTokens + Random.nextInt(10, 45),
                    sessionCostUsd = _telemetry.value.sessionCostUsd + 0.0001f
                )
            }
        }
    }

    fun updateApiKeys(geminiKey: String, exaKey: String, grokKey: String) {
        _customGeminiKey.value = geminiKey.trim()
        _customExaKey.value = exaKey.trim()
        _customGrokKey.value = grokKey.trim()
    }

    // --- Execute Terminal CLI Commands ---
    fun executeTerminalCommand(input: String) {
        val startTime = System.currentTimeMillis()
        val lower = input.trim().lowercase()

        // Detect shell change commands
        when {
            lower == "zsh" || lower.startsWith("nx shell zsh") || lower == "chsh -s zsh" -> {
                _activeShell.value = TerminalShellType.ZSH
            }
            lower == "nu" || lower == "nushell" || lower.startsWith("nx shell nu") || lower == "chsh -s nu" -> {
                _activeShell.value = TerminalShellType.NUSHELL
            }
            lower == "fish" || lower.startsWith("nx shell fish") || lower == "chsh -s fish" -> {
                _activeShell.value = TerminalShellType.FISH
            }
        }

        viewModelScope.launch {
            val (tag, output) = terminalRepository.parseAndGenerateOutput(input, _activeShell.value)
            val duration = System.currentTimeMillis() - startTime
            terminalRepository.logCommand(
                command = input,
                output = output,
                isError = false,
                executionTimeMs = duration,
                agentTag = tag
            )

            // Trigger corresponding live actions if recognized
            when {
                lower.startsWith("nx autopilot") || lower == "autopilot" -> {
                    val goal = input.removePrefix("nx autopilot").removePrefix("autopilot").trim().ifBlank { "Production Delivery" }
                    runAutopilot(goal)
                }
                lower.startsWith("nx debate") || lower.startsWith("debate") -> {
                    val topic = input.removePrefix("nx debate").removePrefix("debate").trim().ifBlank { "Rust vs C++ NDK SIMD" }
                    runDebate(topic)
                }
                lower.startsWith("nx scan") || lower.startsWith("scan") || lower.startsWith("nx security") -> {
                    runSecurityAudit("CLI Triggered Project", "val apiKey = \"sk-123456789\"\nval query = \"SELECT * FROM users WHERE id = \" + id")
                }
                lower.startsWith("nx performance") -> {
                    refreshPerformanceProfile()
                }
                lower.startsWith("nx deploy") || lower.startsWith("deploy") -> {
                    triggerPipeline("CLI CI/CD Auto Deployment", "PRODUCTION")
                }
                lower.startsWith("nx search") || lower.startsWith("search") -> {
                    val q = input.removePrefix("nx search").removePrefix("search").trim().ifBlank { "Android NDK optimizations" }
                    searchExa(q)
                }
                lower.startsWith("nx voice") || lower.startsWith("voice") -> {
                    val speech = input.removePrefix("nx voice").removePrefix("voice").trim().ifBlank { "Nexus Dev Orchestrator system online." }
                    speakText(speech)
                }
            }
        }
    }

    fun clearTerminal() {
        viewModelScope.launch {
            terminalRepository.clearHistory()
        }
    }

    // --- Multi-Agent Orchestration ---
    fun startOrchestration(goal: String) {
        if (goal.isBlank() || _isOrchestrating.value) return
        _isOrchestrating.value = true

        viewModelScope.launch {
            try {
                orchestratorRepository.runOrchestration(
                    goal = goal,
                    customApiKey = _customGeminiKey.value
                ) { agentType, state, task, thoughts ->
                    when (agentType) {
                        AgentType.CLAUDE_CODE -> {
                            _claudeStatus.value = _claudeStatus.value.copy(
                                state = state,
                                currentTask = task,
                                thoughts = thoughts
                            )
                        }
                        AgentType.CODEX -> {
                            _codexStatus.value = _codexStatus.value.copy(
                                state = state,
                                currentTask = task,
                                thoughts = thoughts
                            )
                        }
                        else -> {}
                    }
                }
            } finally {
                _isOrchestrating.value = false
            }
        }
    }

    // --- Autopilot Loop ---
    fun runAutopilot(goal: String) {
        if (_autopilotState.value.active) return
        viewModelScope.launch {
            orchestratorRepository.runAutopilotLoop(
                goal = goal,
                customApiKey = _customGeminiKey.value,
                onProgressUpdate = { progress ->
                    _autopilotState.value = progress
                },
                onAgentStatusUpdate = { agent, state, task, thoughts ->
                    if (agent == AgentType.CLAUDE_CODE) {
                        _claudeStatus.value = _claudeStatus.value.copy(state = state, currentTask = task, thoughts = thoughts)
                    } else if (agent == AgentType.CODEX) {
                        _codexStatus.value = _codexStatus.value.copy(state = state, currentTask = task, thoughts = thoughts)
                    }
                }
            )
        }
    }

    // --- Agent Debate ---
    fun runDebate(topic: String) {
        viewModelScope.launch {
            orchestratorRepository.runAgentDebate(topic, _customGeminiKey.value)
        }
    }

    // --- DevSecOps Audit & Auto-Fix ---
    fun runSecurityAudit(projectName: String, codeSnippet: String) {
        if (_isAuditing.value) return
        _isAuditing.value = true

        viewModelScope.launch {
            try {
                devSecOpsRepository.runSecurityAudit(
                    projectName = projectName.ifBlank { "MobileApp Module" },
                    codeSnippet = codeSnippet,
                    customApiKey = _customGeminiKey.value
                )
            } finally {
                _isAuditing.value = false
            }
        }
    }

    fun runMasvsStaticAnalysis(filePath: String, codeSnippet: String) {
        _currentAuditedCode.value = codeSnippet
        viewModelScope.launch(Dispatchers.Default) {
            val result = MasvsStaticAnalyzer.analyzeSourceCode(filePath, codeSnippet)
            _masvsScanResult.value = result
        }
    }

    fun applyMasvsPatch(finding: SecurityFinding) {
        val currentCode = _currentAuditedCode.value
        val patchedCode = if (currentCode.contains(finding.originalSnippet)) {
            currentCode.replace(finding.originalSnippet, finding.patchSnippet)
        } else {
            currentCode
        }
        _currentAuditedCode.value = patchedCode
        runMasvsStaticAnalysis(finding.filePath.ifBlank { "CoreAuthService.kt" }, patchedCode)
    }

    fun applyAllMasvsPatches() {
        val scan = _masvsScanResult.value ?: return
        val patchedCode = MasvsStaticAnalyzer.applyVerifiedPatches(_currentAuditedCode.value, scan.findings)
        _currentAuditedCode.value = patchedCode
        runMasvsStaticAnalysis(scan.targetName, patchedCode)
    }

    fun approveSecurityFix(audit: SecurityAuditEntity) {
        viewModelScope.launch {
            val updated = audit.copy(autoFixApproved = true)
            database.securityAuditDao().updateAudit(updated)
            terminalRepository.logCommand(
                command = "nx fix approve ${audit.projectName}",
                output = "[AUTO-FIX APPROVED & APPLIED] Patches applied cleanly to branch.",
                agentTag = "DEVSECOPS"
            )
        }
    }

    fun getAuditFindings(audit: SecurityAuditEntity) =
        devSecOpsRepository.deserializeFindings(audit.findingsJson)

    // --- CI/CD Pipeline & Native NDK ---
    fun triggerPipeline(pipelineName: String, targetEnvironment: String = "PRODUCTION") {
        if (_isDeploying.value) return
        _isDeploying.value = true

        viewModelScope.launch {
            try {
                cicdRepository.executePipeline(
                    pipelineName = pipelineName.ifBlank { "Nexus Continuous Delivery" },
                    targetEnvironment = targetEnvironment
                ) { stages ->
                    _currentPipelineStages.value = stages
                }
            } finally {
                _isDeploying.value = false
            }
        }
    }

    fun generateNativeModule(moduleName: String, methodName: String, language: String = "C++20") {
        val spec = cicdRepository.generateNativeModule(moduleName, methodName, language)
        _latestNativeSpec.value = spec
    }

    fun refreshPerformanceProfile() {
        _performanceProfile.value = cicdRepository.generatePerformanceProfile()
    }

    // --- Exa Search ---
    fun searchExa(query: String) {
        if (query.isBlank() || _isSearchingExa.value) return
        _isSearchingExa.value = true

        viewModelScope.launch {
            try {
                val results = exaRepository.searchExa(
                    query = query,
                    customApiKey = _customExaKey.value
                )
                _exaResults.value = results
            } finally {
                _isSearchingExa.value = false
            }
        }
    }

    // --- Grok / Voice Synthesis ---
    fun speakText(text: String) {
        if (text.isBlank()) return
        audioManager.speak(text)
    }

    fun stopSpeaking() {
        audioManager.stop()
    }

    // --- Skills & MCP Management ---
    fun toggleSkill(skill: SkillEntity) {
        viewModelScope.launch {
            database.skillDao().updateSkill(skill.copy(enabled = !skill.enabled))
        }
    }

    fun toggleMcpTool(tool: McpToolEntity) {
        viewModelScope.launch {
            database.mcpToolDao().updateTool(tool.copy(enabled = !tool.enabled))
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.shutdown()
    }
}
