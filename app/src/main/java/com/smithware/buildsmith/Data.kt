package com.smithware.buildsmith

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appName: String,
    val description: String,
    val category: String,
    val platform: String,
    val useCase: String,
    val targetUser: String,
    val problem: String,
    val difference: String,
    val usageFrequency: String,
    val scope: String,
    val monetization: String,
    val status: String,
    val buildStage: String,
    val lastEdited: String,
    val mvpProgress: Int,
    val summary: String,
    val mainFlow: String,
    val futureFeatures: String,
    val premiumFeatures: String,
    val permissions: String,
    val technicalRequirements: String,
    val risks: String,
    val buildOrder: String,
    val archived: Boolean = false
)

@Entity(tableName = "screens")
data class ScreenEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val name: String,
    val purpose: String,
    val components: String,
    val actions: String,
    val emptyState: String,
    val errorState: String,
    val navigation: String,
    val notes: String,
    val mvp: Boolean,
    val navTab: String,
    val sortOrder: Int
)

@Entity(tableName = "features")
data class FeatureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val name: String,
    val description: String,
    val priority: String,
    val complexity: String,
    val monetization: String,
    val status: String,
    val category: String,
    val notes: String
)

@Entity(tableName = "data_models")
data class DataModelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val name: String,
    val purpose: String,
    val fields: String,
    val relationships: String,
    val notes: String
)

@Entity(tableName = "prompts")
data class PromptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val title: String,
    val projectName: String,
    val type: String,
    val body: String,
    val createdAt: String
)

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val label: String,
    val checked: Boolean,
    val sortOrder: Int
)

@Dao
interface BuildSmithDao {
    @Query("SELECT * FROM projects ORDER BY archived ASC, id DESC")
    fun observeProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM screens ORDER BY sortOrder ASC")
    fun observeScreens(): Flow<List<ScreenEntity>>

    @Query("SELECT * FROM features ORDER BY id DESC")
    fun observeFeatures(): Flow<List<FeatureEntity>>

    @Query("SELECT * FROM data_models ORDER BY id DESC")
    fun observeModels(): Flow<List<DataModelEntity>>

    @Query("SELECT * FROM prompts ORDER BY id DESC")
    fun observePrompts(): Flow<List<PromptEntity>>

    @Query("SELECT * FROM checklist_items ORDER BY sortOrder ASC")
    fun observeChecklist(): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProject(project: ProjectEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertScreen(screen: ScreenEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFeature(feature: FeatureEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertModel(model: DataModelEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPrompt(prompt: PromptEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChecklistItem(item: ChecklistItemEntity): Long

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: Long)

    @Query("DELETE FROM screens WHERE id = :id")
    suspend fun deleteScreen(id: Long)

    @Query("DELETE FROM features WHERE id = :id")
    suspend fun deleteFeature(id: Long)

    @Query("DELETE FROM data_models WHERE id = :id")
    suspend fun deleteModel(id: Long)

    @Query("DELETE FROM prompts WHERE id = :id")
    suspend fun deletePrompt(id: Long)

    @Query("UPDATE projects SET archived = :archived WHERE id = :id")
    suspend fun setArchived(id: Long, archived: Boolean)

    @Query("UPDATE checklist_items SET checked = :checked WHERE id = :id")
    suspend fun setChecklistChecked(id: Long, checked: Boolean)

    @Query("DELETE FROM screens WHERE projectId = :projectId")
    suspend fun deleteScreensForProject(projectId: Long)

    @Query("DELETE FROM features WHERE projectId = :projectId")
    suspend fun deleteFeaturesForProject(projectId: Long)

    @Query("DELETE FROM data_models WHERE projectId = :projectId")
    suspend fun deleteModelsForProject(projectId: Long)

    @Query("DELETE FROM prompts WHERE projectId = :projectId")
    suspend fun deletePromptsForProject(projectId: Long)

    @Query("DELETE FROM checklist_items WHERE projectId = :projectId")
    suspend fun deleteChecklistForProject(projectId: Long)

    @Query("SELECT COUNT(*) FROM projects")
    suspend fun projectCount(): Int

    @Query("SELECT COUNT(*) FROM projects WHERE appName = :name")
    suspend fun projectCountByName(name: String): Int
}

@Database(
    entities = [
        ProjectEntity::class,
        ScreenEntity::class,
        FeatureEntity::class,
        DataModelEntity::class,
        PromptEntity::class,
        ChecklistItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BuildSmithDatabase : RoomDatabase() {
    abstract fun dao(): BuildSmithDao

    companion object {
        @Volatile private var instance: BuildSmithDatabase? = null

        fun get(context: Context): BuildSmithDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BuildSmithDatabase::class.java,
                    "buildsmith.db"
                ).build().also { instance = it }
            }
    }
}
