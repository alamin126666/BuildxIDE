package com.buildxide.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.buildxide.data.local.entity.OpenFileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OpenFileDao {

    @Query("SELECT * FROM open_files WHERE project_id = :projectId ORDER BY tab_index")
    fun getOpenFilesByProject(projectId: Long): Flow<List<OpenFileEntity>>

    @Query("SELECT * FROM open_files WHERE project_id = :projectId ORDER BY tab_index")
    suspend fun getOpenFilesByProjectSync(projectId: Long): List<OpenFileEntity>

    @Query("SELECT * FROM open_files WHERE id = :id")
    suspend fun getOpenFileById(id: Long): OpenFileEntity?

    @Query("SELECT * FROM open_files WHERE project_id = :projectId AND file_path = :filePath")
    suspend fun getOpenFileByPath(projectId: Long, filePath: String): OpenFileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpenFile(openFile: OpenFileEntity): Long

    @Update
    suspend fun updateOpenFile(openFile: OpenFileEntity)

    @Delete
    suspend fun deleteOpenFile(openFile: OpenFileEntity)

    @Query("DELETE FROM open_files WHERE id = :id")
    suspend fun deleteOpenFileById(id: Long)

    @Query("DELETE FROM open_files WHERE project_id = :projectId")
    suspend fun deleteAllOpenFilesForProject(projectId: Long)

    @Query("UPDATE open_files SET content = :content, is_modified = :isModified, last_saved = :timestamp WHERE id = :id")
    suspend fun updateContent(id: Long, content: String?, isModified: Boolean, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE open_files SET sha = :sha WHERE id = :id")
    suspend fun updateSha(id: Long, sha: String?)

    @Query("SELECT MAX(tab_index) FROM open_files WHERE project_id = :projectId")
    suspend fun getMaxTabIndex(projectId: Long): Int?
}
