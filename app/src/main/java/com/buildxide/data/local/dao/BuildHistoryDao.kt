package com.buildxide.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.buildxide.data.local.entity.BuildHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildHistoryDao {

    @Query("SELECT * FROM build_history WHERE project_id = :projectId ORDER BY started_at DESC NULLS LAST")
    fun getBuildHistoryByProject(projectId: Long): Flow<List<BuildHistoryEntity>>

    @Query("SELECT * FROM build_history WHERE project_id = :projectId ORDER BY started_at DESC NULLS LAST LIMIT :limit")
    suspend fun getRecentBuilds(projectId: Long, limit: Int = 10): List<BuildHistoryEntity>

    @Query("SELECT * FROM build_history WHERE run_id = :runId")
    suspend fun getBuildByRunId(runId: String): BuildHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildHistory(buildHistory: BuildHistoryEntity): Long

    @Update
    suspend fun updateBuildHistory(buildHistory: BuildHistoryEntity)

    @Delete
    suspend fun deleteBuildHistory(buildHistory: BuildHistoryEntity)

    @Query("DELETE FROM build_history WHERE project_id = :projectId")
    suspend fun deleteAllBuildHistoryForProject(projectId: Long)

    @Query("UPDATE build_history SET status = :status, conclusion = :conclusion, finished_at = :finishedAt WHERE run_id = :runId")
    suspend fun updateBuildStatus(
        runId: String,
        status: String?,
        conclusion: String?,
        finishedAt: Long?
    )

    @Query("UPDATE build_history SET apk_url = :apkUrl WHERE run_id = :runId")
    suspend fun updateApkUrl(runId: String, apkUrl: String?)
}
