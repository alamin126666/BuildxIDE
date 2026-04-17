package com.buildxide.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buildxide.data.local.dao.BuildHistoryDao
import com.buildxide.data.local.dao.OpenFileDao
import com.buildxide.data.local.dao.ProjectDao
import com.buildxide.data.local.entity.BuildHistoryEntity
import com.buildxide.data.local.entity.EditorSettingsEntity
import com.buildxide.data.local.entity.OpenFileEntity
import com.buildxide.data.local.entity.ProjectEntity

@Database(
    entities = [
        ProjectEntity::class,
        OpenFileEntity::class,
        BuildHistoryEntity::class,
        EditorSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BuildxDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun openFileDao(): OpenFileDao
    abstract fun buildHistoryDao(): BuildHistoryDao

    companion object {
        private const val DATABASE_NAME = "buildx_database"

        @Volatile
        private var instance: BuildxDatabase? = null

        fun getInstance(context: Context): BuildxDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): BuildxDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BuildxDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
