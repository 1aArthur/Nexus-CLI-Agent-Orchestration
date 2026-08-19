package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TerminalCommandEntity::class,
        AgentWorkflowEntity::class,
        AgentTaskEntity::class,
        SecurityAuditEntity::class,
        PipelineRunEntity::class,
        SkillEntity::class,
        ProjectBrainEntity::class,
        AgentDebateEntity::class,
        McpToolEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun terminalDao(): TerminalDao
    abstract fun workflowDao(): WorkflowDao
    abstract fun securityAuditDao(): SecurityAuditDao
    abstract fun pipelineDao(): PipelineDao
    abstract fun skillDao(): SkillDao
    abstract fun projectBrainDao(): ProjectBrainDao
    abstract fun agentDebateDao(): AgentDebateDao
    abstract fun mcpToolDao(): McpToolDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexus_dev_orchestrator.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialSkills(database.skillDao())
                        populateInitialAdrs(database.projectBrainDao())
                        populateInitialMcpTools(database.mcpToolDao())
                        populateInitialPipelines(database.pipelineDao())
                    }
                }
            }

            private suspend fun populateInitialSkills(skillDao: SkillDao) {
                val skills = listOf(
                    SkillEntity(
                        name = "android-dev-skill",
                        category = "ANDROID",
                        description = "Jetpack Compose, WindowInsets, MVVM, Room and Modern M3 Architecture",
                        promptTemplate = "Apply Android architectural best practices with Coroutines, StateFlow, and Compose.",
                        pathFolder = ".skills/android/"
                    ),
                    SkillEntity(
                        name = "rust-ndk-engine",
                        category = "RUST",
                        description = "Rust CXX / JNI integration, Cargo C-ABI bindings and CMake bridge",
                        promptTemplate = "Implement high-throughput zero-copy SIMD processing in Rust with JNI export.",
                        pathFolder = ".skills/rust/"
                    ),
                    SkillEntity(
                        name = "cpp-ndk-simd",
                        category = "NATIVE_NDK",
                        description = "C++20, NEON SIMD, CMakeLists 3.22, memory bounds & JNI string lifecycles",
                        promptTemplate = "Generate memory-safe C++20 NDK implementations with explicit bounds checking.",
                        pathFolder = ".skills/cpp/"
                    ),
                    SkillEntity(
                        name = "devsecops-masvs-sast",
                        category = "DEVSECOPS",
                        description = "OWASP MASVS/MASTG v2.0 baseline, CWE mapper, CodeQL and Semgrep rules",
                        promptTemplate = "Audit code against OWASP Mobile Top 10, prevent hardcoded secrets and SQLi.",
                        pathFolder = ".skills/security/"
                    ),
                    SkillEntity(
                        name = "ci-cd-github-actions",
                        category = "CI_CD",
                        description = "GitHub Actions CI/CD workflows, Fastlane, OIDC token exchange and APK signing",
                        promptTemplate = "Generate secure CI/CD pipelines with short-lived OIDC tokens and automated tests.",
                        pathFolder = ".skills/github-actions/"
                    ),
                    SkillEntity(
                        name = "exa-deep-researcher",
                        category = "RESEARCH",
                        description = "Exa Neural API code search, GitHub issue mining, and official documentation grounding",
                        promptTemplate = "Research latest API changes, breaking changes, and library alternatives via Exa.",
                        pathFolder = ".skills/research/"
                    ),
                    SkillEntity(
                        name = "performance-profiler",
                        category = "PERFORMANCE",
                        description = "CPU hotspots, allocation overhead, startup cold-start and DEX bytecode optimizer",
                        promptTemplate = "Analyze flamegraphs, memory leaks, and optimize DEX/R8 bytecode.",
                        pathFolder = ".skills/performance/"
                    ),
                    SkillEntity(
                        name = "testing-suite-automator",
                        category = "TESTING",
                        description = "Robolectric JVM CUJs, property-based tests, and regression harnesses",
                        promptTemplate = "Generate comprehensive test cases with assertions for edge cases and regressions.",
                        pathFolder = ".skills/testing/"
                    )
                )
                skillDao.insertSkills(skills)
            }

            private suspend fun populateInitialAdrs(brainDao: ProjectBrainDao) {
                val adr = ProjectBrainEntity(
                    adrId = "ADR-017",
                    title = "Rust & NDK C++ for Latency-Critical SIMD Pipeline",
                    contextAndProblem = "Image and cryptographic hashing algorithms showed high GC pause times (14ms) when executed on Kotlin heap.",
                    decision = "Migrate compute-heavy hashing and token buffers to Rust & C++20 via CMake and JNI fast buffers.",
                    rationale = "Achieved 48% lower benchmark latency and eliminated runtime GC allocation pauses.",
                    evidence = "Benchmark test: Kotlin 28ms vs Rust/NDK 14.5ms (99th percentile).",
                    status = "ACCEPTED"
                )
                brainDao.insertAdr(adr)
            }

            private suspend fun populateInitialMcpTools(mcpDao: McpToolDao) {
                val tools = listOf(
                    McpToolEntity(toolName = "GitHub Gateway", description = "Repository branches, PRs, and commit inspection", readAllowed = true, writeAllowed = true, executeAllowed = false),
                    McpToolEntity(toolName = "Exa Neural Search", description = "Web grounding and real-time documentation search", readAllowed = true, writeAllowed = false, networkAllowed = true),
                    McpToolEntity(toolName = "DevSecOps Scanners", description = "CodeQL, Semgrep, Gitleaks, and OWASP MASTG checkers", readAllowed = true, writeAllowed = false, executeAllowed = true),
                    McpToolEntity(toolName = "Local Filesystem & Sandbox", description = "Workspace file editing and AST parser", readAllowed = true, writeAllowed = true, executeAllowed = false),
                    McpToolEntity(toolName = "CI/CD Dispatcher", description = "Automated deployment and release bundler", readAllowed = true, writeAllowed = false, deployAllowed = true)
                )
                mcpDao.insertTools(tools)
            }

            private suspend fun populateInitialPipelines(pipelineDao: PipelineDao) {
                val initialRun = PipelineRunEntity(
                    pipelineName = "Android Continuous Delivery [Production]",
                    triggerSource = "AUTOPILOT",
                    status = "SUCCESS",
                    stagesJson = "Lint:SUCCESS:2||Tests:SUCCESS:4||SAST:SUCCESS:3||NDK_CMake:SUCCESS:5||R8_Sign:SUCCESS:3||Deploy:SUCCESS:2",
                    logs = "[CI/CD] Autopilot automated pipeline completed. 0 Critical bugs, 0 Regressions. Release bundle deployed.",
                    durationSeconds = 19,
                    artifactName = "nexus-dev-release-v2.4.apk",
                    testCoveragePercent = 96,
                    apkSizeBytes = 7_840_000
                )
                pipelineDao.insertPipelineRun(initialRun)
            }
        }
    }
}
