package com.smithware.buildsmith

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val Context.settingsDataStore by preferencesDataStore("settings")

data class BuildSmithSettings(
    val theme: String = "System",
    val defaultPlatform: String = "Android",
    val defaultTechStack: String = "Kotlin, Jetpack Compose, Material 3, Room, DataStore",
    val studioName: String = "Smithware Studios"
)

data class BuildSmithUiState(
    val projects: List<ProjectEntity> = emptyList(),
    val screens: List<ScreenEntity> = emptyList(),
    val features: List<FeatureEntity> = emptyList(),
    val models: List<DataModelEntity> = emptyList(),
    val prompts: List<PromptEntity> = emptyList(),
    val checklist: List<ChecklistItemEntity> = emptyList(),
    val settings: BuildSmithSettings = BuildSmithSettings(),
    val selectedProjectId: Long = 0,
    val query: String = "",
    val statusFilter: String = "All"
) {
    val selectedProject: ProjectEntity?
        get() = projects.firstOrNull { it.id == selectedProjectId } ?: projects.firstOrNull()

    val filteredProjects: List<ProjectEntity>
        get() = projects.filter { project ->
            val matchesSearch = query.isBlank() ||
                project.appName.contains(query, ignoreCase = true) ||
                project.description.contains(query, ignoreCase = true)
            val matchesStatus = statusFilter == "All" || project.status == statusFilter
            matchesSearch && matchesStatus && !project.archived
        }
}

class BuildSmithViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = BuildSmithDatabase.get(application).dao()
    private val themeKey = stringPreferencesKey("theme")
    private val platformKey = stringPreferencesKey("default_platform")
    private val stackKey = stringPreferencesKey("default_stack")
    private val studioKey = stringPreferencesKey("studio_name")

    private val mutableSelection = kotlinx.coroutines.flow.MutableStateFlow(0L)
    private val mutableQuery = kotlinx.coroutines.flow.MutableStateFlow("")
    private val mutableFilter = kotlinx.coroutines.flow.MutableStateFlow("All")

    val state: StateFlow<BuildSmithUiState> = combine(
        dao.observeProjects(),
        dao.observeScreens(),
        dao.observeFeatures(),
        dao.observeModels(),
        dao.observePrompts(),
        dao.observeChecklist(),
        settingsFlow(),
        mutableSelection,
        mutableQuery,
        mutableFilter
    ) { values ->
        val projects = values[0] as List<ProjectEntity>
        val selected = values[7] as Long
        BuildSmithUiState(
            projects = projects,
            screens = values[1] as List<ScreenEntity>,
            features = values[2] as List<FeatureEntity>,
            models = values[3] as List<DataModelEntity>,
            prompts = values[4] as List<PromptEntity>,
            checklist = values[5] as List<ChecklistItemEntity>,
            settings = values[6] as BuildSmithSettings,
            selectedProjectId = selected.takeIf { id -> projects.any { it.id == id } } ?: projects.firstOrNull()?.id ?: 0,
            query = values[8] as String,
            statusFilter = values[9] as String
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BuildSmithUiState())

    init {
        viewModelScope.launch { seedDemoDataIfNeeded() }
    }

    private fun settingsFlow() =
        getApplication<Application>().settingsDataStore.data.map {
            BuildSmithSettings(
                theme = it[themeKey] ?: "System",
                defaultPlatform = it[platformKey] ?: "Android",
                defaultTechStack = it[stackKey] ?: "Kotlin, Jetpack Compose, Material 3, Room, DataStore",
                studioName = it[studioKey] ?: "Smithware Studios"
            )
        }

    fun selectProject(id: Long) {
        mutableSelection.value = id
    }

    fun setQuery(query: String) {
        mutableQuery.value = query
    }

    fun setFilter(status: String) {
        mutableFilter.value = status
    }

    fun setSetting(key: String, value: String) {
        viewModelScope.launch {
            getApplication<Application>().settingsDataStore.edit {
                when (key) {
                    "theme" -> it[themeKey] = value
                    "platform" -> it[platformKey] = value
                    "stack" -> it[stackKey] = value
                    "studio" -> it[studioKey] = value
                }
            }
        }
    }

    fun createProjectFromWizard(
        name: String,
        description: String,
        category: String,
        platform: String,
        useCase: String,
        targetUser: String,
        problem: String,
        difference: String,
        frequency: String,
        scope: String,
        monetization: String,
        complexityNotes: String
    ) {
        if (name.isBlank() || description.isBlank()) return
        viewModelScope.launch {
            val project = ProjectEntity(
                appName = name.trim(),
                description = description.trim(),
                category = category,
                platform = platform,
                useCase = useCase,
                targetUser = targetUser.ifBlank { "Indie users with a clear recurring need." },
                problem = problem.ifBlank { "The current workflow is scattered and hard to maintain." },
                difference = difference.ifBlank { "Focused local-first planning with a polished mobile flow." },
                usageFrequency = frequency.ifBlank { "A few times per week" },
                scope = scope,
                monetization = monetization,
                status = "Planning",
                buildStage = "Blueprint created",
                lastEdited = today(),
                mvpProgress = 28,
                summary = "$name turns a rough idea into a focused $platform product plan for $useCase.",
                mainFlow = "Open dashboard, review progress, add or edit core records, inspect details, then export a build prompt.",
                futureFeatures = "Cloud sync, advanced export, templates, analytics, and collaboration.",
                premiumFeatures = premiumSuggestions(monetization),
                permissions = suggestedPermissions(scope),
                technicalRequirements = "Kotlin, Jetpack Compose, Material 3, Room, DataStore, local-first architecture, offline support.",
                risks = "Scope creep, unclear data model, overloaded first release, and launch copy that is too vague. $complexityNotes",
                buildOrder = "1. Lock MVP. 2. Build Room models. 3. Create navigation shell. 4. Implement core screens. 5. Generate prompt/export. 6. Polish UI. 7. Test APK."
            )
            val id = dao.upsertProject(project)
            addDefaultPlan(id, name, scope, monetization)
            mutableSelection.value = id
        }
    }

    fun quickAddScreen(projectId: Long, name: String, purpose: String) = viewModelScope.launch {
        if (name.isNotBlank()) {
            dao.upsertScreen(ScreenEntity(projectId = projectId, name = name, purpose = purpose, components = "Cards, actions, empty state", actions = "Open detail, edit, save", emptyState = "Nothing here yet.", errorState = "Could not load this screen.", navigation = "Bottom tab or detail route", notes = "", mvp = true, navTab = "Builder", sortOrder = state.value.screens.count { it.projectId == projectId } + 1))
        }
    }

    fun quickAddFeature(projectId: Long, name: String, description: String) = viewModelScope.launch {
        if (name.isNotBlank()) {
            dao.upsertFeature(FeatureEntity(projectId = projectId, name = name, description = description, priority = "Must-have", complexity = "Medium", monetization = "Free", status = "Planned", category = "Core features", notes = "Added from planner."))
        }
    }

    fun addModelTemplate(projectId: Long, template: String) = viewModelScope.launch {
        val fields = when (template) {
            "UserProfile" -> "id: String\nname: String\nemail: String\ncreatedAt: LocalDateTime"
            "Task" -> "id: String\ntitle: String\nnotes: String\ndueDate: LocalDate?\ncompleted: Boolean"
            "Transaction" -> "id: String\namount: Double\ncategory: String\ndate: LocalDate\nnotes: String"
            "MealLog" -> "id: String\nfoodName: String\ncalories: Int\ncarbs: Double\nprotein: Double\nfat: Double\ndate: LocalDateTime"
            "Project" -> "id: String\nname: String\nstatus: String\nlastEdited: LocalDateTime"
            else -> "id: String\ntitle: String\nnotes: String\ncreatedAt: LocalDateTime"
        }
        dao.upsertModel(DataModelEntity(projectId = projectId, name = template, purpose = "Stores $template records locally.", fields = fields, relationships = "Belongs to the active project or user profile.", notes = "Template can be edited later."))
    }

    fun generatePrompt(projectId: Long, type: String = "Full app build prompt") = viewModelScope.launch {
        val snapshot = state.value
        val project = snapshot.projects.firstOrNull { it.id == projectId } ?: return@launch
        val body = PromptFactory.generate(project, snapshot.screens.filter { it.projectId == projectId }, snapshot.features.filter { it.projectId == projectId }, snapshot.models.filter { it.projectId == projectId }, snapshot.settings, type)
        dao.upsertPrompt(PromptEntity(projectId = projectId, title = "$type - ${project.appName}", projectName = project.appName, type = type, body = body, createdAt = today()))
    }

    fun archiveProject(projectId: Long) = viewModelScope.launch { dao.setArchived(projectId, true) }

    fun duplicateProject(projectId: Long) = viewModelScope.launch {
        val snapshot = state.value
        val project = snapshot.projects.firstOrNull { it.id == projectId } ?: return@launch
        val copyId = dao.upsertProject(project.copy(id = 0, appName = "${project.appName} Copy", status = "Planning", lastEdited = today()))
        snapshot.screens.filter { it.projectId == projectId }.forEach { dao.upsertScreen(it.copy(id = 0, projectId = copyId)) }
        snapshot.features.filter { it.projectId == projectId }.forEach { dao.upsertFeature(it.copy(id = 0, projectId = copyId)) }
        snapshot.models.filter { it.projectId == projectId }.forEach { dao.upsertModel(it.copy(id = 0, projectId = copyId)) }
        snapshot.checklist.filter { it.projectId == projectId }.forEach { dao.upsertChecklistItem(it.copy(id = 0, projectId = copyId, checked = false)) }
        mutableSelection.value = copyId
    }

    fun deleteProject(projectId: Long) = viewModelScope.launch {
        dao.deleteScreensForProject(projectId)
        dao.deleteFeaturesForProject(projectId)
        dao.deleteModelsForProject(projectId)
        dao.deletePromptsForProject(projectId)
        dao.deleteChecklistForProject(projectId)
        dao.deleteProject(projectId)
    }

    fun deleteScreen(id: Long) = viewModelScope.launch { dao.deleteScreen(id) }
    fun deleteFeature(id: Long) = viewModelScope.launch { dao.deleteFeature(id) }
    fun deleteModel(id: Long) = viewModelScope.launch { dao.deleteModel(id) }
    fun deletePrompt(id: Long) = viewModelScope.launch { dao.deletePrompt(id) }
    fun setChecklist(id: Long, checked: Boolean) = viewModelScope.launch { dao.setChecklistChecked(id, checked) }

    private suspend fun seedDemoDataIfNeeded() {
        if (dao.projectCount() > 0) return
        listOf(
            DemoSeed("MarkerMic", "Audio recorder with timestamp markers", "Creator tool", "Freemium"),
            DemoSeed("PlatePilot", "Calorie, carb, and macro tracker", "Health/fitness app", "Subscription"),
            DemoSeed("Renewal Radar", "Subscription and bill tracker", "Tracker/logging app", "Lifetime unlock")
        ).forEach { seed ->
            val id = dao.upsertProject(ProjectEntity(appName = seed.name, description = seed.description, category = "Productivity", platform = "Android", useCase = "Public release", targetUser = "Solo builders and practical users who want fast planning.", problem = "Important details get scattered across notes before the first build.", difference = "BuildSmith keeps screens, features, data, prompts, and launch tasks in one local project.", usageFrequency = "Weekly during planning, daily during build", scope = seed.scope, monetization = seed.monetization, status = "Planning", buildStage = "MVP blueprint", lastEdited = today(), mvpProgress = 45, summary = "${seed.name} is a focused ${seed.scope.lowercase()} for ${seed.description}.", mainFlow = "Open dashboard, add the main record, review recent activity, edit details, and export build prompts.", futureFeatures = "Widgets, exports, cloud backup, and automation hooks.", premiumFeatures = premiumSuggestions(seed.monetization), permissions = suggestedPermissions(seed.scope), technicalRequirements = "Kotlin, Jetpack Compose, Room, DataStore, offline-first local storage.", risks = "Keep the MVP narrow and avoid launch copy that overpromises.", buildOrder = "Data layer, navigation shell, MVP screens, prompt/export, polish, release checklist."))
            addDefaultPlan(id, seed.name, seed.scope, seed.monetization)
            dao.upsertPrompt(PromptEntity(projectId = id, title = "Full app build prompt - ${seed.name}", projectName = seed.name, type = "Full app build prompt", body = PromptFactory.generate(state.value.projects.firstOrNull { it.id == id } ?: ProjectEntity(appName = seed.name, description = seed.description, category = "", platform = "Android", useCase = "", targetUser = "", problem = "", difference = "", usageFrequency = "", scope = seed.scope, monetization = seed.monetization, status = "", buildStage = "", lastEdited = "", mvpProgress = 0, summary = seed.description, mainFlow = "", futureFeatures = "", premiumFeatures = "", permissions = "", technicalRequirements = "", risks = "", buildOrder = ""), emptyList(), emptyList(), emptyList(), BuildSmithSettings(), "Full app build prompt"), createdAt = today()))
        }
    }

    private suspend fun addDefaultPlan(projectId: Long, name: String, scope: String, monetization: String) {
        listOf("Dashboard", "Add/Edit", "Detail", "Settings", "Export").forEachIndexed { index, screen ->
            dao.upsertScreen(ScreenEntity(projectId = projectId, name = screen, purpose = when (screen) { "Dashboard" -> "Show build progress and next actions"; "Export" -> "Prepare Codex-ready output"; else -> "Support the core app workflow" }, components = "Section headers, concise cards, primary action, supporting list", actions = "Create, edit, delete, navigate", emptyState = "Nothing planned yet.", errorState = "Could not load this section.", navigation = "Bottom navigation or detail route", notes = "Generated by BuildSmith.", mvp = index < 4, navTab = if (screen == "Settings") "Settings" else "Main", sortOrder = index))
        }
        listOf("Create project", "Edit saved items", "Track MVP progress", "Generate build prompt", "Launch checklist").forEach {
            dao.upsertFeature(FeatureEntity(projectId = projectId, name = it, description = "$it for $name.", priority = "Must-have", complexity = "Medium", monetization = if (it.contains("prompt", true)) monetization else "Free", status = "Planned", category = "Core features", notes = "Keep the first version focused."))
        }
        listOf("Project", "Settings").forEach { addModelTemplate(projectId, it) }
        checklistLabels.forEachIndexed { index, label -> dao.upsertChecklistItem(ChecklistItemEntity(projectId = projectId, label = label, checked = index < 3, sortOrder = index)) }
    }
}

data class DemoSeed(val name: String, val description: String, val scope: String, val monetization: String)

private fun today(): String = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

private fun premiumSuggestions(monetization: String): String =
    if (monetization == "Free") "Optional future Pro tier: unlimited projects, exports, custom templates, and AI-assisted planning."
    else "Unlimited projects, export TXT/Markdown/PDF, custom templates, premium launch checklist, cloud backup, and GitHub-ready briefs."

private fun suggestedPermissions(scope: String): String = when {
    scope.contains("File", true) -> "Storage picker access only when importing or exporting user-selected files."
    scope.contains("Health", true) -> "No sensitive permissions for v1 unless the user explicitly adds device integrations."
    scope.contains("AI", true) -> "No paid APIs in v1; keep prompt generation local and template-based."
    else -> "No special permissions required for v1."
}

val checklistLabels = listOf(
    "Idea validated",
    "App name chosen",
    "MVP scope locked",
    "Screens planned",
    "Data models planned",
    "Codex prompt generated",
    "First build created",
    "Debug APK tested",
    "UI polished",
    "Bugs fixed",
    "App icon created",
    "Play Store screenshots created",
    "Privacy policy written",
    "Store listing written",
    "Closed testing started",
    "Ready for release"
)
