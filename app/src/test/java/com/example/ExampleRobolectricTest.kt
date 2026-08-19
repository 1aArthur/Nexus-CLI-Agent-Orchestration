package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.CicdDeployRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Nexus Dev Orchestrator", appName)
    }

    @Test
    fun `verify native module generation spec`() {
        // Mocking/testing repository native generation helper without DAO
        val fakeRepo = CicdDeployRepository(object : com.example.data.local.PipelineDao {
            override fun getAllPipelineRuns() = kotlinx.coroutines.flow.flowOf(emptyList<com.example.data.local.PipelineRunEntity>())
            override suspend fun insertPipelineRun(run: com.example.data.local.PipelineRunEntity) = 1L
            override suspend fun updatePipelineRun(run: com.example.data.local.PipelineRunEntity) {}
            override suspend fun deletePipelineRun(run: com.example.data.local.PipelineRunEntity) {}
        })

        val spec = fakeRepo.generateNativeModule("crypto_fast", "processBuffer")
        assertNotNull(spec)
        assertEquals("crypto_fast", spec.moduleName)
        assertTrue(spec.cppSource.contains("processBuffer"))
        assertTrue(spec.cmakeListsContent.contains("crypto_fast"))
    }
}
