// Domain models ported from the Kotlin Nexus Dev Orchestrator.
// OLED theme uses black/white only; severity is conveyed via white intensity.

enum AgentType {
  planner('Planner Agent', 'Decomposes complex requests into task execution DAG', 'gemini-3.1-pro-preview'),
  architect('Architect Agent', 'System architecture design, module graph & ADR creation', 'gemini-3.1-pro-preview'),
  claudeCode('Claude Code Agent', 'Senior Code Architect & High-Level Refactoring Engine', 'gemini-3.1-pro-preview'),
  codex('Codex Native Agent', 'High-Performance Logic Synthesizer, JNI & NDK Specialist', 'gemini-3.1-pro-preview'),
  exaResearcher('Exa Neural Researcher', 'Deep Web Grounding, GitHub Issues & Tech Intelligence', 'gemini-3.5-flash'),
  debugAgent('Debug & Log Agent', 'Log parser, stacktrace minimization & hypothesis testing', 'gemini-3.1-pro-preview'),
  devsecopsSentinel('DevSecOps Sentinel', 'SAST/DAST Vulnerability Scanner, OWASP & CWE Auditor', 'gemini-3.1-pro-preview'),
  performanceAgent('Performance Agent', 'Memory allocation, GC pauses, DEX count & SIMD speedup', 'gemini-3.5-flash'),
  testAgent('Test Automator', 'Unit, Robolectric JVM, regression & property-based tests', 'gemini-3.5-flash'),
  reviewerAgent('Reviewer Agent', 'Diff inspector, regression blocker & code standards', 'gemini-3.1-pro-preview'),
  releaseAgent('Release Agent', 'Changelog generator, versioning & CI/CD deployment', 'gemini-3.5-flash'),
  nativeNdkEngineer('Native NDK Engineer', 'C++20/Rust/JNI Bindings & CMake Build System Architect', 'gemini-3.5-flash');

  const AgentType(this.displayName, this.roleDescription, this.defaultModel);
  final String displayName;
  final String roleDescription;
  final String defaultModel;

  String get title => displayName;
}

enum AgentState {
  idle,
  planning,
  researching,
  architecting,
  writingCode,
  auditing,
  compiling,
  testing,
  profiling,
  reviewing,
  deploying,
  completed,
  error;

  String get label => name.toUpperCase();
}

class AgentLiveStatus {
  final AgentType agentType;
  final AgentState state;
  final String currentTask;
  final double progress;
  final String thoughts;
  final int activeTokens;
  final int latencyMs;

  const AgentLiveStatus({
    required this.agentType,
    this.state = AgentState.idle,
    this.currentTask = 'Standby',
    this.progress = 0,
    this.thoughts = '',
    this.activeTokens = 0,
    this.latencyMs = 42,
  });

  AgentLiveStatus copyWith({
    AgentState? state,
    String? currentTask,
    String? thoughts,
  }) =>
      AgentLiveStatus(
        agentType: agentType,
        state: state ?? this.state,
        currentTask: currentTask ?? this.currentTask,
        thoughts: thoughts ?? this.thoughts,
        progress: progress,
        activeTokens: activeTokens,
        latencyMs: latencyMs,
      );
}

enum VulnerabilitySeverity { critical, high, medium, low, clean }

class SecurityFinding {
  final String id;
  final String title;
  final VulnerabilitySeverity severity;
  final String cwe;
  final String masvsRef;
  final String masvsCategory;
  final String component;
  final int line;
  final String description;
  final String recommendation;
  final String originalSnippet;
  final String patchSnippet;
  final double confidence;
  final bool autoFixAvailable;

  const SecurityFinding({
    required this.id,
    required this.title,
    required this.severity,
    this.cwe = '',
    this.masvsRef = '',
    this.masvsCategory = '',
    this.component = '',
    this.line = 0,
    this.description = '',
    this.recommendation = '',
    this.originalSnippet = '',
    this.patchSnippet = '',
    this.confidence = 0.95,
    this.autoFixAvailable = true,
  });
}

class MasvsCategoryMetric {
  final String categoryCode;
  final String categoryName;
  final int findingsCount;
  final VulnerabilitySeverity maxSeverity;
  final bool isCompliant;

  const MasvsCategoryMetric({
    required this.categoryCode,
    required this.categoryName,
    required this.findingsCount,
    required this.maxSeverity,
    required this.isCompliant,
  });
}

class MasvsScanResult {
  final String scanId;
  final String targetName;
  final int linesScanned;
  final int complianceScore;
  final String complianceGrade;
  final int totalVulnerabilities;
  final int criticalCount;
  final int highCount;
  final int mediumCount;
  final int lowCount;
  final List<SecurityFinding> findings;
  final List<MasvsCategoryMetric> categories;
  final int scanDurationMs;
  final int autoFixPatchesCount;

  const MasvsScanResult({
    required this.scanId,
    required this.targetName,
    required this.linesScanned,
    required this.complianceScore,
    required this.complianceGrade,
    required this.totalVulnerabilities,
    required this.criticalCount,
    required this.highCount,
    required this.mediumCount,
    required this.lowCount,
    required this.findings,
    required this.categories,
    required this.scanDurationMs,
    required this.autoFixPatchesCount,
  });
}

class TelemetrySnapshot {
  final double cpuPercent;
  final double memoryUsageMb;
  final int activeThreads;
  final int latencyMs;
  final int totalRequests;
  final int totalTokens;
  final double sessionCostUsd;

  const TelemetrySnapshot({
    this.cpuPercent = 12.4,
    this.memoryUsageMb = 94.2,
    this.activeThreads = 8,
    this.latencyMs = 48,
    this.totalRequests = 24,
    this.totalTokens = 15200,
    this.sessionCostUsd = 0.0024,
  });

  TelemetrySnapshot copyWith({
    double? cpuPercent,
    double? memoryUsageMb,
    int? activeThreads,
    int? latencyMs,
    int? totalRequests,
    int? totalTokens,
    double? sessionCostUsd,
  }) =>
      TelemetrySnapshot(
        cpuPercent: cpuPercent ?? this.cpuPercent,
        memoryUsageMb: memoryUsageMb ?? this.memoryUsageMb,
        activeThreads: activeThreads ?? this.activeThreads,
        latencyMs: latencyMs ?? this.latencyMs,
        totalRequests: totalRequests ?? this.totalRequests,
        totalTokens: totalTokens ?? this.totalTokens,
        sessionCostUsd: sessionCostUsd ?? this.sessionCostUsd,
      );
}

enum TerminalShellType { zsh, nushell, fish }

class AutopilotProgress {
  final int stepIndex;
  final int totalSteps;
  final String currentStep;
  final bool active;
  final bool buildPass;
  final bool testsPass;
  final bool securityAcceptable;
  final bool lintPass;
  final int criticalBugsCount;
  final int regressionsCount;
  final List<String> logs;

  const AutopilotProgress({
    this.stepIndex = 0,
    this.totalSteps = 12,
    this.currentStep = 'Standby',
    this.active = false,
    this.buildPass = true,
    this.testsPass = true,
    this.securityAcceptable = true,
    this.lintPass = true,
    this.criticalBugsCount = 0,
    this.regressionsCount = 0,
    this.logs = const [],
  });

  AutopilotProgress copyWith({
    int? stepIndex,
    int? totalSteps,
    String? currentStep,
    bool? active,
    bool? buildPass,
    bool? testsPass,
    bool? securityAcceptable,
    bool? lintPass,
    int? criticalBugsCount,
    int? regressionsCount,
    List<String>? logs,
  }) =>
      AutopilotProgress(
        stepIndex: stepIndex ?? this.stepIndex,
        totalSteps: totalSteps ?? this.totalSteps,
        currentStep: currentStep ?? this.currentStep,
        active: active ?? this.active,
        buildPass: buildPass ?? this.buildPass,
        testsPass: testsPass ?? this.testsPass,
        securityAcceptable: securityAcceptable ?? this.securityAcceptable,
        lintPass: lintPass ?? this.lintPass,
        criticalBugsCount: criticalBugsCount ?? this.criticalBugsCount,
        regressionsCount: regressionsCount ?? this.regressionsCount,
        logs: logs ?? this.logs,
      );
}

class NativeModuleSpec {
  final String moduleName;
  final String language;
  final String packageName;
  final List<String> targetArch;
  final List<String> jniMethods;
  final String cppSource;
  final String headerSource;
  final String cmakeListsContent;
  final String kotlinWrapperCode;
  final bool isCompiled;

  const NativeModuleSpec({
    required this.moduleName,
    this.language = 'C++20',
    this.packageName = 'com.example.nativemodule',
    this.targetArch = const ['arm64-v8a', 'armeabi-v7a', 'x86_64'],
    this.jniMethods = const [],
    this.cppSource = '',
    this.headerSource = '',
    this.cmakeListsContent = '',
    this.kotlinWrapperCode = '',
    this.isCompiled = false,
  });
}

class PerformanceMetricsProfile {
  final double cpuUtilizationPercent;
  final double memoryUsageMb;
  final double apkSizeMb;
  final int dexMethodCount;
  final int coldStartTimeMs;
  final int warmStartTimeMs;
  final double gcPauseAverageMs;
  final double jniBridgeLatencyMicroseconds;
  final double nativeSimdSpeedup;

  const PerformanceMetricsProfile({
    this.cpuUtilizationPercent = 14.2,
    this.memoryUsageMb = 88.5,
    this.apkSizeMb = 18.4,
    this.dexMethodCount = 34210,
    this.coldStartTimeMs = 310,
    this.warmStartTimeMs = 85,
    this.gcPauseAverageMs = 2.1,
    this.jniBridgeLatencyMicroseconds = 0.45,
    this.nativeSimdSpeedup = 4.8,
  });
}

class PipelineStageInfo {
  final String name;
  final String status;
  final int durationSeconds;
  final String logSummary;

  const PipelineStageInfo({
    required this.name,
    required this.status,
    required this.durationSeconds,
    this.logSummary = '',
  });
}

class AgentDebateRound {
  final int roundNumber;
  final String claudeArgument;
  final String codexArgument;
  final String evaluatorVerdict;
  final double convergenceScore;

  const AgentDebateRound({
    required this.roundNumber,
    required this.claudeArgument,
    required this.codexArgument,
    required this.evaluatorVerdict,
    required this.convergenceScore,
  });
}
