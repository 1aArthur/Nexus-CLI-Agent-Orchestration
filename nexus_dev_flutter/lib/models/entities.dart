// Local persistence entities. Serialized as JSON (no codegen) for reliable builds.
import 'dart:convert';

class TerminalCommandEntity {
  final int id;
  final String command;
  final String output;
  final bool isError;
  final int executionTimeMs;
  final int timestamp;
  final String agentTag;

  TerminalCommandEntity({
    this.id = 0,
    required this.command,
    required this.output,
    this.isError = false,
    this.executionTimeMs = 0,
    this.timestamp = 0,
    this.agentTag = 'USER',
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'command': command,
        'output': output,
        'isError': isError,
        'executionTimeMs': executionTimeMs,
        'timestamp': timestamp,
        'agentTag': agentTag,
      };

  factory TerminalCommandEntity.fromJson(Map<String, dynamic> j) => TerminalCommandEntity(
        id: j['id'] ?? 0,
        command: j['command'] ?? '',
        output: j['output'] ?? '',
        isError: j['isError'] ?? false,
        executionTimeMs: j['executionTimeMs'] ?? 0,
        timestamp: j['timestamp'] ?? 0,
        agentTag: j['agentTag'] ?? 'USER',
      );
}

class AgentWorkflowEntity {
  final int id;
  final String title;
  final String goal;
  final String status;
  final String primaryAgent;
  final String secondaryAgent;
  final String generatedCode;
  final String executionSummary;
  final String thinkingProcess;
  final int tokenUsage;
  final double estimatedCostUsd;
  final int timestamp;

  AgentWorkflowEntity({
    this.id = 0,
    required this.title,
    required this.goal,
    required this.status,
    required this.primaryAgent,
    required this.secondaryAgent,
    this.generatedCode = '',
    this.executionSummary = '',
    this.thinkingProcess = '',
    this.tokenUsage = 1420,
    this.estimatedCostUsd = 0.0035,
    this.timestamp = 0,
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'goal': goal,
        'status': status,
        'primaryAgent': primaryAgent,
        'secondaryAgent': secondaryAgent,
        'generatedCode': generatedCode,
        'executionSummary': executionSummary,
        'thinkingProcess': thinkingProcess,
        'tokenUsage': tokenUsage,
        'estimatedCostUsd': estimatedCostUsd,
        'timestamp': timestamp,
      };

  factory AgentWorkflowEntity.fromJson(Map<String, dynamic> j) => AgentWorkflowEntity(
        id: j['id'] ?? 0,
        title: j['title'] ?? '',
        goal: j['goal'] ?? '',
        status: j['status'] ?? '',
        primaryAgent: j['primaryAgent'] ?? '',
        secondaryAgent: j['secondaryAgent'] ?? '',
        generatedCode: j['generatedCode'] ?? '',
        executionSummary: j['executionSummary'] ?? '',
        thinkingProcess: j['thinkingProcess'] ?? '',
        tokenUsage: j['tokenUsage'] ?? 0,
        estimatedCostUsd: (j['estimatedCostUsd'] ?? 0).toDouble(),
        timestamp: j['timestamp'] ?? 0,
      );
}

class SecurityAuditEntity {
  final int id;
  final String projectName;
  final String targetCode;
  final String findingsJson;
  final String severityScore;
  final int totalVulnerabilities;
  final int totalBugs;
  final String optimizationSuggestion;
  final String patchedCode;
  final String masvsCategory;
  final String sbomJson;
  final bool autoFixApproved;
  final int timestamp;

  SecurityAuditEntity({
    this.id = 0,
    required this.projectName,
    required this.targetCode,
    required this.findingsJson,
    required this.severityScore,
    required this.totalVulnerabilities,
    required this.totalBugs,
    required this.optimizationSuggestion,
    required this.patchedCode,
    required this.masvsCategory,
    required this.sbomJson,
    required this.autoFixApproved,
    this.timestamp = 0,
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'projectName': projectName,
        'targetCode': targetCode,
        'findingsJson': findingsJson,
        'severityScore': severityScore,
        'totalVulnerabilities': totalVulnerabilities,
        'totalBugs': totalBugs,
        'optimizationSuggestion': optimizationSuggestion,
        'patchedCode': patchedCode,
        'masvsCategory': masvsCategory,
        'sbomJson': sbomJson,
        'autoFixApproved': autoFixApproved,
        'timestamp': timestamp,
      };

  factory SecurityAuditEntity.fromJson(Map<String, dynamic> j) => SecurityAuditEntity(
        id: j['id'] ?? 0,
        projectName: j['projectName'] ?? '',
        targetCode: j['targetCode'] ?? '',
        findingsJson: j['findingsJson'] ?? '',
        severityScore: j['severityScore'] ?? '',
        totalVulnerabilities: j['totalVulnerabilities'] ?? 0,
        totalBugs: j['totalBugs'] ?? 0,
        optimizationSuggestion: j['optimizationSuggestion'] ?? '',
        patchedCode: j['patchedCode'] ?? '',
        masvsCategory: j['masvsCategory'] ?? '',
        sbomJson: j['sbomJson'] ?? '',
        autoFixApproved: j['autoFixApproved'] ?? false,
        timestamp: j['timestamp'] ?? 0,
      );
}

class PipelineRunEntity {
  final int id;
  final String pipelineName;
  final String triggerSource;
  final String status;
  final String stagesJson;
  final String logs;
  final int durationSeconds;
  final String artifactName;
  final int testCoveragePercent;
  final int apkSizeBytes;
  final int timestamp;

  PipelineRunEntity({
    this.id = 0,
    required this.pipelineName,
    required this.triggerSource,
    required this.status,
    required this.stagesJson,
    required this.logs,
    required this.durationSeconds,
    required this.artifactName,
    required this.testCoveragePercent,
    required this.apkSizeBytes,
    this.timestamp = 0,
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'pipelineName': pipelineName,
        'triggerSource': triggerSource,
        'status': status,
        'stagesJson': stagesJson,
        'logs': logs,
        'durationSeconds': durationSeconds,
        'artifactName': artifactName,
        'testCoveragePercent': testCoveragePercent,
        'apkSizeBytes': apkSizeBytes,
        'timestamp': timestamp,
      };

  factory PipelineRunEntity.fromJson(Map<String, dynamic> j) => PipelineRunEntity(
        id: j['id'] ?? 0,
        pipelineName: j['pipelineName'] ?? '',
        triggerSource: j['triggerSource'] ?? '',
        status: j['status'] ?? '',
        stagesJson: j['stagesJson'] ?? '',
        logs: j['logs'] ?? '',
        durationSeconds: j['durationSeconds'] ?? 0,
        artifactName: j['artifactName'] ?? '',
        testCoveragePercent: j['testCoveragePercent'] ?? 0,
        apkSizeBytes: j['apkSizeBytes'] ?? 0,
        timestamp: j['timestamp'] ?? 0,
      );
}

class SkillEntity {
  final int id;
  final String name;
  final String category;
  final String description;
  final String promptTemplate;
  final String pathFolder;
  final bool isSystemSkill;
  final bool enabled;

  SkillEntity({
    this.id = 0,
    required this.name,
    required this.category,
    required this.description,
    required this.promptTemplate,
    required this.pathFolder,
    this.isSystemSkill = true,
    this.enabled = true,
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'name': name,
        'category': category,
        'description': description,
        'promptTemplate': promptTemplate,
        'pathFolder': pathFolder,
        'isSystemSkill': isSystemSkill,
        'enabled': enabled,
      };

  factory SkillEntity.fromJson(Map<String, dynamic> j) => SkillEntity(
        id: j['id'] ?? 0,
        name: j['name'] ?? '',
        category: j['category'] ?? '',
        description: j['description'] ?? '',
        promptTemplate: j['promptTemplate'] ?? '',
        pathFolder: j['pathFolder'] ?? '',
        isSystemSkill: j['isSystemSkill'] ?? true,
        enabled: j['enabled'] ?? true,
      );
}

class ProjectBrainEntity {
  final int id;
  final String adrId;
  final String title;
  final String contextAndProblem;
  final String decision;
  final String rationale;
  final String evidence;
  final String status;
  final int timestamp;

  ProjectBrainEntity({
    this.id = 0,
    required this.adrId,
    required this.title,
    required this.contextAndProblem,
    required this.decision,
    required this.rationale,
    required this.evidence,
    required this.status,
    this.timestamp = 0,
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'adrId': adrId,
        'title': title,
        'contextAndProblem': contextAndProblem,
        'decision': decision,
        'rationale': rationale,
        'evidence': evidence,
        'status': status,
        'timestamp': timestamp,
      };

  factory ProjectBrainEntity.fromJson(Map<String, dynamic> j) => ProjectBrainEntity(
        id: j['id'] ?? 0,
        adrId: j['adrId'] ?? '',
        title: j['title'] ?? '',
        contextAndProblem: j['contextAndProblem'] ?? '',
        decision: j['decision'] ?? '',
        rationale: j['rationale'] ?? '',
        evidence: j['evidence'] ?? '',
        status: j['status'] ?? '',
        timestamp: j['timestamp'] ?? 0,
      );
}

class AgentDebateEntity {
  final int id;
  final String topic;
  final String architectOpinion;
  final String securityOpinion;
  final String performanceOpinion;
  final String judgeVerdict;
  final int confidenceScore;
  final String chosenOption;
  final int timestamp;

  AgentDebateEntity({
    this.id = 0,
    required this.topic,
    required this.architectOpinion,
    required this.securityOpinion,
    required this.performanceOpinion,
    required this.judgeVerdict,
    required this.confidenceScore,
    required this.chosenOption,
    this.timestamp = 0,
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'topic': topic,
        'architectOpinion': architectOpinion,
        'securityOpinion': securityOpinion,
        'performanceOpinion': performanceOpinion,
        'judgeVerdict': judgeVerdict,
        'confidenceScore': confidenceScore,
        'chosenOption': chosenOption,
        'timestamp': timestamp,
      };

  factory AgentDebateEntity.fromJson(Map<String, dynamic> j) => AgentDebateEntity(
        id: j['id'] ?? 0,
        topic: j['topic'] ?? '',
        architectOpinion: j['architectOpinion'] ?? '',
        securityOpinion: j['securityOpinion'] ?? '',
        performanceOpinion: j['performanceOpinion'] ?? '',
        judgeVerdict: j['judgeVerdict'] ?? '',
        confidenceScore: j['confidenceScore'] ?? 0,
        chosenOption: j['chosenOption'] ?? '',
        timestamp: j['timestamp'] ?? 0,
      );
}

class McpToolEntity {
  final int id;
  final String toolName;
  final String description;
  final bool readAllowed;
  final bool writeAllowed;
  final bool executeAllowed;
  final bool networkAllowed;
  final bool deployAllowed;
  final bool secretAccessAllowed;
  final bool enabled;

  McpToolEntity({
    this.id = 0,
    required this.toolName,
    required this.description,
    this.readAllowed = true,
    this.writeAllowed = true,
    this.executeAllowed = true,
    this.networkAllowed = true,
    this.deployAllowed = false,
    this.secretAccessAllowed = false,
    this.enabled = true,
  });

  Map<String, dynamic> toJson() => {
        'id': id,
        'toolName': toolName,
        'description': description,
        'readAllowed': readAllowed,
        'writeAllowed': writeAllowed,
        'executeAllowed': executeAllowed,
        'networkAllowed': networkAllowed,
        'deployAllowed': deployAllowed,
        'secretAccessAllowed': secretAccessAllowed,
        'enabled': enabled,
      };

  factory McpToolEntity.fromJson(Map<String, dynamic> j) => McpToolEntity(
        id: j['id'] ?? 0,
        toolName: j['toolName'] ?? '',
        description: j['description'] ?? '',
        readAllowed: j['readAllowed'] ?? true,
        writeAllowed: j['writeAllowed'] ?? true,
        executeAllowed: j['executeAllowed'] ?? true,
        networkAllowed: j['networkAllowed'] ?? true,
        deployAllowed: j['deployAllowed'] ?? false,
        secretAccessAllowed: j['secretAccessAllowed'] ?? false,
        enabled: j['enabled'] ?? true,
      );
}