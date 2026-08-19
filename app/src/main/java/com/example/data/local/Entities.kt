package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terminal_history")
data class TerminalCommandEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val command: String,
    val output: String,
    val isError: Boolean = false,
    val executionTimeMs: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val agentTag: String = "USER"
)

@Entity(tableName = "agent_workflows")
data class AgentWorkflowEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val goal: String,
    val status: String, // IDLE, RUNNING, COMPLETED, FAILED
    val primaryAgent: String, // CLAUDE_CODE, CODEX, SWARM, AUTOPILOT
    val secondaryAgent: String, // CODEX, CLAUDE_CODE, DEVSECOPS
    val generatedCode: String = "",
    val executionSummary: String = "",
    val thinkingProcess: String = "",
    val tokenUsage: Int = 1420,
    val estimatedCostUsd: Double = 0.0035,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "agent_tasks")
data class AgentTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workflowId: Long,
    val agentName: String,
    val stepTitle: String,
    val prompt: String,
    val response: String,
    val status: String, // PENDING, PROCESSING, DONE, FAILED
    val stepOrder: Int,
    val toolsUsed: String = "Exa, CodeQL, NDK Compiler",
    val latencyMs: Long = 340,
    val tokensUsed: Int = 420,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "security_audits")
data class SecurityAuditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectName: String,
    val targetCode: String,
    val findingsJson: String, // Serialized list of findings
    val severityScore: String, // CRITICAL, HIGH, MEDIUM, LOW, CLEAN
    val totalVulnerabilities: Int,
    val totalBugs: Int,
    val optimizationSuggestion: String,
    val patchedCode: String,
    val masvsCategory: String = "MASVS-STORAGE / MASVS-CRYPTO",
    val sbomJson: String = "",
    val autoFixApproved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "pipeline_runs")
data class PipelineRunEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pipelineName: String,
    val triggerSource: String, // MANUAL, AGENT_AUTO, CLI, AUTOPILOT
    val status: String, // QUEUED, RUNNING, SUCCESS, FAILED
    val stagesJson: String, // Stages with duration and status
    val logs: String,
    val durationSeconds: Int,
    val artifactName: String,
    val testCoveragePercent: Int = 94,
    val apkSizeBytes: Long = 8_420_000,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "workflow_skills")
data class SkillEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // ANDROID, RUST, JNI, DEVSECOPS, RESEARCH, TESTING, PERFORMANCE, CI_CD
    val description: String,
    val promptTemplate: String,
    val pathFolder: String = ".skills/",
    val isSystemSkill: Boolean = true,
    val enabled: Boolean = true
)

@Entity(tableName = "project_brain")
data class ProjectBrainEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adrId: String, // e.g. "ADR-017"
    val title: String,
    val contextAndProblem: String,
    val decision: String,
    val rationale: String,
    val evidence: String,
    val status: String = "ACCEPTED", // PROPOSED, ACCEPTED, SUPERSEDED
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "agent_debates")
data class AgentDebateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val architectOpinion: String,
    val securityOpinion: String,
    val performanceOpinion: String,
    val judgeVerdict: String,
    val confidenceScore: Int = 92,
    val chosenOption: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mcp_tools")
data class McpToolEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val toolName: String,
    val description: String,
    val readAllowed: Boolean = true,
    val writeAllowed: Boolean = true,
    val executeAllowed: Boolean = true,
    val networkAllowed: Boolean = true,
    val deployAllowed: Boolean = false,
    val secretAccessAllowed: Boolean = false,
    val enabled: Boolean = true
)
