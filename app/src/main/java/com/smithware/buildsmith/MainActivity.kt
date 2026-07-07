package com.smithware.buildsmith

import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewKanban
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { BuildSmithApp() }
    }
}

private val Charcoal = Color(0xFF24231F)
private val Graphite = Color(0xFF34332F)
private val Cream = Color(0xFFFFF3DE)
private val WarmOrange = Color(0xFFE7792B)
private val Lime = Color(0xFFB8F05A)
private val Ink = Color(0xFF151411)

@Composable
fun BuildSmithApp(vm: BuildSmithViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val dark = when (state.settings.theme) {
        "Dark" -> true
        "Light" -> false
        else -> false
    }
    MaterialTheme(
        colorScheme = if (dark) darkColorScheme(primary = WarmOrange, secondary = Lime, background = Ink, surface = Charcoal) else lightColorScheme(primary = WarmOrange, secondary = Graphite, background = Cream, surface = Color(0xFFFFFBF2)),
        typography = MaterialTheme.typography
    ) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AppScaffold(state, vm)
        }
    }
}

enum class Tab(val label: String, val icon: ImageVector) {
    Projects("Projects", Icons.Default.ViewKanban),
    Builder("Builder", Icons.Default.ListAlt),
    Prompts("Prompts", Icons.Default.LibraryBooks),
    Launch("Launch", Icons.Default.RocketLaunch),
    Settings("Settings", Icons.Default.Settings)
}

@Composable
private fun AppScaffold(state: BuildSmithUiState, vm: BuildSmithViewModel) {
    var tab by remember { mutableStateOf(Tab.Projects) }
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                Tab.entries.forEach {
                    NavigationBarItem(selected = tab == it, onClick = { tab = it }, icon = { Icon(it.icon, it.label) }, label = { Text(it.label, maxLines = 1) })
                }
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surface)))
                .padding(padding)
        ) {
            when (tab) {
                Tab.Projects -> ProjectsDashboard(state, vm, onBuilder = { tab = Tab.Builder })
                Tab.Builder -> BuilderScreen(state, vm)
                Tab.Prompts -> PromptsScreen(state, vm)
                Tab.Launch -> LaunchScreen(state, vm)
                Tab.Settings -> SettingsScreen(state, vm)
            }
        }
    }
}

@Composable
private fun Header(title: String, subtitle: String) {
    Column(Modifier.fillMaxWidth().padding(20.dp, 18.dp, 20.dp, 8.dp)) {
        Text("BuildSmith", style = MaterialTheme.typography.labelLarge, color = WarmOrange, fontWeight = FontWeight.Bold)
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProjectsDashboard(state: BuildSmithUiState, vm: BuildSmithViewModel, onBuilder: () -> Unit) {
    var wizard by remember { mutableStateOf(false) }
    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Header("Projects Dashboard", "Turn app ideas into build-ready plans.")
            PrivacyStrip()
            Row(Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { wizard = true }) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("New app") }
                OutlinedButton(onClick = onBuilder, enabled = state.selectedProject != null) { Text("Open builder") }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = state.query, onValueChange = vm::setQuery, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), label = { Text("Search projects") }, singleLine = true)
            StatusFilters(state.statusFilter, vm::setFilter)
            AnimatedVisibility(wizard) { NewAppWizard(state, vm, onDone = { wizard = false }) }
        }
        if (state.filteredProjects.isEmpty()) {
            item { EmptyState("No app projects yet. Start with an idea and BuildSmith will turn it into a plan.") }
        } else {
            items(state.filteredProjects, key = { it.id }) { project ->
                ProjectCard(project, screens = state.screens.count { it.projectId == project.id }, features = state.features.count { it.projectId == project.id }, selected = state.selectedProject?.id == project.id, onSelect = { vm.selectProject(project.id); onBuilder() }, onArchive = { vm.archiveProject(project.id) }, onDuplicate = { vm.duplicateProject(project.id) }, onDelete = { vm.deleteProject(project.id) })
            }
        }
    }
}

@Composable
private fun PrivacyStrip() {
    Card(Modifier.fillMaxWidth().padding(20.dp, 8.dp), colors = CardDefaults.cardColors(containerColor = Charcoal), shape = RoundedCornerShape(8.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(10.dp).clip(RoundedCornerShape(50)).background(Lime))
            Spacer(Modifier.width(10.dp))
            Text("Your app ideas stay on this device.", color = Cream, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun StatusFilters(selected: String, onSelect: (String) -> Unit) {
    val statuses = listOf("All", "Idea", "Planning", "Building", "Testing", "Ready to Launch", "Launched")
    Row(Modifier.padding(20.dp, 10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        statuses.take(4).forEach { FilterChip(selected = selected == it, onClick = { onSelect(it) }, label = { Text(it) }) }
    }
}

@Composable
private fun ProjectCard(project: ProjectEntity, screens: Int, features: Int, selected: Boolean, onSelect: () -> Unit, onArchive: () -> Unit, onDuplicate: () -> Unit, onDelete: () -> Unit) {
    var menu by remember { mutableStateOf(false) }
    Card(onClick = onSelect, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFFFFE3BF) else MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(project.appName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text(project.description, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box {
                    IconButton(onClick = { menu = true }) { Icon(Icons.Default.MoreVert, "Project actions") }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Duplicate") }, onClick = { menu = false; onDuplicate() }, leadingIcon = { Icon(Icons.Default.ContentCopy, null) })
                        DropdownMenuItem(text = { Text("Archive") }, onClick = { menu = false; onArchive() }, leadingIcon = { Icon(Icons.Default.Archive, null) })
                        DropdownMenuItem(text = { Text("Delete") }, onClick = { menu = false; onDelete() }, leadingIcon = { Icon(Icons.Default.Delete, null) })
                    }
                }
            }
            LinearProgressIndicator(progress = { project.mvpProgress / 100f }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(project.platform) })
                AssistChip(onClick = {}, label = { Text(project.status) })
                AssistChip(onClick = {}, label = { Text(project.monetization) })
            }
            Text("${project.buildStage} • Edited ${project.lastEdited} • ${project.mvpProgress}% MVP • $screens screens • $features features", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun NewAppWizard(state: BuildSmithUiState, vm: BuildSmithViewModel, onDone: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Productivity") }
    var platform by remember { mutableStateOf(state.settings.defaultPlatform) }
    var useCase by remember { mutableStateOf("Public release") }
    var target by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }
    var difference by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Weekly") }
    var scope by remember { mutableStateOf("Simple utility") }
    var monetization by remember { mutableStateOf("Freemium") }
    var complexity by remember { mutableStateOf("UI: Medium, Database: Medium, API: Low, Offline: Yes, Notifications: Maybe") }
    Card(Modifier.fillMaxWidth().padding(20.dp), shape = RoundedCornerShape(8.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("New App Wizard", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Step ${step + 1} of 5", color = WarmOrange, fontWeight = FontWeight.SemiBold)
            when (step) {
                0 -> {
                    Field("App name", name) { name = it }
                    Field("App description", description) { description = it }
                    ChoiceRow("Platform", listOf("Android", "iOS", "Web", "Desktop", "Chrome Extension"), platform) { platform = it }
                    ChoiceRow("Use", listOf("Personal use", "Public release", "Business use"), useCase) { useCase = it }
                    Field("App category", category) { category = it }
                }
                1 -> {
                    Field("Who is this app for?", target) { target = it }
                    Field("What problem does it solve?", problem) { problem = it }
                    Field("Why use this instead of existing apps?", difference) { difference = it }
                    Field("How often would users open it?", frequency) { frequency = it }
                }
                2 -> ChoiceRow("Build scope", listOf("Simple utility", "Tracker/logging app", "Planner/calendar app", "Creator tool", "Business tool", "Health/fitness app", "Finance app", "File/storage app", "AI-assisted app", "Other"), scope) { scope = it }
                3 -> ChoiceRow("Monetization", listOf("Free", "One-time purchase", "Freemium", "Subscription", "Ads", "Lifetime unlock", "Pro tools", "AI credits"), monetization) { monetization = it }
                4 -> Field("Difficulty notes", complexity) { complexity = it }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { if (step == 0) onDone() else step-- }) { Text(if (step == 0) "Cancel" else "Back") }
                Button(onClick = {
                    if (step < 4) step++ else {
                        vm.createProjectFromWizard(name, description, category, platform, useCase, target, problem, difference, frequency, scope, monetization, complexity)
                        onDone()
                    }
                }, enabled = step < 4 || (name.isNotBlank() && description.isNotBlank())) { Text(if (step < 4) "Next" else "Create blueprint") }
            }
        }
    }
}

@Composable
private fun BuilderScreen(state: BuildSmithUiState, vm: BuildSmithViewModel) {
    val project = state.selectedProject
    if (project == null) {
        EmptyState("Select or create a project to start planning.")
        return
    }
    var section by remember { mutableStateOf("Blueprint") }
    val sections = listOf("Blueprint", "Screens", "Features", "Data")
    LazyColumn(contentPadding = PaddingValues(bottom = 28.dp)) {
        item {
            Header(project.appName, project.description)
            Row(Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { sections.forEach { FilterChip(selected = section == it, onClick = { section = it }, label = { Text(it) }) } }
        }
        when (section) {
            "Blueprint" -> item { Blueprint(project, state) }
            "Screens" -> item { ScreenPlanner(project, state, vm) }
            "Features" -> item { FeaturePlanner(project, state, vm) }
            "Data" -> item { DataPlanner(project, state, vm) }
        }
    }
}

@Composable
private fun Blueprint(project: ProjectEntity, state: BuildSmithUiState) {
    val sections = listOf(
        "App summary" to project.summary,
        "Target audience" to project.targetUser,
        "Core problem" to project.problem,
        "Main user flow" to project.mainFlow,
        "MVP features" to state.features.filter { it.projectId == project.id && it.priority != "Future" }.joinToString("\n") { it.name },
        "Future features" to project.futureFeatures,
        "Premium features" to project.premiumFeatures,
        "Screens needed" to state.screens.filter { it.projectId == project.id }.joinToString("\n") { it.name },
        "Data models needed" to state.models.filter { it.projectId == project.id }.joinToString("\n") { it.name },
        "Permissions needed" to project.permissions,
        "Technical requirements" to project.technicalRequirements,
        "Risks and complications" to project.risks,
        "Suggested build order" to project.buildOrder
    )
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        sections.forEach { (title, body) -> InfoCard(title, body.ifBlank { "Not planned yet." }, editable = true) }
    }
}

@Composable
private fun ScreenPlanner(project: ProjectEntity, state: BuildSmithUiState, vm: BuildSmithViewModel) {
    var name by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    PlannerAdd("Add screen", "Screen name", name, { name = it }, "Purpose", purpose, { purpose = it }) { vm.quickAddScreen(project.id, name, purpose); name = ""; purpose = "" }
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        state.screens.filter { it.projectId == project.id }.forEach {
            InfoCard("${it.sortOrder + 1}. ${it.name}", "${it.purpose}\nComponents: ${it.components}\nActions: ${it.actions}\nEmpty: ${it.emptyState}\nError: ${it.errorState}\nNavigation: ${it.navigation}\nTab: ${it.navTab}\n${if (it.mvp) "MVP" else "Future"}", onDelete = { vm.deleteScreen(it.id) })
        }
    }
}

@Composable
private fun FeaturePlanner(project: ProjectEntity, state: BuildSmithUiState, vm: BuildSmithViewModel) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    PlannerAdd("Add feature", "Feature name", name, { name = it }, "Description", description, { description = it }) { vm.quickAddFeature(project.id, name, description); name = ""; description = "" }
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        state.features.filter { it.projectId == project.id }.groupBy { it.category }.forEach { (category, features) ->
            Text(category, fontWeight = FontWeight.Bold, color = WarmOrange)
            features.forEach { InfoCard(it.name, "${it.description}\nPriority: ${it.priority} • Complexity: ${it.complexity} • Monetization: ${it.monetization} • Status: ${it.status}\n${it.notes}", onDelete = { vm.deleteFeature(it.id) }) }
        }
    }
}

@Composable
private fun DataPlanner(project: ProjectEntity, state: BuildSmithUiState, vm: BuildSmithViewModel) {
    val templates = listOf("UserProfile", "Task", "Note", "Reminder", "Transaction", "MealLog", "WeightLog", "Recording", "Folder", "Project", "Settings")
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Model templates", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            templates.take(4).forEach { AssistChip(onClick = { vm.addModelTemplate(project.id, it) }, label = { Text(it) }) }
        }
        templates.drop(4).chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { row.forEach { AssistChip(onClick = { vm.addModelTemplate(project.id, it) }, label = { Text(it) }) } }
        }
        state.models.filter { it.projectId == project.id }.forEach { InfoCard(it.name, "${it.purpose}\nFields:\n${it.fields}\nRelationships: ${it.relationships}\n${it.notes}", onDelete = { vm.deleteModel(it.id) }) }
    }
}

@Composable
private fun PromptsScreen(state: BuildSmithUiState, vm: BuildSmithViewModel) {
    val project = state.selectedProject
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    LazyColumn(contentPadding = PaddingValues(bottom = 28.dp)) {
        item {
            Header("Prompt Generator", "Create Codex-ready build, update, bug-fix, UI polish, listing, logo, and screenshot prompts.")
            if (project != null) {
                val promptTypes = listOf("Full app build prompt", "MVP-only prompt", "UI improvement prompt", "Bug fix prompt", "Feature expansion prompt", "Monetization prompt", "Play Store listing prompt", "App icon/logo prompt", "Database/storage prompt", "Refactor prompt")
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    promptTypes.chunked(2).forEach { row -> Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { row.forEach { OutlinedButton(onClick = { vm.generatePrompt(project.id, it) }) { Text(it) } } } }
                }
            }
        }
        if (state.prompts.isEmpty()) item { EmptyState("No generated prompts yet.") }
        items(state.prompts, key = { it.id }) { prompt ->
            InfoCard(
                prompt.title,
                "${prompt.projectName} - ${prompt.type} - ${prompt.createdAt}\n\n${prompt.body.take(520)}${if (prompt.body.length > 520) "\n..." else ""}",
                onCopy = { clipboard.setText(AnnotatedString(prompt.body)) },
                onShare = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, prompt.title)
                        putExtra(Intent.EXTRA_TEXT, prompt.body)
                    }
                    context.startActivity(Intent.createChooser(intent, "Export prompt as TXT"))
                },
                onDelete = { vm.deletePrompt(prompt.id) }
            )
        }
    }
}

@Composable
private fun LaunchScreen(state: BuildSmithUiState, vm: BuildSmithViewModel) {
    val project = state.selectedProject
    LazyColumn(contentPadding = PaddingValues(bottom = 28.dp)) {
        item { Header("Launch Planner", "Prepare store copy, privacy notes, testing tasks, screenshots, and release readiness.") }
        if (project == null) {
            item { EmptyState("Choose a project to prepare launch materials.") }
        } else {
            item {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    InfoCard("App tagline", "Turn app ideas into build-ready plans.")
                    InfoCard("Short description", project.description)
                    InfoCard("Long description", "${project.summary}\n\nBuilt for ${project.targetUser}. It solves: ${project.problem}")
                    InfoCard("Keywords", "${project.category}, ${project.scope}, ${project.platform}, productivity, builder, planner")
                    InfoCard("Screenshot ideas", "Dashboard progress, blueprint sections, screen planner, prompt generator, launch checklist.")
                    InfoCard("App icon prompt", "Premium studio mark for ${project.appName}, charcoal background, warm orange build-grid symbol, cream highlights, lime accent.")
                    IconStudio(project.appName)
                    InfoCard("Privacy policy notes", "All app plans stay local on device in v1. No login, no cloud, no analytics, no paid APIs.")
                    Button(onClick = { vm.generatePrompt(project.id, "Play Store listing prompt") }) { Icon(Icons.Default.Launch, null); Spacer(Modifier.width(8.dp)); Text("Generate Play Store Listing Draft") }
                    Text("Build checklist", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    state.checklist.filter { it.projectId == project.id }.forEach { item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = item.checked, onCheckedChange = { vm.setChecklist(item.id, it) })
                            Text(item.label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IconStudio(projectName: String) {
    val context = LocalContext.current
    var sourceBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var cleanedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var status by remember { mutableStateOf("Pick a square icon image, then remove the connected white background before exporting.") }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                sourceBitmap = BitmapFactory.decodeStream(stream)
                cleanedBitmap = null
                status = "Icon loaded locally. No upload required."
            }
        }
    }
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Icon Studio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { picker.launch("image/*") }) { Text("Pick icon") }
                OutlinedButton(onClick = {
                    sourceBitmap?.let {
                        cleanedBitmap = removeConnectedWhiteBackground(it)
                        status = "White background removed. Preview and export the transparent PNG."
                    }
                }, enabled = sourceBitmap != null) { Text("Remove white") }
                OutlinedButton(onClick = {
                    cleanedBitmap?.let {
                        val file = File(context.cacheDir, "${projectName.filter { c -> c.isLetterOrDigit() }}-icon-transparent.png")
                        FileOutputStream(file).use { out -> it.compress(Bitmap.CompressFormat.PNG, 100, out) }
                        val uri = androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.files", file)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "image/png"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "Export transparent icon"))
                    }
                }, enabled = cleanedBitmap != null) { Text("Export PNG") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                IconPreview("Original", sourceBitmap, Modifier.weight(1f))
                IconPreview("Transparent", cleanedBitmap, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun IconPreview(label: String, bitmap: Bitmap?, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Box(
            Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2E2D29)),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap == null) {
                Text("No image", color = Cream)
            } else {
                Image(bitmap.asImageBitmap(), contentDescription = label, modifier = Modifier.size(112.dp))
            }
        }
    }
}

private fun removeConnectedWhiteBackground(source: Bitmap): Bitmap {
    val bitmap = source.copy(Bitmap.Config.ARGB_8888, true)
    val width = bitmap.width
    val height = bitmap.height
    val visited = BooleanArray(width * height)
    val queue = ArrayDeque<Pair<Int, Int>>()
    for (x in 0 until width) {
        queue.add(x to 0)
        queue.add(x to height - 1)
    }
    for (y in 0 until height) {
        queue.add(0 to y)
        queue.add(width - 1 to y)
    }
    fun index(x: Int, y: Int) = y * width + x
    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        if (x !in 0 until width || y !in 0 until height) continue
        val idx = index(x, y)
        if (visited[idx]) continue
        visited[idx] = true
        val pixel = bitmap.getPixel(x, y)
        if (!isWhiteBackgroundPixel(pixel)) continue
        bitmap.setPixel(x, y, AndroidColor.TRANSPARENT)
        queue.add(x + 1 to y)
        queue.add(x - 1 to y)
        queue.add(x to y + 1)
        queue.add(x to y - 1)
    }
    repeat(5) {
        val toClear = mutableListOf<Pair<Int, Int>>()
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val pixel = bitmap.getPixel(x, y)
                if (AndroidColor.alpha(pixel) == 0 || !isWhiteBackgroundPixel(pixel)) continue
                val touchesTransparent =
                    AndroidColor.alpha(bitmap.getPixel(x + 1, y)) == 0 ||
                        AndroidColor.alpha(bitmap.getPixel(x - 1, y)) == 0 ||
                        AndroidColor.alpha(bitmap.getPixel(x, y + 1)) == 0 ||
                        AndroidColor.alpha(bitmap.getPixel(x, y - 1)) == 0
                if (touchesTransparent) toClear.add(x to y)
            }
        }
        toClear.forEach { (x, y) -> bitmap.setPixel(x, y, AndroidColor.TRANSPARENT) }
        if (toClear.isEmpty()) return@repeat
    }
    return bitmap
}

private fun isWhiteBackgroundPixel(pixel: Int): Boolean {
    val r = AndroidColor.red(pixel)
    val g = AndroidColor.green(pixel)
    val b = AndroidColor.blue(pixel)
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    return r >= 218 && g >= 218 && b >= 218 && max - min <= 32
}

@Composable
private fun SettingsScreen(state: BuildSmithUiState, vm: BuildSmithViewModel) {
    LazyColumn(contentPadding = PaddingValues(bottom = 28.dp)) {
        item {
            Header("Settings", "Defaults, local privacy, backup actions, and BuildSmith app info.")
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ChoiceRow("Theme", listOf("System", "Light", "Dark"), state.settings.theme) { vm.setSetting("theme", it) }
                Field("Default platform", state.settings.defaultPlatform) { vm.setSetting("platform", it) }
                Field("Default tech stack", state.settings.defaultTechStack) { vm.setSetting("stack", it) }
                Field("Default studio name", state.settings.studioName) { vm.setSetting("studio", it) }
                InfoCard("Export all projects", "Future-ready local backup action. v1 data is stored in Room and DataStore.")
                InfoCard("Import backup", "Future-ready restore action for local backups.")
                InfoCard("Reset demo data", "Delete custom projects manually, then reinstall to reseed demo projects.")
                InfoCard("About BuildSmith", "BuildSmith by Smithware Studios. Turn app ideas into build-ready plans.")
                InfoCard("Privacy note", "Your app ideas stay on this device. BuildSmith v1 has no login, no cloud, and no paid APIs.")
            }
        }
    }
}

@Composable
private fun PlannerAdd(title: String, label1: String, value1: String, onValue1: (String) -> Unit, label2: String, value2: String, onValue2: (String) -> Unit, onAdd: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(20.dp), shape = RoundedCornerShape(8.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Field(label1, value1, onValue1)
            Field(label2, value2, onValue2)
            Button(onClick = onAdd, enabled = value1.isNotBlank()) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text(title) }
        }
    }
}

@Composable
private fun InfoCard(title: String, body: String, editable: Boolean = false, onCopy: (() -> Unit)? = null, onShare: (() -> Unit)? = null, onDelete: (() -> Unit)? = null) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (editable) IconButton(onClick = {}) { Icon(Icons.Default.Edit, "Edit section") }
                if (onCopy != null) IconButton(onClick = onCopy) { Icon(Icons.Default.ContentCopy, "Copy") }
                if (onShare != null) IconButton(onClick = onShare) { Icon(Icons.Default.Share, "Export as TXT") }
                if (onDelete != null) IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Delete") }
            }
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth(), minLines = if (value.length > 80) 3 else 1)
}

@Composable
private fun ChoiceRow(label: String, values: List<String>, selected: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        values.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { FilterChip(selected = selected == it, onClick = { onSelect(it) }, label = { Text(it) }) }
            }
        }
    }
}

@Composable
private fun EmptyState(text: String) {
    Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(42.dp), tint = WarmOrange)
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}
