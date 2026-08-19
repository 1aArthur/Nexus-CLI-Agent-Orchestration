package com.example.domain.model

enum class AgentType(
    val displayName: String,
    val roleDescription: String,
    val defaultModel: String,
    val badgeColorHex: Long
) {
    PLANNER(
        "Planner Agent",
        "Decomposes complex requests into task execution DAG",
        "gemini-3.1-pro-preview",
        0xFF38BDF8
    ),
    ARCHITECT(
        "Architect Agent",
        "System architecture design, module graph & ADR creation",
        "gemini-3.1-pro-preview",
        0xFF818CF8
    ),
    CLAUDE_CODE(
        "Claude Code Agent",
        "Senior Code Architect & High-Level Refactoring Engine",
        "gemini-3.1-pro-preview",
        0xFF8B5CF6
    ),
    CODEX(
        "Codex Native Agent",
        "High-Performance Logic Synthesizer, JNI & NDK Specialist",
        "gemini-3.1-pro-preview",
        0xFF00F0FF
    ),
    EXA_RESEARCHER(
        "Exa Neural Researcher",
        "Deep Web Grounding, GitHub Issues & Tech Intelligence",
        "gemini-3.5-flash",
        0xFFF59E0B
    ),
    DEBUG_AGENT(
        "Debug & Log Agent",
        "Log parser, stacktrace minimization & hypothesis testing",
        "gemini-3.1-pro-preview",
        0xFFEC4899
    ),
    DEVSECOPS_SENTINEL(
        "DevSecOps Sentinel",
        "SAST/DAST Vulnerability Scanner, OWASP & CWE Auditor",
        "gemini-3.1-pro-preview",
        0xFFEF4444
    ),
    PERFORMANCE_AGENT(
        "Performance Agent",
        "Memory allocation, GC pauses, DEX count & SIMD speedup",
        "gemini-3.5-flash",
        0xFF10B981
    ),
    TEST_AGENT(
        "Test Automator",
        "Unit, Robolectric JVM, regression & property-based tests",
        "gemini-3.5-flash",
        0xFF06B6D4
    ),
    REVIEWER_AGENT(
        "Reviewer Agent",
        "Diff inspector, regression blocker & code standards",
        "gemini-3.1-pro-preview",
        0xFFA855F7
    ),
    RELEASE_AGENT(
        "Release Agent",
        "Changelog generator, versioning & CI/CD deployment",
        "gemini-3.5-flash",
        0xFF6366F1
    ),
    NATIVE_NDK_ENGINEER(
        "Native NDK Engineer",
        "C++20/Rust/JNI Bindings & CMake Build System Architect",
        "gemini-3.5-flash",
        0xFF10B981
    );

    val title: String get() = displayName
}

enum class AgentState {
    IDLE,
    PLANNING,
    RESEARCHING,
    ARCHITECTING,
    WRITING_CODE,
    AUDITING,
    COMPILING,
    TESTING,
    PROFILING,
    REVIEWING,
    DEPLOYING,
    COMPLETED,
    ERROR
}

data class AgentLiveStatus(
    val agentType: AgentType,
    val state: AgentState = AgentState.IDLE,
    val currentTask: String = "Standby",
    val progress: Float = 0f,
    val thoughts: String = "",
    val activeTokens: Int = 0,
    val latencyMs: Long = 42
)

enum class VulnerabilitySeverity(val label: String, val colorHex: Long) {
    CRITICAL("CRITICAL", 0xFFDC2626),
    HIGH("HIGH", 0xFFEA580C),
    MEDIUM("MEDIUM", 0xFFF59E0B),
    LOW("LOW", 0xFF3B82F6),
    CLEAN("CLEAN", 0xFF10B981)
}

data class SecurityFinding(
    val id: String,
    val title: String,
    val severity: VulnerabilitySeverity,
    val cwe: String,
    val masvsRef: String = "",
    val masvsCategory: String = "",
    val component: String = "",
    val line: Int = 0,
    val lineNumber: Int = line,
    val filePath: String = component,
    val description: String = "",
    val recommendation: String = "",
    val originalSnippet: String = "",
    val vulnerableCodeSnippet: String = originalSnippet,
    val patchSnippet: String = "",
    val remediationPatch: String = patchSnippet,
    val confidence: Float = 0.95f,
    val autoFixAvailable: Boolean = true
)

data class SbomPackage(
    val name: String,
    val version: String,
    val purl: String,
    val license: String,
    val sha256Hash: String,
    val vulnerabilitiesCount: Int
)

data class SbomComponent(
    val name: String,
    val version: String,
    val purl: String,
    val license: String,
    val hashSha256: String,
    val vulnerabilitiesCount: Int
)

data class AutopilotPhaseStatus(
    val phaseNumber: Int,
    val phaseName: String,
    val assignedAgent: AgentType,
    val status: PhaseExecutionStatus,
    val details: String = "",
    val durationMs: Long = 0
)

data class AutopilotProgress(
    val stepIndex: Int = 0,
    val totalSteps: Int = 12,
    val currentStep: String = "Standby",
    val active: Boolean = false,
    val buildPass: Boolean = true,
    val testsPass: Boolean = true,
    val securityAcceptable: Boolean = true,
    val lintPass: Boolean = true,
    val criticalBugsCount: Int = 0,
    val regressionsCount: Int = 0,
    val logs: List<String> = emptyList()
)

enum class PhaseExecutionStatus {
    PENDING,
    IN_PROGRESS,
    PASSED,
    FAILED,
    SKIPPED
}

data class NativeModuleSpec(
    val moduleName: String,
    val language: String, // "C++20" or "Rust"
    val packageName: String = "com.example.nativemodule",
    val targetArch: List<String> = listOf("arm64-v8a", "armeabi-v7a", "x86_64"),
    val jniMethods: List<String> = emptyList(),
    val cppSource: String = "",
    val headerSource: String = "",
    val cmakeListsContent: String = "",
    val kotlinWrapperCode: String = "",
    val headerContent: String = headerSource,
    val implementationContent: String = cppSource,
    val cmakeContent: String = cmakeListsContent,
    val jniKotlinWrapper: String = kotlinWrapperCode,
    val isCompiled: Boolean = false
)

data class ProjectBrainNode(
    val id: String,
    val title: String,
    val category: String, // "ADR", "RFC", "CONVERGENCE", "POLICY"
    val status: String,   // "ACCEPTED", "PROPOSED", "SUPERSEDED"
    val content: String,
    val lastUpdated: String
)

data class AgentDebateRound(
    val roundNumber: Int,
    val claudeArgument: String,
    val codexArgument: String,
    val evaluatorVerdict: String,
    val convergenceScore: Float
)

data class PerformanceMetricsProfile(
    val cpuUtilizationPercent: Float = 14.2f,
    val memoryUsageMb: Float = 88.5f,
    val apkSizeMb: Float = 18.4f,
    val dexMethodCount: Int = 34210,
    val coldStartTimeMs: Int = 310,
    val warmStartTimeMs: Int = 85,
    val gcPauseAverageMs: Float = 2.1f,
    val jniBridgeLatencyMicroseconds: Float = 0.45f,
    val nativeSimdSpeedup: Float = 4.8f,
    val recommendations: List<String> = listOf(
        "Enable R8 full mode with shrinkResources to shave ~2.3MB from release APK",
        "Batch native JNI calls in NdkMathAccelerator to reduce JNI boundary transitions",
        "Use Baseline Profiles for Compose startup optimization (<180ms cold start)"
    )
)

data class PipelineStageInfo(
    val name: String,
    val status: String, // "PENDING", "RUNNING", "SUCCESS", "FAILED"
    val durationSeconds: Int,
    val logSummary: String = ""
)

data class TelemetrySnapshot(
    val cpuPercent: Float = 12.4f,
    val memoryUsageMb: Float = 94.2f,
    val activeThreads: Int = 8,
    val latencyMs: Long = 48,
    val totalRequests: Int = 24,
    val totalTokens: Int = 15200,
    val sessionCostUsd: Float = 0.0024f
)

enum class TerminalShellType(
    val shellName: String,
    val promptPrefix: String,
    val promptBadge: String,
    val description: String,
    val tagColorHex: Long
) {
    ZSH(
        shellName = "zsh",
        promptPrefix = "zsh ❯",
        promptBadge = "ZSH",
        description = "Default shell • Powerlevel10k & DevOps POSIX workflows",
        tagColorHex = 0xFF00F0FF
    ),
    NUSHELL(
        shellName = "nushell",
        promptPrefix = "nu ❯",
        promptBadge = "NUSHELL",
        description = "Data exploration • Cloud automation & config manipulation",
        tagColorHex = 0xFF10B981
    ),
    FISH(
        shellName = "fish",
        promptPrefix = "fish ❯",
        promptBadge = "FISH",
        description = "Interactive shell • Instant autosuggestions & syntax highlighting",
        tagColorHex = 0xFFF59E0B
    )
}

// --- Agent Orchestration & Concurrency Models ---
enum class OrchestrationExecutionState {
    IDLE,
    PLANNING_DAG,
    RUNNING_CONCURRENT,
    BARRIER_SYNC,
    EVALUATING,
    COMPLETED,
    FAILED
}

data class ConcurrentAgentTask(
    val taskId: String,
    val agentType: AgentType,
    val title: String,
    val description: String,
    val state: AgentState = AgentState.IDLE,
    val progress: Float = 0f,
    val thoughtsStream: String = "",
    val outputSnippet: String = "",
    val executionTimeMs: Long = 0,
    val isCompleted: Boolean = false,
    val error: String? = null
)

data class SynchronizedAgentOutput(
    val syncId: String,
    val goal: String,
    val claudeArchitectureOutput: String,
    val codexNativeOutput: String,
    val unifiedCodebaseSnapshot: String,
    val conflictCount: Int = 0,
    val testSuitePassed: Boolean = true,
    val convergenceScore: Float = 0.98f,
    val timestamp: Long = System.currentTimeMillis()
)

data class AgentTimelineEvent(
    val id: String,
    val agentType: AgentType,
    val eventType: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- MASVS Static Analysis Result Models ---
data class MasvsCategoryMetric(
    val categoryCode: String,      // e.g. "MASVS-STORAGE", "MASVS-CRYPTO"
    val categoryName: String,      // e.g. "Storage Security", "Cryptography"
    val findingsCount: Int,
    val maxSeverity: VulnerabilitySeverity,
    val isCompliant: Boolean
)

data class MasvsScanResult(
    val scanId: String,
    val targetName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val linesScanned: Int,
    val complianceScore: Int,      // 0 - 100
    val complianceGrade: String,   // A+, A, B, C, F
    val totalVulnerabilities: Int,
    val criticalCount: Int,
    val highCount: Int,
    val mediumCount: Int,
    val lowCount: Int,
    val findings: List<SecurityFinding>,
    val categories: List<MasvsCategoryMetric>,
    val scanDurationMs: Long,
    val autoFixPatchesCount: Int
)


