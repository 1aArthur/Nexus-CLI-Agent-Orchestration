package com.example.data.repository

import com.example.data.local.TerminalCommandEntity
import com.example.data.local.TerminalDao
import com.example.domain.model.TerminalShellType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TerminalRepository(
    private val terminalDao: TerminalDao
) {
    val commandHistory: Flow<List<TerminalCommandEntity>> = terminalDao.getAllCommands()

    suspend fun logCommand(
        command: String,
        output: String,
        isError: Boolean = false,
        executionTimeMs: Long = 0,
        agentTag: String = "USER"
    ): Long = withContext(Dispatchers.IO) {
        val entity = TerminalCommandEntity(
            command = command,
            output = output,
            isError = isError,
            executionTimeMs = executionTimeMs,
            agentTag = agentTag
        )
        terminalDao.insertCommand(entity)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        terminalDao.clearHistory()
    }

    fun parseAndGenerateOutput(input: String, activeShell: TerminalShellType = TerminalShellType.ZSH): Pair<String, String> {
        val trimmed = input.trim()
        val parts = trimmed.split("\\s+".toRegex())
        val cmd = parts.firstOrNull()?.lowercase() ?: ""
        val subCmd = parts.getOrNull(1)?.lowercase() ?: ""
        val args = if (parts.size > 2) parts.drop(2).joinToString(" ") else (if (parts.size > 1 && cmd != "nx") parts.drop(1).joinToString(" ") else "")

        // Support direct shell invocation or subshell prefix: "nu -c '...'", "zsh -c '...'", "fish -c '...'"
        if (cmd == "zsh" || cmd == "nu" || cmd == "nushell" || cmd == "fish" || cmd == "chsh") {
            return handleShellSpecificCommand(cmd, parts, trimmed)
        }

        // Support NuShell style pipeline commands (open, sys, ls, http, etc.) when running in NuShell or piped
        if (activeShell == TerminalShellType.NUSHELL || trimmed.contains("|") || cmd in listOf("open", "sys", "from", "to", "select", "where", "sort-by")) {
            val nuResult = tryHandleNuShellPipeline(trimmed)
            if (nuResult != null) return nuResult
        }

        // Support Fish Shell specific commands
        if (activeShell == TerminalShellType.FISH || cmd in listOf("fish_greeting", "fish_config", "abbr", "complete", "functions")) {
            val fishResult = tryHandleFishCommand(trimmed, cmd, parts)
            if (fishResult != null) return fishResult
        }

        // Support both "nx <cmd>" and "<cmd>" directly
        val actualCmd = if (cmd == "nx") subCmd else cmd
        val actualArgs = if (cmd == "nx") args else if (parts.size > 1) parts.drop(1).joinToString(" ") else ""

        val (tag, response) = when (actualCmd) {
            "help", "" -> "NEXUS_CORE" to """
                [NEXUS DEV ORCHESTRATOR - CLI COMMAND CENTER]
                Shell Environments: Zsh (Default), NuShell (Data & Cloud), Fish (Interactive)
                
                Core Orchestration Commands:
                  nx init                -> Initialize project context & detect skills
                  nx doctor              -> Verify environment, NDK, Room & Keystore
                  nx agents              -> Display active Agent Fleet (Claude & Codex)
                  nx autopilot [goal]    -> Run full 12-phase automated delivery loop
                  nx workflow <type>     -> Execute workflow (build-app, audit, fix, release, full)
                  nx debate <topic>      -> Launch multi-agent debate (Architect vs Security vs Performance)
                  nx scan / nx security  -> Run OWASP MASVS/MASTG SAST vulnerability scan
                  nx fix                 -> Apply automated security patches with approval
                  nx performance         -> Display Performance Lab (CPU, RAM, GC, APK size)
                  nx benchmark           -> Run SIMD & JNI memory latency benchmarks
                  nx native-lib <name>   -> Generate C++20/Rust CMake NDK module
                  nx search <query>      -> Exa neural search & code grounding
                  nx research <query>    -> Exa Deep Research & architecture synthesis
                  nx voice <text>        -> Grok/xAI TTS voice synthesizer
                  nx sbom                -> Generate CycloneDX / SPDX SBOM JSON
                  nx memory / nx project -> Inspect Project Brain ADRs (e.g. ADR-017)
                  nx mcp                 -> List MCP Gateway tools and permissions
                  nx secrets             -> View masked API secrets vault
                  nx skills              -> List active workflow skills
                  nx deploy [env]        -> Trigger multi-environment CI/CD deployment
                  nx shell <zsh|nu|fish> -> Switch active shell environment
                  nx clear               -> Clear terminal history
                
                NuShell Data & Cloud Commands:
                  open config.json | get devsecops
                  open sbom.json | get components | where license == 'Apache-2.0'
                  sys | get cpu, memory, disks
                  ls | sort-by size -r | first 5
                  http get https://api.status.cloud/health | from json
            """.trimIndent()

            "shell" -> "SHELL_MANAGER" to """
                [SHELL SWITCHED]: ${actualArgs.ifBlank { "zsh" }.uppercase()}
                Switched active terminal environment to ${actualArgs.ifBlank { "zsh" }}.
                Prompt syntax, autosuggestions and completions reloaded.
            """.trimIndent()

            "init" -> "PLANNER" to """
                [NX INIT] Project Context Initialized
                ✓ Target Runtime: Android (Kotlin 2.0 / Compose M3 / NDK CMake 3.22)
                ✓ Local Persistence: SQLite Room Database (Room 2.7)
                ✓ Active Skills Loaded: 8 Skills (.skills/android, .skills/rust, .skills/security, .skills/ci-cd)
                ✓ MCP Gateway: 5 Tools Registered (GitHub, Exa, DevSecOps, Filesystem, CI/CD)
                ✓ Active Shell: ${activeShell.shellName} (Zsh / NuShell / Fish enabled)
                ✓ Ready for autonomous orchestration.
            """.trimIndent()

            "doctor" -> "DEVSECOPS" to """
                [NX DOCTOR] Diagnostic Health Report
                ✓ Shell Toolchains: Zsh 5.9, NuShell 0.96.1, Fish 3.7.0 (All Operational)
                ✓ Android SDK & Build Tools: OK (API Level 36, minSdk 24)
                ✓ NDK & CMake Toolchain: OK (r26d C++20 / Rust CXX)
                ✓ Room SQLite Engine: OK (Schema version 2 verified)
                ✓ Keystore Security: OK (Encrypted Hardware Storage available)
                ✓ Exa Search Engine: CONFIGURED (Neural grounding active)
                ✓ Grok/xAI TTS Engine: READY (High-fidelity audio playback)
                ✓ Overall System Health: 100% (OPTIMAL)
            """.trimIndent()

            "agents" -> "ORCHESTRATOR" to """
                [AGENT FLEET STATUS]
                ├── [PLANNER]             -> Standby (DAG Decomposer)
                ├── [ARCHITECT]           -> Standby (Architecture Design & ADRs)
                ├── [CLAUDE_CODE]         -> Standby (Senior Code Architect)
                ├── [CODEX_NATIVE]        -> Standby (C++/Rust JNI Synthesizer)
                ├── [EXA_RESEARCHER]      -> Standby (Deep Web & Docs Grounding)
                ├── [DEVSECOPS_SENTINEL]  -> Standby (OWASP MASVS / SAST Scanner)
                ├── [PERFORMANCE_AGENT]   -> Standby (Memory, GC & DEX Profiler)
                ├── [TEST_AUTOMATOR]      -> Standby (Robolectric JVM Test Harness)
                ├── [REVIEWER_AGENT]      -> Standby (Diff & Regression Blocker)
                └── [RELEASE_AGENT]       -> Standby (CI/CD Deployment & SBOM)
            """.trimIndent()

            "autopilot" -> "AUTOPILOT" to """
                [AUTOPILOT TRIGGERED]
                Goal: "${actualArgs.ifBlank { "Transform project into production-ready Android release" }}"
                Starting 12-stage Autonomous Loop:
                [01] Repository Analysis        -> [PASS]
                [02] Skill Detection            -> [PASS]
                [03] Architecture Spec          -> [PASS]
                [04] Exa Deep Research          -> [PASS]
                [05] Claude & Codex Synthesis   -> [PASS]
                [06] NDK & C++ Compilation      -> [PASS]
                [07] Unit & Regression Tests    -> [PASS]
                [08] OWASP MASVS SAST Scan      -> [PASS]
                [09] Performance Lab Benchmark  -> [PASS]
                [10] Auto-Fix & Patch Gate      -> [PASS]
                [11] CycloneDX SBOM Gen         -> [PASS]
                [12] CI/CD Release Candidate    -> [READY]
                
                Outcome: BUILD=PASS | TESTS=PASS (43/43) | SECURITY=CLEAN | CRITICAL_BUGS=0
            """.trimIndent()

            "workflow" -> "ORCHESTRATOR" to """
                [WORKFLOW ENGINE] Execution: ${actualArgs.ifBlank { "full" }}
                ✓ Loaded Skills: .skills/android, .skills/security, .skills/devsecops
                ✓ Decomposed into 7 sub-tasks across Claude Code & Codex
                ✓ Automated tests validated with 0 regressions.
            """.trimIndent()

            "debate" -> "JUDGE" to """
                [AGENT DEBATE RESOLUTION]
                Topic: "${actualArgs.ifBlank { "Rust vs C++ for Latency-Critical SIMD Pipeline" }}"
                
                [ARCHITECT]: Rust guarantees memory safety at compile-time with zero-cost abstractions.
                [SECURITY]: Rust eliminates 70% of memory corruption vulnerabilities (CWE-120 buffer overflows).
                [PERFORMANCE]: Both Rust and C++20 achieve 14.2ms SIMD throughput (48% faster than JVM).
                
                [FINAL JUDGE VERDICT]:
                Decision: Rust NDK Engine (via Cargo C-ABI & CMake)
                Confidence: 94%
                Reason 1: Total elimination of runtime buffer bounds exploits.
                Reason 2: Zero GC allocation pauses in high-throughput audio/crypto loop.
                Reason 3: Seamless JNI interoperability with Kotlin coroutines.
                
                Stored into Project Brain as ADR-018.
            """.trimIndent()

            "scan", "security", "audit" -> "DEVSECOPS" to """
                [DEVSECOPS SECURITY REPORT - OWASP MASVS v2.0]
                Scan Target: Android App Codebase
                Findings:
                1. [MASVS-STORAGE-1] Hardcoded Secret in BuildConfig (CWE-798) -> PATCH AVAILABLE
                2. [MASVS-STORAGE-2] World-Readable File Mode (CWE-276)        -> PATCH AVAILABLE
                3. [MASVS-NETWORK-1] Cleartext HTTP Endpoint (CWE-319)        -> PATCH AVAILABLE
                4. [MASVS-CODE-4]    Unsafe C++ strcpy buffer (CWE-120)       -> PATCH AVAILABLE
                
                Summary: 0 Critical (unpatched), 0 High, 2 Medium, 28 Dependencies OK
                Auto-fix engine ready. Run 'nx fix' to apply verified patches.
            """.trimIndent()

            "fix" -> "DEBUG_AGENT" to """
                [VULNERABILITY AUTO-FIX ENGINE]
                Creating isolated worktree sandbox...
                ✓ Patched CWE-798: Migrated plaintext secrets to Android Keystore / BuildConfig
                ✓ Patched CWE-276: Replaced with Context.MODE_PRIVATE
                ✓ Patched CWE-319: Enforced TLS 1.3 HTTPS endpoints
                ✓ Patched CWE-120: Replaced strcpy with bounded snprintf in C++20
                ✓ Re-running SAST scanner -> All 4 issues RESOLVED (0 Critical, 0 High).
            """.trimIndent()

            "performance", "benchmark" -> "PERFORMANCE" to """
                [PERFORMANCE LAB METRICS]
                CPU Utilization:    [████████░░░░░░░░] 28%
                RAM Footprint:      [█████░░░░░░░░░░░] 340 MB
                Disk I/O Read:      14.8 MB/s | Write: 6.4 MB/s
                Cold Startup Time:  310 ms
                Warm Startup Time:  88 ms
                APK Size:           7.84 MB
                DEX Method Count:   23,840 / 65,536 (Multi-dex safe)
                Avg GC Pause:       2.1 ms (Optimal)
            """.trimIndent()

            "sbom" -> "RELEASE_AGENT" to """
                [CYCLONEDX SBOM GENERATED]
                Format: CycloneDX v1.5 JSON
                Components: 28 tracked dependencies (Room, Compose M3, Retrofit, Coroutines, OkHttp)
                Licenses: 100% Apache-2.0 / MIT Compliant
                Vulnerabilities: 0 CVEs detected in dependency tree.
            """.trimIndent()

            "mcp" -> "MCP_GATEWAY" to """
                [MCP GATEWAY HUB]
                Tool 1: GitHub Gateway      [READ: YES | WRITE: YES | DEPLOY: NO]
                Tool 2: Exa Neural Search   [READ: YES | NETWORK: YES | SECRET: NO]
                Tool 3: DevSecOps Scanners  [READ: YES | EXECUTE: YES | DEPLOY: NO]
                Tool 4: Local Filesystem    [READ: YES | WRITE: YES | EXECUTE: NO]
                Tool 5: CI/CD Dispatcher    [READ: YES | DEPLOY: YES | APPROVAL: REQUIRED]
            """.trimIndent()

            "secrets" -> "SECRETS_VAULT" to """
                [SECRETS VAULT - ANDROID KEYSTORE BACKED]
                GEMINI_API_KEY      ●●●●●●●●●●●●●●●● (Configured)
                EXA_API_KEY         ●●●●●●●●●●●●●●●● (Configured)
                GROK_API_KEY        ●●●●●●●●●●●●●●●● (Configured)
                GITHUB_TOKEN        ●●●●●●●●●●●●●●●● (OIDC Ephemeral)
            """.trimIndent()

            "memory", "project" -> "PROJECT_BRAIN" to """
                [PROJECT BRAIN - ARCHITECTURE DECISION RECORDS]
                ADR-017: Rust & NDK C++ for Latency-Critical SIMD Pipeline [ACCEPTED]
                  -> Rationale: 48% lower benchmark latency, zero GC pause.
                ADR-016: Local-First Room Persistence with StateFlow MVVM   [ACCEPTED]
                ADR-015: DevSecOps OWASP MASVS v2.0 Compliance Baseline     [ACCEPTED]
            """.trimIndent()

            "skills" -> "SKILL_REGISTRY" to """
                [SKILL REGISTRY (.skills/)]
                ├── .skills/android/         [ENABLED] (Compose M3, MVVM, Room)
                ├── .skills/rust/            [ENABLED] (Cargo C-ABI, JNI Bindings)
                ├── .skills/cpp/             [ENABLED] (C++20 SIMD, CMakeLists)
                ├── .skills/security/        [ENABLED] (OWASP MASVS, SAST)
                ├── .skills/ci-cd/           [ENABLED] (GitHub Actions, OIDC)
                ├── .skills/research/        [ENABLED] (Exa Neural Search Grounding)
                ├── .skills/performance/     [ENABLED] (DEX, GC, Flamegraphs)
                └── .skills/testing/         [ENABLED] (Robolectric JVM, Regressions)
            """.trimIndent()

            "deploy", "ci" -> "CI_CD" to """
                [CI/CD DEPLOYMENT TRIGGERED]
                Target: ${actualArgs.ifBlank { "PRODUCTION" }}
                Pipeline: CodeQL SAST -> Tests -> NDK Build -> R8 Shrink -> Signed APK
                Duration: 19s -> Status: SUCCESS (Artifact: nexus-dev-release-v2.4.apk)
            """.trimIndent()

            "search", "exa" -> "EXA_RESEARCHER" to """
                [EXA NEURAL SEARCH]
                Query: "${actualArgs.ifBlank { "Android NDK performance optimizations" }}"
                Grounding 1: "Android NDK r26d CMake best practices for NEON SIMD" -> https://developer.android.com/ndk
                Grounding 2: "OWASP Mobile Security Testing Guide MASTG v2.0" -> https://mas.owasp.org
                Found 14 relevant technical sources.
            """.trimIndent()

            "voice" -> "GROK_TTS" to """
                [GROK / xAI VOICE ENGINE]
                Voice State: ONLINE (pt-BR / en-US High-Fidelity Neural Synthesis)
                Narration Queue: Ready for architectural and security briefings.
            """.trimIndent()

            "ls" -> "ZSH" to """
                drwxr-xr-x  12 nexus dev  4096 Aug 19 07:20 app
                -rw-r--r--   1 nexus dev  1248 Aug 19 07:15 build.gradle.kts
                -rw-r--r--   1 nexus dev   290 Aug 19 07:18 metadata.json
                drwxr-xr-x   8 nexus dev  4096 Aug 19 07:10 .skills
                drwxr-xr-x   4 nexus dev  4096 Aug 19 07:12 .mcp
                -rw-r--r--   1 nexus dev  8412 Aug 19 07:14 sbom.json
                -rw-r--r--   1 nexus dev   418 Aug 19 07:05 .env.example
            """.trimIndent()

            "git" -> "ZSH" to """
                On branch main
                Your branch is up to date with 'origin/main'.
                Changes to be committed:
                  (use "git restore --staged <file>..." to unstage)
                    modified:   app/src/main/java/com/example/ui/components/TerminalEmulator.kt
                    modified:   app/src/main/java/com/example/domain/model/DomainModels.kt
                Untracked files:
                  (use "git add <file>..." to include in what will be committed)
                    .skills/devsecops/
            """.trimIndent()

            "pwd" -> "ZSH" to "/workspace/nexus-dev-orchestrator"

            "whoami" -> "ZSH" to "nexus-orchestrator-agent (uid=1000)"

            else -> "NEXUS_CORE" to """
                [COMMAND EXECUTED]: $trimmed
                Executed via ${activeShell.shellName} environment.
                Type 'nx help' to inspect all 31 system commands or 'open config.json' in NuShell.
            """.trimIndent()
        }

        return tag to response
    }

    private fun handleShellSpecificCommand(shellCmd: String, parts: List<String>, rawInput: String): Pair<String, String> {
        return when (shellCmd) {
            "zsh" -> "ZSH" to """
                [ZSH 5.9 SHELL - POWERLEVEL10K PROMPT LOADED]
                ╭─ nexus@dev-orchestrator ~/workspace (main)
                ╰─ zsh ❯ Shell environment initialized. POSIX, Git & DevOps aliases active.
            """.trimIndent()

            "nu", "nushell" -> "NUSHELL" to """
                [NUSHELL 0.96.1 - STRUCTURED DATA & CLOUD ENGINE]
                Welcome to NuShell: Pipelines operate on structured tables, JSON, YAML & cloud telemetry.
                
                Try commands:
                  open metadata.json | get majorCapabilities
                  sys | get cpu, memory
                  ls | sort-by size -r | first 3
            """.trimIndent()

            "fish" -> "FISH" to """
                [FISH SHELL 3.7.0 - INTERACTIVE & INSTANT AUTOSUGGESTIONS]
                Welcome to fish, the friendly interactive shell!
                Instant syntax highlighting, autosuggestions as you type, and rich tab completion enabled.
            """.trimIndent()

            "chsh" -> "SHELL_MANAGER" to """
                [SHELL CONFIGURATION]
                Available shells:
                  /bin/zsh      (Default DevOps)
                  /usr/bin/nu   (Structured Data & Cloud)
                  /usr/bin/fish (Interactive Smart Shell)
                Default shell updated.
            """.trimIndent()

            else -> "SHELL" to "Shell $shellCmd ready."
        }
    }

    private fun tryHandleNuShellPipeline(input: String): Pair<String, String>? {
        val lower = input.lowercase()
        return when {
            lower.contains("open metadata.json") || lower.contains("open config.json") -> "NUSHELL" to """
                ╭───┬───────────────────────────────┬──────────────────────────────────────────────────────────╮
                │ # │              key              │                          value                           │
                ├───┼───────────────────────────────┼──────────────────────────────────────────────────────────┤
                │ 0 │ name                          │ Nexus Dev Orchestrator                                   │
                │ 1 │ description                   │ AI DevOps Command Center with Multi-Agent Swarm          │
                │ 2 │ majorCapabilities             │ [MAJOR_CAPABILITY_SERVER_SIDE_GEMINI_API]                │
                │ 3 │ defaultShell                  │ zsh (NuShell & Fish enabled)                             │
                │ 4 │ cloudSync                     │ active                                                   │
                ╰───┴───────────────────────────────┴──────────────────────────────────────────────────────────╯
            """.trimIndent()

            lower.contains("sys") -> "NUSHELL" to """
                ╭───┬─────────┬──────────────┬──────────────┬─────────────────────────╮
                │ # │  core   │ cpu_usage_%  │ mem_used_mb  │      status             │
                ├───┼─────────┼──────────────┼──────────────┼─────────────────────────┤
                │ 0 │ Core 0  │ 18.4%        │ 340 MB       │ OPTIMAL (SIMD NEON)     │
                │ 1 │ Core 1  │ 22.1%        │ 340 MB       │ ACTIVE (JNI Native)     │
                │ 2 │ Core 2  │ 14.8%        │ 340 MB       │ STANDBY (Autopilot)     │
                │ 3 │ Core 3  │ 12.0%        │ 340 MB       │ IDLE (DevSecOps)        │
                ╰───┴─────────┴──────────────┴──────────────┴─────────────────────────╯
            """.trimIndent()

            lower.contains("ls") -> "NUSHELL" to """
                ╭───┬───────────────────────────┬──────────┬─────────────┬─────────────────────╮
                │ # │           name            │   type   │    size     │      modified       │
                ├───┼───────────────────────────┼──────────┼─────────────┼─────────────────────┤
                │ 0 │ app/                      │ dir      │ 4.0 KiB     │ 2026-08-19 07:20:00 │
                │ 1 │ sbom.json                 │ file     │ 8.4 KiB     │ 2026-08-19 07:18:22 │
                │ 2 │ build.gradle.kts          │ file     │ 1.2 KiB     │ 2026-08-19 07:15:10 │
                │ 3 │ metadata.json             │ file     │ 290 B       │ 2026-08-19 07:18:00 │
                │ 4 │ .skills/                  │ dir      │ 4.0 KiB     │ 2026-08-19 07:10:00 │
                ╰───┴───────────────────────────┴──────────┴─────────────┴─────────────────────╯
            """.trimIndent()

            lower.contains("open sbom.json") || lower.contains("where license") -> "NUSHELL" to """
                ╭───┬───────────────────────────────────────────┬──────────────┬────────────┬──────────────╮
                │ # │                  package                  │   version    │  license   │  cve_count   │
                ├───┼───────────────────────────────────────────┼──────────────┼────────────┼──────────────┤
                │ 0 │ androidx.compose.material3:material3      │ 1.3.1        │ Apache-2.0 │ 0            │
                │ 1 │ androidx.room:room-runtime                │ 2.7.0-a13    │ Apache-2.0 │ 0            │
                │ 2 │ com.google.firebase:firebase-ai           │ 16.1.0       │ Apache-2.0 │ 0            │
                │ 3 │ com.squareup.retrofit2:retrofit           │ 2.11.0       │ Apache-2.0 │ 0            │
                │ 4 │ org.jetbrains.kotlinx:coroutines-android  │ 1.10.1       │ Apache-2.0 │ 0            │
                ╰───┴───────────────────────────────────────────┴──────────────┴────────────┴──────────────╯
            """.trimIndent()

            lower.contains("http get") || lower.contains("cloud") -> "NUSHELL" to """
                ╭───┬─────────────────┬──────────┬────────────┬─────────────────────────────╮
                │ # │     service     │  region  │   status   │          latency            │
                ├───┼─────────────────┼──────────┼────────────┼─────────────────────────────┤
                │ 0 │ Gemini Flash API│ us-west2 │ HEALTHY    │ 42 ms                       │
                │ 1 │ Exa Search GW   │ global   │ HEALTHY    │ 98 ms                       │
                │ 2 │ Grok TTS Voice  │ us-east1 │ HEALTHY    │ 115 ms                      │
                │ 3 │ GitHub OIDC CI  │ cloud    │ CONNECTED  │ 64 ms                       │
                ╰───┴─────────────────┴──────────┴────────────┴─────────────────────────────╯
            """.trimIndent()

            else -> null
        }
    }

    private fun tryHandleFishCommand(input: String, cmd: String, parts: List<String>): Pair<String, String>? {
        val lower = input.lowercase()
        return when {
            cmd == "fish_greeting" -> "FISH" to """
                Welcome to fish, the friendly interactive shell.
                Type `help` for instructions, or try typing commands to see instant autosuggestions!
            """.trimIndent()

            cmd == "fish_config" -> "FISH" to """
                [FISH CONFIGURATION]
                ✓ Syntax Highlighting: ENABLED (Cyan=Commands, Amber=Flags, Purple=Strings)
                ✓ Autosuggestions: INSTANT (Ghost inline completion active)
                ✓ Tab Completion: RICH 2D MATRIX
                ✓ Color Theme: Cyberpunk Neon Night
            """.trimIndent()

            cmd == "abbr" || cmd == "complete" -> "FISH" to """
                [FISH COMPLETION ENGINE]
                Registered abbreviations & completions:
                  nx a    -> nx autopilot
                  nx d    -> nx doctor
                  nx s    -> nx scan --strict
                  nx p    -> nx performance
                  nx deb  -> nx debate
            """.trimIndent()

            lower.contains("history") -> "FISH" to """
                1  nx init
                2  nx doctor
                3  nu -c "open metadata.json"
                4  nx scan --strict
                5  nx autopilot "Deploy Production APK"
                6  fish_config
            """.trimIndent()

            else -> null
        }
    }
}
