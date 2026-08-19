package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TerminalDao {
    @Query("SELECT * FROM terminal_history ORDER BY timestamp ASC")
    fun getAllCommands(): Flow<List<TerminalCommandEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommand(command: TerminalCommandEntity): Long

    @Query("DELETE FROM terminal_history")
    suspend fun clearHistory()
}

@Dao
interface WorkflowDao {
    @Query("SELECT * FROM agent_workflows ORDER BY timestamp DESC")
    fun getAllWorkflows(): Flow<List<AgentWorkflowEntity>>

    @Query("SELECT * FROM agent_workflows WHERE id = :id LIMIT 1")
    suspend fun getWorkflowById(id: Long): AgentWorkflowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkflow(workflow: AgentWorkflowEntity): Long

    @Update
    suspend fun updateWorkflow(workflow: AgentWorkflowEntity)

    @Delete
    suspend fun deleteWorkflow(workflow: AgentWorkflowEntity)

    @Query("SELECT * FROM agent_tasks WHERE workflowId = :workflowId ORDER BY stepOrder ASC")
    fun getTasksForWorkflow(workflowId: Long): Flow<List<AgentTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: AgentTaskEntity): Long
}

@Dao
interface SecurityAuditDao {
    @Query("SELECT * FROM security_audits ORDER BY timestamp DESC")
    fun getAllAudits(): Flow<List<SecurityAuditEntity>>

    @Query("SELECT * FROM security_audits WHERE id = :id LIMIT 1")
    suspend fun getAuditById(id: Long): SecurityAuditEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudit(audit: SecurityAuditEntity): Long

    @Update
    suspend fun updateAudit(audit: SecurityAuditEntity)

    @Delete
    suspend fun deleteAudit(audit: SecurityAuditEntity)
}

@Dao
interface PipelineDao {
    @Query("SELECT * FROM pipeline_runs ORDER BY timestamp DESC")
    fun getAllPipelineRuns(): Flow<List<PipelineRunEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPipelineRun(run: PipelineRunEntity): Long

    @Update
    suspend fun updatePipelineRun(run: PipelineRunEntity)

    @Delete
    suspend fun deletePipelineRun(run: PipelineRunEntity)
}

@Dao
interface SkillDao {
    @Query("SELECT * FROM workflow_skills ORDER BY category ASC, name ASC")
    fun getAllSkills(): Flow<List<SkillEntity>>

    @Query("SELECT * FROM workflow_skills WHERE enabled = 1")
    suspend fun getActiveSkills(): List<SkillEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkill(skill: SkillEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<SkillEntity>)

    @Update
    suspend fun updateSkill(skill: SkillEntity)
}

@Dao
interface ProjectBrainDao {
    @Query("SELECT * FROM project_brain ORDER BY timestamp DESC")
    fun getAllAdrs(): Flow<List<ProjectBrainEntity>>

    @Query("SELECT * FROM project_brain WHERE adrId = :adrId LIMIT 1")
    suspend fun getAdrById(adrId: String): ProjectBrainEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdr(adr: ProjectBrainEntity): Long

    @Update
    suspend fun updateAdr(adr: ProjectBrainEntity)
}

@Dao
interface AgentDebateDao {
    @Query("SELECT * FROM agent_debates ORDER BY timestamp DESC")
    fun getAllDebates(): Flow<List<AgentDebateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebate(debate: AgentDebateEntity): Long
}

@Dao
interface McpToolDao {
    @Query("SELECT * FROM mcp_tools ORDER BY toolName ASC")
    fun getAllTools(): Flow<List<McpToolEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTool(tool: McpToolEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTools(tools: List<McpToolEntity>)

    @Update
    suspend fun updateTool(tool: McpToolEntity)
}
