package com.smithware.central

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Upsert
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.commandHubSettings: DataStore<Preferences> by preferencesDataStore("smithware_command_hub_settings")

@Entity(tableName = "connected_apps")
data class ConnectedAppEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortCode: String,
    val category: String,
    val status: String,
    val summary: String,
    val todaySummary: String,
    val metrics: List<String>,
    val recentActivity: List<String>,
    val priority: Int,
    val health: String,
    val showOnHub: Boolean,
    val isCustom: Boolean,
    val packageName: String?,
    val fallbackPlayStoreUrl: String?,
    val iconKey: String,
    val accentColor: Long?,
    val sortOrder: Int
)

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val id: String,
    val title: String,
    val body: String,
    val category: String,
    val severity: String,
    val resolved: Boolean,
    val description: String,
    val priority: String,
    val sourceAppId: String,
    val dueDateTime: Long?,
    val isCompleted: Boolean,
    val createdAt: Long
)

@Entity(tableName = "assistant_summaries")
data class AssistantSummaryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val body: String,
    val createdLabel: String
)

data class HubSettings(
    val assistantEnabled: Boolean = false,
    val dailyBriefEnabled: Boolean = true,
    val offlineMode: Boolean = true,
    val demoMode: Boolean = true,
    val premiumThemeEnabled: Boolean = true,
    val themeMode: String = "system",
    val accentIntensity: String = "balanced",
    val compactDashboard: Boolean = false,
    val onboardingComplete: Boolean = false,
    val selectedDashboardAreas: Set<String> = defaultDashboardAreas
)

val defaultDashboardAreas = setOf("work", "health", "diet", "chores", "money", "files", "reminders")

class HubTypeConverters {
    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(LIST_SEPARATOR)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(LIST_SEPARATOR)

    private companion object {
        const val LIST_SEPARATOR = "|||"
    }
}

@Dao
interface HubDao {
    @Query("SELECT * FROM connected_apps ORDER BY sortOrder DESC, priority DESC")
    fun observeApps(): Flow<List<ConnectedAppEntity>>

    @Query("SELECT * FROM connected_apps")
    suspend fun observeAppsSnapshot(): List<ConnectedAppEntity>

    @Query("SELECT * FROM alerts")
    fun observeAlerts(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM assistant_summaries")
    fun observeAssistantSummaries(): Flow<List<AssistantSummaryEntity>>

    @Query("SELECT COUNT(*) FROM connected_apps")
    suspend fun appCount(): Int

    @Query("SELECT COUNT(*) FROM connected_apps WHERE id = :appId")
    suspend fun appExists(appId: String): Int

    @Query("SELECT COUNT(*) FROM alerts WHERE id = :alertId")
    suspend fun alertExists(alertId: String): Int

    @Upsert
    suspend fun upsertApps(apps: List<ConnectedAppEntity>)

    @Upsert
    suspend fun upsertApp(app: ConnectedAppEntity)

    @Upsert
    suspend fun upsertAlerts(alerts: List<AlertEntity>)

    @Query("DELETE FROM alerts WHERE id NOT IN (:activeIds)")
    suspend fun deleteStaleAlerts(activeIds: List<String>)

    @Query(
        """
        UPDATE alerts
        SET title = :title,
            body = :body,
            category = :category,
            severity = :severity,
            description = :description,
            priority = :priority,
            sourceAppId = :sourceAppId,
            dueDateTime = :dueDateTime,
            createdAt = :createdAt
        WHERE id = :alertId
        """
    )
    suspend fun updateDemoAlert(
        alertId: String,
        title: String,
        body: String,
        category: String,
        severity: String,
        description: String,
        priority: String,
        sourceAppId: String,
        dueDateTime: Long?,
        createdAt: Long
    )

    @Upsert
    suspend fun upsertAssistantSummaries(summaries: List<AssistantSummaryEntity>)

    @Query("DELETE FROM connected_apps")
    suspend fun deleteAllApps()

    @Query("DELETE FROM alerts")
    suspend fun deleteAllAlerts()

    @Query("UPDATE alerts SET isCompleted = :completed, resolved = :completed WHERE id = :alertId")
    suspend fun setAlertCompleted(alertId: String, completed: Boolean)

    @Query("DELETE FROM assistant_summaries")
    suspend fun deleteAllAssistantSummaries()

    @Query("UPDATE connected_apps SET showOnHub = :showOnHub WHERE id = :appId")
    suspend fun setAppHubVisibility(appId: String, showOnHub: Boolean)

    @Query(
        """
        UPDATE connected_apps
        SET name = :name,
            shortCode = :shortCode,
            category = :category,
            summary = :summary,
            todaySummary = :todaySummary,
            showOnHub = :showOnHub,
            packageName = :packageName,
            fallbackPlayStoreUrl = :fallbackPlayStoreUrl,
            iconKey = :iconKey,
            accentColor = :accentColor
        WHERE id = :appId
        """
    )
    suspend fun updateCardCustomization(
        appId: String,
        name: String,
        shortCode: String,
        category: String,
        summary: String,
        todaySummary: String,
        showOnHub: Boolean,
        packageName: String?,
        fallbackPlayStoreUrl: String?,
        iconKey: String,
        accentColor: Long?
    )

    @Query("UPDATE connected_apps SET sortOrder = :sortOrder, priority = :sortOrder WHERE id = :appId")
    suspend fun updateAppSortOrder(appId: String, sortOrder: Int)

    @Query("DELETE FROM connected_apps WHERE id = :appId AND isCustom = 1")
    suspend fun deleteCustomApp(appId: String)

    @Query("DELETE FROM connected_apps WHERE isCustom = 0 AND id NOT IN (:activeIds)")
    suspend fun deleteStaleBuiltInApps(activeIds: List<String>)

    @Query("UPDATE connected_apps SET packageName = :packageName, fallbackPlayStoreUrl = :fallbackPlayStoreUrl WHERE id = :appId AND isCustom = 0")
    suspend fun updateBuiltInLaunchMetadata(appId: String, packageName: String?, fallbackPlayStoreUrl: String?)

    @Query("UPDATE connected_apps SET category = :category WHERE id = :appId AND isCustom = 0")
    suspend fun updateBuiltInCategory(appId: String, category: String)

    @Query(
        """
        UPDATE connected_apps
        SET name = :name,
            shortCode = :shortCode,
            category = :category,
            status = :status,
            summary = :summary,
            todaySummary = :todaySummary,
            metrics = :metrics,
            recentActivity = :recentActivity,
            priority = :priority,
            health = :health,
            packageName = :packageName,
            fallbackPlayStoreUrl = :fallbackPlayStoreUrl
        WHERE id = :appId AND isCustom = 0
        """
    )
    suspend fun updateBuiltInAppCatalog(
        appId: String,
        name: String,
        shortCode: String,
        category: String,
        status: String,
        summary: String,
        todaySummary: String,
        metrics: List<String>,
        recentActivity: List<String>,
        priority: Int,
        health: String,
        packageName: String?,
        fallbackPlayStoreUrl: String?
    )
}

@Database(
    entities = [ConnectedAppEntity::class, AlertEntity::class, AssistantSummaryEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(HubTypeConverters::class)
abstract class HubDatabase : RoomDatabase() {
    abstract fun hubDao(): HubDao
}

class CommandHubRepository(context: Context) {
    private val appContext = context.applicationContext
    private val database = Room.databaseBuilder(appContext, HubDatabase::class.java, "smithware_command_hub.db")
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .addMigrations(MIGRATION_3_4)
        .addMigrations(MIGRATION_4_5)
        .fallbackToDestructiveMigration()
        .build()
    private val dao = database.hubDao()

    val apps: Flow<List<ConnectedAppEntity>> = dao.observeApps()
    val alerts: Flow<List<AlertEntity>> = dao.observeAlerts()
    val assistantSummaries: Flow<List<AssistantSummaryEntity>> = dao.observeAssistantSummaries()
    val settings: Flow<HubSettings> = appContext.commandHubSettings.data.map { preferences ->
        HubSettings(
            assistantEnabled = preferences[Keys.assistantEnabled] ?: false,
            dailyBriefEnabled = preferences[Keys.dailyBriefEnabled] ?: true,
            offlineMode = preferences[Keys.offlineMode] ?: true,
            demoMode = preferences[Keys.demoMode] ?: true,
            premiumThemeEnabled = preferences[Keys.premiumThemeEnabled] ?: true,
            themeMode = preferences[Keys.themeMode] ?: "system",
            accentIntensity = preferences[Keys.accentIntensity] ?: "balanced",
            compactDashboard = preferences[Keys.compactDashboard] ?: false,
            onboardingComplete = preferences[Keys.onboardingComplete] ?: false,
            selectedDashboardAreas = preferences[Keys.selectedDashboardAreas] ?: defaultDashboardAreas
        )
    }

    suspend fun seedIfEmpty(
        apps: List<ConnectedAppEntity>,
        alerts: List<AlertEntity>,
        assistantSummaries: List<AssistantSummaryEntity>
    ) {
        if (dao.appCount() == 0) {
            dao.upsertApps(apps)
            dao.upsertAlerts(alerts)
            dao.upsertAssistantSummaries(assistantSummaries)
        }
    }

    suspend fun setAppHubVisibility(appId: String, showOnHub: Boolean) {
        dao.setAppHubVisibility(appId, showOnHub)
    }

    suspend fun addCustomApp(app: ConnectedAppEntity) {
        dao.upsertApp(app)
    }

    suspend fun updateCardCustomization(app: ConnectedAppEntity) {
        dao.updateCardCustomization(
            appId = app.id,
            name = app.name,
            shortCode = app.shortCode,
            category = app.category,
            summary = app.summary,
            todaySummary = app.todaySummary,
            showOnHub = app.showOnHub,
            packageName = app.packageName,
            fallbackPlayStoreUrl = app.fallbackPlayStoreUrl,
            iconKey = app.iconKey,
            accentColor = app.accentColor
        )
    }

    suspend fun updateAppSortOrder(appId: String, sortOrder: Int) {
        dao.updateAppSortOrder(appId, sortOrder)
    }

    suspend fun deleteCustomApp(appId: String) {
        dao.deleteCustomApp(appId)
    }

    suspend fun setAlertCompleted(alertId: String, completed: Boolean) {
        dao.setAlertCompleted(alertId, completed)
    }

    suspend fun completeOnboarding(selectedAreas: Set<String>) {
        val selectedAppIds = selectedAreas.flatMap { area ->
            when (area) {
                "work" -> listOf("workday", "markermic")
                "health" -> listOf("healthtracker")
                "diet" -> listOf("platepilot", "fridgefinish")
                "chores" -> listOf("choretracker")
                "money" -> listOf("renewal")
                "files" -> listOf("foldersmith")
                else -> emptyList()
            }
        }.toSet()
        dao.observeAppsSnapshot().filter { !it.isCustom }.forEach { app ->
            dao.setAppHubVisibility(app.id, app.id in selectedAppIds)
        }
        appContext.commandHubSettings.edit { preferences ->
            preferences[Keys.selectedDashboardAreas] = selectedAreas
            preferences[Keys.onboardingComplete] = true
        }
    }

    suspend fun syncDemoAlerts(alerts: List<AlertEntity>) {
        alerts.forEach { alert ->
            if (dao.alertExists(alert.id) == 0) {
                dao.upsertAlerts(listOf(alert))
            } else {
                dao.updateDemoAlert(
                    alertId = alert.id,
                    title = alert.title,
                    body = alert.body,
                    category = alert.category,
                    severity = alert.severity,
                    description = alert.description,
                    priority = alert.priority,
                    sourceAppId = alert.sourceAppId,
                    dueDateTime = alert.dueDateTime,
                    createdAt = alert.createdAt
                )
            }
        }
        dao.deleteStaleAlerts(alerts.map { it.id })
    }

    suspend fun syncBuiltInAppCatalog(apps: List<ConnectedAppEntity>) {
        apps.filter { !it.isCustom }.forEach { app ->
            if (dao.appExists(app.id) == 0) {
                dao.upsertApps(listOf(app))
            } else {
                dao.updateBuiltInLaunchMetadata(app.id, app.packageName, app.fallbackPlayStoreUrl)
                dao.updateBuiltInCategory(app.id, app.category)
            }
        }
        dao.deleteStaleBuiltInApps(apps.map { it.id })
    }

    suspend fun resetDemoCards(
        apps: List<ConnectedAppEntity>,
        alerts: List<AlertEntity>,
        assistantSummaries: List<AssistantSummaryEntity>
    ) {
        dao.deleteAllAssistantSummaries()
        dao.deleteAllAlerts()
        dao.deleteAllApps()
        dao.upsertApps(apps)
        dao.upsertAlerts(alerts)
        dao.upsertAssistantSummaries(assistantSummaries)
    }

    suspend fun updateAssistantEnabled(enabled: Boolean) = updateBoolean(Keys.assistantEnabled, enabled)
    suspend fun updateDailyBriefEnabled(enabled: Boolean) = updateBoolean(Keys.dailyBriefEnabled, enabled)
    suspend fun updateOfflineMode(enabled: Boolean) = updateBoolean(Keys.offlineMode, enabled)
    suspend fun updateDemoMode(enabled: Boolean) = updateBoolean(Keys.demoMode, enabled)
    suspend fun updatePremiumTheme(enabled: Boolean) = updateBoolean(Keys.premiumThemeEnabled, enabled)
    suspend fun updateThemeMode(mode: String) = updateString(Keys.themeMode, mode)
    suspend fun updateAccentIntensity(intensity: String) = updateString(Keys.accentIntensity, intensity)
    suspend fun updateCompactDashboard(enabled: Boolean) = updateBoolean(Keys.compactDashboard, enabled)

    suspend fun deleteLocalData() {
        dao.deleteAllAssistantSummaries()
        dao.deleteAllAlerts()
        dao.deleteAllApps()
        appContext.commandHubSettings.edit { preferences -> preferences.clear() }
    }

    private suspend fun updateBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        appContext.commandHubSettings.edit { preferences -> preferences[key] = value }
    }

    private suspend fun updateString(key: Preferences.Key<String>, value: String) {
        appContext.commandHubSettings.edit { preferences -> preferences[key] = value }
    }

    private object Keys {
        val assistantEnabled = booleanPreferencesKey("assistant_enabled")
        val dailyBriefEnabled = booleanPreferencesKey("daily_brief_enabled")
        val offlineMode = booleanPreferencesKey("offline_mode")
        val demoMode = booleanPreferencesKey("demo_mode")
        val premiumThemeEnabled = booleanPreferencesKey("premium_theme_enabled")
        val themeMode = stringPreferencesKey("theme_mode")
        val accentIntensity = stringPreferencesKey("accent_intensity")
        val compactDashboard = booleanPreferencesKey("compact_dashboard")
        val onboardingComplete = booleanPreferencesKey("onboarding_complete")
        val selectedDashboardAreas = stringSetPreferencesKey("selected_dashboard_areas")
    }

    private companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE connected_apps ADD COLUMN packageName TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE alerts ADD COLUMN description TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE alerts ADD COLUMN priority TEXT NOT NULL DEFAULT 'Medium'")
                db.execSQL("ALTER TABLE alerts ADD COLUMN sourceAppId TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE alerts ADD COLUMN dueDateTime INTEGER")
                db.execSQL("ALTER TABLE alerts ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE alerts ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE connected_apps ADD COLUMN iconKey TEXT NOT NULL DEFAULT 'apps'")
                db.execSQL("ALTER TABLE connected_apps ADD COLUMN accentColor INTEGER")
                db.execSQL("ALTER TABLE connected_apps ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
                db.execSQL("UPDATE connected_apps SET sortOrder = priority")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE connected_apps ADD COLUMN fallbackPlayStoreUrl TEXT")
            }
        }
    }
}
