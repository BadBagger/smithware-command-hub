package com.smithware.central

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CommandHubApp()
        }
    }
}

private val graphite = Color(0xFF121416)
private val graphiteRaised = Color(0xFF1B1E22)
private val metallic = Color(0xFFC8C1B3)
private val cream = Color(0xFFF3E8D0)
private val spark = Color(0xFFFF8A1C)
private val ember = Color(0xFFFFB347)
private val calmGreen = Color(0xFF76B889)
private val signalPurple = Color(0xFF8B5CF6)
private val steelBlue = Color(0xFF4EA3D8)
private val healthRed = Color(0xFFE85D5D)
private val fitnessViolet = Color(0xFF9B7BFF)
private val dietGreen = Color(0xFF7CCB7A)
private val choresTeal = Color(0xFF34B8A6)
private val familyLavender = Color(0xFFB08CFF)
private val financeGold = Color(0xFFFFB347)
private val filesGraphite = Color(0xFFC8C1B3)
private val tasksBlue = Color(0xFF63B3F3)
private val projectsCopper = Color(0xFFFF9D4D)

private val commandScheme = darkColorScheme(
    primary = spark,
    secondary = cream,
    tertiary = calmGreen,
    background = graphite,
    surface = graphiteRaised,
    surfaceVariant = Color(0xFF252A30),
    outline = Color(0x55C8C1B3),
    outlineVariant = Color(0x334D535A),
    onPrimary = Color(0xFF1B0B00),
    onSecondary = Color(0xFF211A0F),
    onTertiary = Color(0xFF07170D),
    onBackground = Color(0xFFF7F2E8),
    onSurface = Color(0xFFF7F2E8),
    onSurfaceVariant = Color(0xFFD4CCBD)
)

private val commandLightScheme = lightColorScheme(
    primary = spark,
    secondary = Color(0xFF3A2A13),
    tertiary = calmGreen,
    background = Color(0xFFF7F2E8),
    surface = Color(0xFFFFFAF0),
    surfaceVariant = Color(0xFFE9E1D4),
    outline = Color(0x66564B3B),
    outlineVariant = Color(0x33564B3B),
    onPrimary = Color(0xFF1B0B00),
    onSecondary = Color(0xFFF7F2E8),
    onTertiary = Color(0xFF07170D),
    onBackground = Color(0xFF17130E),
    onSurface = Color(0xFF17130E),
    onSurfaceVariant = Color(0xFF5B5246)
)

@Composable
private fun CommandHubTheme(themeMode: String, content: @Composable () -> Unit) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        "light" -> false
        "dark" -> true
        else -> systemDark
    }
    MaterialTheme(colorScheme = if (darkTheme) commandScheme else commandLightScheme, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommandHubApp(vm: CommandHubViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    var lockedFeature by remember { mutableStateOf<PremiumFeature?>(null) }
    val systemDark = isSystemInDarkTheme()
    val darkSurfaces = when (state.themeMode) {
        "light" -> false
        "dark" -> true
        else -> systemDark
    }
    CommandHubTheme(state.themeMode) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (state.onboardingComplete) {
                    NavigationBar(
                        containerColor = if (darkSurfaces) Color(0xF20E1012) else Color(0xFFF7F2E8),
                        tonalElevation = 0.dp
                    ) {
                        CommandTab.entries.forEach { tab ->
                            NavigationBarItem(
                                selected = state.selectedTab == tab,
                                onClick = { vm.selectTab(tab) },
                                icon = { Icon(tab.icon, contentDescription = tab.label, modifier = Modifier.size(26.dp)) },
                                label = { Text(tab.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF1B0B00),
                                    selectedTextColor = spark,
                                    indicatorColor = spark,
                                    unselectedIconColor = if (darkSurfaces) metallic else Color(0xFF5B5246),
                                    unselectedTextColor = if (darkSurfaces) metallic.copy(alpha = 0.82f) else Color(0xFF5B5246)
                                )
                            )
                        }
                    }
                }
            }
        ) { padding ->
            val backgroundGradient = if (darkSurfaces) {
                listOf(Color(0xFF08090A), graphite, Color(0xFF17130E))
            } else {
                listOf(Color(0xFFF7F2E8), Color(0xFFEDE4D6), Color(0xFFF9F1E3))
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            backgroundGradient
                        )
                    )
                    .padding(padding)
            ) {
                if (!state.onboardingComplete) {
                    OnboardingFlow(
                        initialSelectedAreas = state.selectedDashboardAreas,
                        onFinish = vm::completeOnboarding
                    )
                } else {
                    val selectedApp = state.selectedAppId?.let { appId -> state.apps.firstOrNull { it.id == appId } }
                    if (state.showDailyBrief) {
                        DailyCommandBriefScreen(
                            state = state,
                            onBack = vm::closeDailyBrief,
                            onRefresh = vm::refreshDailyBrief,
                            onOpenApp = { appId ->
                                vm.closeDailyBrief()
                                vm.selectApp(appId)
                            },
                            onOpenAlerts = {
                                vm.closeDailyBrief()
                                vm.selectTab(CommandTab.Alerts)
                            }
                        )
                    } else if (selectedApp != null) {
                        AppDetailScreen(
                            app = selectedApp,
                            alerts = state.alerts.filter { it.sourceAppId == selectedApp.id && !it.isCompleted },
                            onBack = vm::clearSelectedApp,
                            onToggleHub = { enabled -> vm.toggleAppOnHub(selectedApp.id, enabled) }
                        )
                    } else {
                        when (state.selectedTab) {
                            CommandTab.Hub -> HubScreen(
                                state = state,
                                onDailyBrief = vm::toggleDailyBrief,
                                onAppSelected = vm::selectApp,
                                onOpenBrief = vm::openDailyBrief,
                                onOpenApps = { vm.selectTab(CommandTab.Apps) },
                                onOpenAlerts = { vm.selectTab(CommandTab.Alerts) }
                            )
                            CommandTab.Apps -> AppsScreen(
                                state = state,
                                onCategory = vm::selectCategory,
                                onAppSelected = vm::selectApp,
                                onAddCustomCard = vm::addCustomCard,
                                onUpdateCard = vm::updateCardCustomization,
                                onToggleHub = vm::toggleAppOnHub,
                                onMoveCard = vm::moveDashboardCard,
                                onDeleteCustomCard = vm::deleteCustomCard
                            )
                            CommandTab.Assistant -> AssistantScreen(state, vm::toggleAssistant)
                            CommandTab.Alerts -> AlertsScreen(state, vm::toggleAlertCompleted, vm::selectApp)
                            CommandTab.Settings -> SettingsScreen(
                                state = state,
                                onThemeMode = vm::updateThemeMode,
                                onAccentIntensity = vm::updateAccentIntensity,
                                onCompactDashboard = vm::toggleCompactDashboard,
                                onAssistant = vm::toggleAssistant,
                                onMoveCard = vm::moveDashboardCard,
                                onShowCard = vm::toggleAppOnHub,
                                onResetDemoCards = vm::resetDemoCards,
                                onDeleteLocalData = vm::deleteLocalData,
                                onLockedFeature = { lockedFeature = it }
                            )
                        }
                    }
                }
                lockedFeature?.let { feature ->
                    PremiumFeatureSheet(
                        feature = feature,
                        onDismiss = { lockedFeature = null }
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingFlow(
    initialSelectedAreas: Set<String>,
    onFinish: (Set<String>) -> Unit
) {
    var step by remember { mutableStateOf(0) }
    var selectedAreas by remember { mutableStateOf(initialSelectedAreas.ifEmpty { defaultDashboardAreas }) }
    val areas = onboardingAreas()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            OnboardingProgress(step = step, total = 5)
        }
        item {
            when (step) {
                0 -> OnboardingWelcomeScreen()
                1 -> OnboardingConnectsScreen()
                2 -> OnboardingPrivacyScreen()
                3 -> OnboardingChooseAppsScreen(
                    areas = areas,
                    selectedAreas = selectedAreas,
                    onToggle = { areaId ->
                        selectedAreas = if (areaId in selectedAreas) {
                            selectedAreas - areaId
                        } else {
                            selectedAreas + areaId
                        }
                    }
                )
                else -> OnboardingFinishScreen(selectedAreas = selectedAreas, areas = areas)
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                if (step > 0) {
                    OutlinedButton(
                        onClick = { step -= 1 },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 54.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                        border = BorderStroke(1.dp, Color(0x55C8C1B3))
                    ) {
                        Text("Back", fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = {
                        if (step < 4) {
                            step += 1
                        } else {
                            onFinish(selectedAreas)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 54.dp),
                    enabled = step != 3 || selectedAreas.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = spark,
                        contentColor = Color(0xFF1B0B00),
                        disabledContainerColor = Color(0xFF2C3035),
                        disabledContentColor = metallic.copy(alpha = 0.72f)
                    )
                ) {
                    Text(if (step < 4) "Continue" else "Finish setup", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
private fun OnboardingProgress(step: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
            repeat(total) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .background(
                            if (index <= step) spark else Color(0x334D535A),
                            RoundedCornerShape(8.dp)
                        )
                )
            }
        }
        Text("Step ${step + 1} of $total", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = metallic)
    }
}

@Composable
private fun OnboardingWelcomeScreen() {
    OnboardingHeroCard(
        icon = Icons.Filled.Dashboard,
        title = "Your life apps, one command center.",
        body = "Smithware Command Hub gives your day one clean dashboard for tasks, reminders, alerts, and the apps that help run your life."
    )
}

@Composable
private fun OnboardingConnectsScreen() {
    OnboardingSectionCard(
        title = "What it connects",
        body = "Command Hub organizes the life areas you care about into one dashboard."
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            onboardingAreas().forEach { area ->
                AssistChip(
                    onClick = {},
                    label = { Text(area.label, fontWeight = FontWeight.SemiBold) },
                    leadingIcon = { Icon(area.icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = area.color.copy(alpha = 0.16f),
                        labelColor = cream,
                        leadingIconContentColor = area.color
                    ),
                    border = BorderStroke(1.dp, area.color.copy(alpha = 0.34f))
                )
            }
        }
    }
}

@Composable
private fun OnboardingPrivacyScreen() {
    OnboardingSectionCard(
        title = "Privacy by default",
        body = "Version 1 is local-first and built around user control."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PrivacyPoint("Data stays local in v1")
            PrivacyPoint("You control what appears in the hub")
            PrivacyPoint("No hidden monitoring")
            PrivacyPoint("No cloud account required")
        }
    }
}

@Composable
private fun OnboardingChooseAppsScreen(
    areas: List<OnboardingArea>,
    selectedAreas: Set<String>,
    onToggle: (String) -> Unit
) {
    OnboardingSectionCard(
        title = "Choose starting apps",
        body = "Pick the life areas you want on your dashboard. You can change this later."
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(9.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            areas.forEach { area ->
                FilterChip(
                    selected = area.id in selectedAreas,
                    onClick = { onToggle(area.id) },
                    label = { Text(area.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold) },
                    leadingIcon = { Icon(area.icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = premiumFilterChipColors(selectedColor = area.color),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = area.id in selectedAreas,
                        borderColor = area.color.copy(alpha = 0.28f),
                        selectedBorderColor = area.color
                    )
                )
            }
        }
    }
}

@Composable
private fun OnboardingFinishScreen(selectedAreas: Set<String>, areas: List<OnboardingArea>) {
    val selectedLabels = areas.filter { it.id in selectedAreas }.joinToString(", ") { it.label }
    OnboardingHeroCard(
        icon = Icons.Filled.CheckCircle,
        title = "Your command center is ready.",
        body = if (selectedLabels.isBlank()) {
            "Command Hub will start with a clean dashboard."
        } else {
            "Starter cards will be created for: $selectedLabels."
        }
    )
}

@Composable
private fun OnboardingHeroCard(icon: ImageVector, title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0x44FF8A1C))
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Color(0xFF2E2419), Color(0xFF171A1E), Color(0xFF302A21))))
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp), horizontalAlignment = Alignment.Start) {
                Image(
                    painter = painterResource(id = R.drawable.smithware_central_logo),
                    contentDescription = "Smithware Command Hub logo",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Icon(icon, contentDescription = null, tint = spark, modifier = Modifier.size(38.dp))
                Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = cream)
                Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun OnboardingSectionCard(
    title: String,
    body: String,
    content: @Composable ColumnScope.() -> Unit
) {
    PremiumCard(accent = spark) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = cream)
        Spacer(Modifier.height(8.dp))
        Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(18.dp))
        content()
    }
}

@Composable
private fun PrivacyPoint(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = calmGreen, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = cream)
    }
}

private data class OnboardingArea(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val color: Color
)

private fun onboardingAreas() = listOf(
    OnboardingArea("work", "Work", AppCategory.Work.icon, AppCategory.Work.color),
    OnboardingArea("health", "Health", AppCategory.Health.icon, AppCategory.Health.color),
    OnboardingArea("diet", "Diet", AppCategory.Diet.icon, AppCategory.Diet.color),
    OnboardingArea("chores", "Chores", AppCategory.Chores.icon, AppCategory.Chores.color),
    OnboardingArea("family", "Family", AppCategory.Family.icon, AppCategory.Family.color),
    OnboardingArea("money", "Money", AppCategory.Finance.icon, AppCategory.Finance.color),
    OnboardingArea("files", "Files", AppCategory.Files.icon, AppCategory.Files.color),
    OnboardingArea("reminders", "Reminders", AppCategory.Reminders.icon, AppCategory.Reminders.color)
)

@Composable
private fun HubScreen(
    state: CommandHubState,
    onDailyBrief: (Boolean) -> Unit,
    onAppSelected: (String) -> Unit,
    onOpenBrief: () -> Unit,
    onOpenApps: () -> Unit,
    onOpenAlerts: () -> Unit
) {
    val activeAlerts = state.alerts.count { !it.isCompleted }
    val activeAlertCards = state.alerts
        .filter { !it.isCompleted }
        .sortedBy { it.priority.rank }
        .take(4)
    val lifeApps = hubLifeAppCards(state.apps, state.selectedDashboardAreas)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { SmithwareLogoHeader(activeAlerts = activeAlerts) }
        item {
            TodaysCommandBriefCard(
                items = hubCommandBriefItems(),
                dailyBriefEnabled = state.dailyBriefEnabled,
                onDailyBrief = onDailyBrief,
                onOpenBrief = onOpenBrief
            )
        }
        item { SectionTitle("Life Apps Grid") }
        item {
            if (lifeApps.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Filled.Apps,
                    title = "Your command center is empty.",
                    body = "Add your first life area to start organizing your day.",
                    actionLabel = "Add life area",
                    onAction = onOpenApps
                )
            } else {
                ConnectedLifeAppsGrid(lifeApps, onAppSelected)
            }
        }
        item { SectionTitle("Alerts") }
        item {
            if (activeAlertCards.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Filled.CheckCircle,
                    title = "No urgent alerts.",
                    body = "You're clear for now.",
                    actionLabel = "View alerts",
                    onAction = onOpenAlerts
                )
            } else {
                HubAlertsSection(activeAlertCards)
            }
        }
        item { SectionTitle("Quick Actions") }
        item { QuickActionsRow() }
    }
}

@Composable
private fun AppsScreen(
    state: CommandHubState,
    onCategory: (AppCategory?) -> Unit,
    onAppSelected: (String) -> Unit,
    onAddCustomCard: (CardCustomizationDraft) -> Unit,
    onUpdateCard: (ConnectedAppCardModel, CardCustomizationDraft) -> Unit,
    onToggleHub: (String, Boolean) -> Unit,
    onMoveCard: (String, Int) -> Unit,
    onDeleteCustomCard: (String) -> Unit
) {
    val apps = state.selectedCategory?.let { selected -> state.apps.filter { it.category == selected } } ?: state.apps
    val featuredApp = apps.firstOrNull()
    val compactApps = apps.drop(1)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenshotHero(
                title = "Connected Life Apps",
                subtitle = "Your Smithware cards, grouped by the parts of life they help manage.",
                icon = Icons.Filled.Apps,
                accent = steelBlue,
                chips = listOf("${state.apps.size} cards", "${state.apps.count { it.showOnHub }} on Hub", "Local data")
            )
        }
        item {
            CategoryChips(selected = state.selectedCategory, onCategory = onCategory)
        }
        if (apps.isEmpty()) {
            item {
                val noConnectedApps = state.apps.isEmpty()
                EmptyStateCard(
                    icon = Icons.Filled.Apps,
                    title = if (noConnectedApps) "Your command center is empty." else "No cards in this category.",
                    body = if (noConnectedApps) "Add your first life area to start organizing your day." else "Try all cards or choose another life area.",
                    actionLabel = if (noConnectedApps) "Add life card" else "Show all cards",
                    onAction = { onCategory(null) }
                )
            }
        } else {
            featuredApp?.let { app ->
                item {
                    LargeFeaturedAppCard(
                        app = app,
                        alertCount = state.alerts.alertCountFor(app),
                        onAppSelected = onAppSelected
                    )
                }
            }
            if (compactApps.isNotEmpty()) {
                item { SectionTitle("Connected Cards") }
                item {
                    CompactAppGrid(
                        apps = compactApps,
                        alerts = state.alerts,
                        onAppSelected = onAppSelected
                    )
                }
            }
        }
        item { SectionTitle("Customize Cards") }
        item {
            CardCustomizationPanel(
                apps = state.apps,
                onAddCustomCard = onAddCustomCard,
                onUpdateCard = onUpdateCard,
                onToggleHub = onToggleHub,
                onMoveCard = onMoveCard,
                onDeleteCustomCard = onDeleteCustomCard
            )
        }
    }
}

@Composable
private fun AssistantScreen(state: CommandHubState, onToggle: (Boolean) -> Unit) {
    var selectedQuestion by remember { mutableStateOf("What needs attention today?") }
    val questions = assistantSuggestedQuestions()
    val responses = localAssistantResponses(selectedQuestion, state)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenshotHero(
                title = "Smithware Companion",
                subtitle = "Helpful local guidance based on connected cards, not hidden monitoring.",
                icon = Icons.Filled.AutoAwesome,
                accent = signalPurple,
                chips = listOf("Local only", "No microphone", "User controlled")
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, signalPurple.copy(alpha = 0.36f))
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF211733), Color(0xFF1A1D21), Color(0xFF2D1C10))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(signalPurple.copy(alpha = 0.22f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = signalPurple, modifier = Modifier.size(34.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text("What needs my attention today?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
                                Text("Based on your connected cards, not hidden monitoring.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Local assistant", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = metallic)
                            Switch(checked = state.assistantEnabled, onCheckedChange = onToggle)
                        }
                    }
                }
            }
        }
        item {
            AssistantSuggestedQuestions(
                questions = questions,
                selectedQuestion = selectedQuestion,
                onQuestionSelected = { selectedQuestion = it }
            )
        }
        item {
            AssistantDailyCommandSummary(state)
        }
        item { SectionTitle("Local Response Cards") }
        if (responses.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Filled.AutoAwesome,
                    title = "Nothing to summarize yet.",
                    body = "Add app cards or alerts to build your daily command brief.",
                    actionLabel = "Review suggestions",
                    onAction = { selectedQuestion = questions.first() }
                )
            }
        } else {
            items(responses, key = { it.title }) { response ->
                LocalAssistantResponseCard(response)
            }
        }
        item {
            PremiumCard(accent = calmGreen) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.PrivacyTip, null, tint = calmGreen)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Privacy posture", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("This screen uses local summaries only. No OpenAI connection, microphone, listening, cloud sync, or secret monitoring is active.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun AssistantSummaryRow(summary: AssistantSummary) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = signalPurple, modifier = Modifier.size(26.dp))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(summary.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(summary.body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(summary.createdLabel, style = MaterialTheme.typography.titleSmall, color = metallic)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CardCustomizationPanel(
    apps: List<ConnectedAppCardModel>,
    onAddCustomCard: (CardCustomizationDraft) -> Unit,
    onUpdateCard: (ConnectedAppCardModel, CardCustomizationDraft) -> Unit,
    onToggleHub: (String, Boolean) -> Unit,
    onMoveCard: (String, Int) -> Unit,
    onDeleteCustomCard: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(AppCategory.Other) }
    var iconKey by remember { mutableStateOf("apps") }
    var accentColor by remember { mutableStateOf(spark) }
    var packageName by remember { mutableStateOf("") }
    var fallbackPlayStoreUrl by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        PremiumCard(accent = spark) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(spark.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Apps, contentDescription = null, tint = spark, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Custom Card Setup", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
                    Text("Create a local tracker for any life area.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Card name") },
                singleLine = true
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("One-line summary") },
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            Text("Launch settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = packageName,
                onValueChange = { packageName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Android package name") },
                singleLine = true
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = fallbackPlayStoreUrl,
                onValueChange = { fallbackPlayStoreUrl = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Install link") },
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            Text("Category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppCategory.entries.forEach { option ->
                    FilterChip(
                        selected = category == option,
                        onClick = { category = option },
                        label = { Text(option.label, fontWeight = FontWeight.SemiBold) },
                        colors = premiumFilterChipColors(selectedColor = option.color),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = category == option,
                            borderColor = option.color.copy(alpha = 0.26f),
                            selectedBorderColor = option.color
                        )
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Icon", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cardIconOptions().forEach { option ->
                    FilterChip(
                        selected = iconKey == option.key,
                        onClick = { iconKey = option.key },
                        label = { Text(option.label, fontWeight = FontWeight.SemiBold) },
                        leadingIcon = { Icon(option.icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        colors = premiumFilterChipColors(selectedColor = accentColor),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = iconKey == option.key,
                            borderColor = accentColor.copy(alpha = 0.26f),
                            selectedBorderColor = accentColor
                        )
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Accent", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cardColorOptions().forEach { option ->
                    FilterChip(
                        selected = accentColor == option.color,
                        onClick = { accentColor = option.color },
                        label = { Text(option.label, fontWeight = FontWeight.SemiBold) },
                        colors = premiumFilterChipColors(selectedColor = option.color),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = accentColor == option.color,
                            borderColor = option.color.copy(alpha = 0.26f),
                            selectedBorderColor = option.color
                        )
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = {
                    onAddCustomCard(CardCustomizationDraft(name, summary, category, iconKey, accentColor, packageName, fallbackPlayStoreUrl))
                    name = ""
                    summary = ""
                    category = AppCategory.Other
                    iconKey = "apps"
                    accentColor = spark
                    packageName = ""
                    fallbackPlayStoreUrl = ""
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = spark, contentColor = Color(0xFF1B0B00))
            ) {
                Text("Add custom card", fontWeight = FontWeight.ExtraBold)
            }
        }

        apps.forEach { app ->
            EditableCardRow(
                app = app,
                onUpdateCard = onUpdateCard,
                onToggleHub = onToggleHub,
                onMoveCard = onMoveCard,
                onDeleteCustomCard = onDeleteCustomCard
            )
        }
    }
}

@Composable
private fun EditableCardRow(
    app: ConnectedAppCardModel,
    onUpdateCard: (ConnectedAppCardModel, CardCustomizationDraft) -> Unit,
    onToggleHub: (String, Boolean) -> Unit,
    onMoveCard: (String, Int) -> Unit,
    onDeleteCustomCard: (String) -> Unit
) {
    var name by remember(app.id, app.name) { mutableStateOf(app.name) }
    var summary by remember(app.id, app.summary) { mutableStateOf(app.summary) }
    var category by remember(app.id, app.category) { mutableStateOf(app.category) }
    var iconKey by remember(app.id, app.iconKey) { mutableStateOf(app.iconKey) }
    var accentColor by remember(app.id, app.accentColor) { mutableStateOf(app.cardAccent()) }
    var packageName by remember(app.id, app.packageName) { mutableStateOf(app.packageName.orEmpty()) }
    var fallbackPlayStoreUrl by remember(app.id, app.fallbackPlayStoreUrl) { mutableStateOf(app.fallbackPlayStoreUrl.orEmpty()) }

    PremiumCard(accent = app.cardAccent()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(accentColor.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconForKey(iconKey), contentDescription = null, tint = accentColor, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(if (app.isCustom) "Custom card" else "Starter card", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = metallic)
                Text(app.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = cream)
            }
            Switch(checked = app.showOnHub, onCheckedChange = { onToggleHub(app.id, it) })
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Rename card") },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = summary,
            onValueChange = { summary = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Summary") },
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        Text("Launch settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = packageName,
            onValueChange = { packageName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Android package name") },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = fallbackPlayStoreUrl,
            onValueChange = { fallbackPlayStoreUrl = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Install link") },
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AppCategory.entries.forEach { option ->
                FilterChip(
                    selected = category == option,
                    onClick = { category = option },
                    label = { Text(option.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                    colors = premiumFilterChipColors(selectedColor = option.color)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            cardIconOptions().forEach { option ->
                FilterChip(
                    selected = iconKey == option.key,
                    onClick = { iconKey = option.key },
                    label = { Text(option.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                    leadingIcon = { Icon(option.icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = premiumFilterChipColors(selectedColor = accentColor)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            cardColorOptions().forEach { option ->
                FilterChip(
                    selected = accentColor == option.color,
                    onClick = { accentColor = option.color },
                    label = { Text(option.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                    colors = premiumFilterChipColors(selectedColor = option.color)
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { onMoveCard(app.id, 1) },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                border = BorderStroke(1.dp, Color(0x55C8C1B3))
            ) { Text("Move up", fontWeight = FontWeight.Bold) }
            OutlinedButton(
                onClick = { onMoveCard(app.id, -1) },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                border = BorderStroke(1.dp, Color(0x55C8C1B3))
            ) { Text("Move down", fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onUpdateCard(app, CardCustomizationDraft(name, summary, category, iconKey, accentColor, packageName, fallbackPlayStoreUrl)) },
                enabled = name.isNotBlank(),
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = spark, contentColor = Color(0xFF1B0B00))
            ) { Text("Save", fontWeight = FontWeight.ExtraBold) }
            if (app.isCustom) {
                OutlinedButton(
                    onClick = { onDeleteCustomCard(app.id) },
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                    border = BorderStroke(1.dp, Color(0x66FF6257))
                ) { Text("Delete", fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AssistantSuggestedQuestions(
    questions: List<String>,
    selectedQuestion: String,
    onQuestionSelected: (String) -> Unit
) {
    PremiumCard(accent = signalPurple) {
        Text("Suggested questions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
        Spacer(Modifier.height(12.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            questions.forEach { question ->
                FilterChip(
                    selected = question == selectedQuestion,
                    onClick = { onQuestionSelected(question) },
                    label = { Text(question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                    colors = premiumFilterChipColors(selectedColor = signalPurple),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = question == selectedQuestion,
                        borderColor = signalPurple.copy(alpha = 0.28f),
                        selectedBorderColor = signalPurple
                    )
                )
            }
        }
    }
}

@Composable
private fun AssistantDailyCommandSummary(state: CommandHubState) {
    val urgentCount = state.alerts.count { !it.isCompleted && it.priority != AlertPriority.Low }
    val visibleApps = state.apps.count { it.showOnHub }
    PremiumCard(accent = spark) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(spark.copy(alpha = 0.16f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.FlashOn, contentDescription = null, tint = spark, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Daily command summary", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
                val summaryText = when {
                    visibleApps == 0 && state.alerts.isEmpty() -> "Nothing to summarize yet. Add app cards or alerts to build your daily command brief."
                    urgentCount > 0 -> "Here's what needs attention from your local cards."
                    else -> "Nothing urgent right now from your local cards."
                }
                Text(summaryText, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            AppInfoPill("$urgentCount urgent", Icons.Filled.Warning, if (urgentCount > 0) ember else calmGreen, Modifier.weight(1f))
            AppInfoPill("$visibleApps visible", Icons.Filled.Apps, steelBlue, Modifier.weight(1f))
        }
    }
}

@Composable
private fun LocalAssistantResponseCard(response: AssistantResponseCard) {
    PremiumCard(accent = response.color) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(response.color.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(response.icon, contentDescription = null, tint = response.color, modifier = Modifier.size(27.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(response.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = cream)
                Text(response.body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlertsScreen(
    state: CommandHubState,
    onCompleteAlert: (String, Boolean) -> Unit,
    onOpenApp: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(AlertListTab.Today) }
    val alerts = alertsForTab(state.alerts, selectedTab)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenshotHero(
                title = "Alerts",
                subtitle = "Urgent items first, with calm local reminders for the rest of the day.",
                icon = Icons.Filled.Notifications,
                accent = ember,
                chips = listOf("${state.alerts.count { !it.isCompleted }} active", "${state.alerts.count { it.priority == AlertPriority.High && !it.isCompleted }} high priority", "No push setup")
            )
        }
        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AlertListTab.entries.forEach { tab ->
                    FilterChip(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(tab.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                        colors = premiumFilterChipColors(selectedColor = spark),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedTab == tab,
                            borderColor = Color(0x44C8C1B3),
                            selectedBorderColor = spark
                        )
                    )
                }
            }
        }
        if (alerts.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Filled.CheckCircle,
                    title = "No urgent alerts.",
                    body = "You're clear for now.",
                    actionLabel = if (selectedTab == AlertListTab.All) "Refresh view" else "View all alerts",
                    onAction = { selectedTab = AlertListTab.All }
                )
            }
        } else {
            items(alerts, key = { it.id }) { alert ->
                AlertCard(
                    alert = alert,
                    sourceApp = state.apps.firstOrNull { it.id == alert.sourceAppId },
                    onComplete = { onCompleteAlert(alert.id, true) },
                    onOpen = { alert.sourceAppId.takeIf { it.isNotBlank() }?.let(onOpenApp) }
                )
            }
        }
    }
}

@Composable
private fun DailyCommandBriefScreen(
    state: CommandHubState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onOpenApp: (String) -> Unit,
    onOpenAlerts: () -> Unit
) {
    val sections = dailyCommandBriefSections(state)
    val activeAlerts = state.alerts.count { !it.isCompleted }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onBack,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                    border = BorderStroke(1.dp, Color(0x44C8C1B3))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Back", fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            ScreenshotHero(
                title = "Daily Command Brief",
                subtitle = "A rule-based summary from local cards and realistic starter data.",
                icon = Icons.Filled.FlashOn,
                accent = spark,
                chips = listOf("Start here", "Needs attention", "All clear")
            )
        }
        item {
            PremiumCard(accent = spark) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    AppInfoPill("${state.apps.count { it.showOnHub }} cards", Icons.Filled.Apps, steelBlue, Modifier.weight(1f))
                    AppInfoPill("$activeAlerts alerts", Icons.Filled.Warning, if (activeAlerts > 0) ember else calmGreen, Modifier.weight(1f))
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onRefresh,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = spark, contentColor = Color(0xFF1B0B00))
                ) {
                    Text("Refresh brief", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                }
                if (state.dailyBriefRefreshCount > 0) {
                    Spacer(Modifier.height(10.dp))
                    Text("Refreshed from local data.", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = metallic)
                }
            }
        }
        items(sections, key = { it.title }) { section ->
            DailyBriefSectionCard(section, onOpenApp, onOpenAlerts)
        }
    }
}

@Composable
private fun DailyBriefSectionCard(
    section: DailyBriefSection,
    onOpenApp: (String) -> Unit,
    onOpenAlerts: () -> Unit
) {
    PremiumCard(accent = section.color) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(section.color.copy(alpha = 0.16f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(section.icon, contentDescription = null, tint = section.color, modifier = Modifier.size(29.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(section.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
                Text(section.subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(14.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            section.items.forEach { item ->
                DailyBriefItemRow(item)
            }
        }
        section.actionLabel?.let { label ->
            Spacer(Modifier.height(14.dp))
            OutlinedButton(
                onClick = {
                    when {
                        section.actionAppId != null -> onOpenApp(section.actionAppId)
                        section.opensAlerts -> onOpenAlerts()
                    }
                },
                modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                border = BorderStroke(1.dp, section.color.copy(alpha = 0.55f))
            ) {
                Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
private fun DailyBriefItemRow(item: CommandBriefItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x22252A30), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(25.dp))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
            Text(item.body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SettingsScreen(
    state: CommandHubState,
    onThemeMode: (String) -> Unit,
    onAccentIntensity: (String) -> Unit,
    onCompactDashboard: (Boolean) -> Unit,
    onAssistant: (Boolean) -> Unit,
    onMoveCard: (String, Int) -> Unit,
    onShowCard: (String, Boolean) -> Unit,
    onResetDemoCards: () -> Unit,
    onDeleteLocalData: () -> Unit,
    onLockedFeature: (PremiumFeature) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenshotHero(
                title = "Settings & Privacy",
                subtitle = "Local-first controls for appearance, cards, assistant behavior, and data.",
                icon = Icons.Filled.PrivacyTip,
                accent = calmGreen,
                chips = listOf("Local data", "No hidden monitoring", "No cloud sync")
            )
        }
        item {
            AppearanceSettingsSection(
                themeMode = state.themeMode,
                accentIntensity = state.accentIntensity,
                compactDashboard = state.compactDashboard,
                onThemeMode = onThemeMode,
                onAccentIntensity = onAccentIntensity,
                onCompactDashboard = onCompactDashboard
            )
        }
        item {
            DashboardSettingsSection(
                apps = state.apps,
                onMoveCard = onMoveCard,
                onShowCard = onShowCard,
                onResetDemoCards = {
                    onResetDemoCards()
                    Toast.makeText(context, "Starter cards reset.", Toast.LENGTH_SHORT).show()
                }
            )
        }
        item {
            AssistantSettingsSection(state.assistantEnabled, onAssistant)
        }
        item {
            PrivacySettingsScreen(onDeleteLocalData = onDeleteLocalData)
        }
        item {
            FuturePremiumSettingsSection(onLockedFeature)
        }
        item {
            AboutSettingsSection()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppearanceSettingsSection(
    themeMode: String,
    accentIntensity: String,
    compactDashboard: Boolean,
    onThemeMode: (String) -> Unit,
    onAccentIntensity: (String) -> Unit,
    onCompactDashboard: (Boolean) -> Unit
) {
    PremiumCard(accent = spark) {
        SettingsSectionHeader(Icons.Filled.AutoAwesome, "Appearance", "Tune the Smithware dashboard feel.")
        Spacer(Modifier.height(14.dp))
        Text("Theme", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("system" to "System", "light" to "Light", "dark" to "Dark").forEach { (value, label) ->
                FilterChip(
                    selected = themeMode == value,
                    onClick = { onThemeMode(value) },
                    label = { Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                    colors = premiumFilterChipColors(),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = themeMode == value,
                        borderColor = Color(0x44C8C1B3),
                        selectedBorderColor = spark
                    )
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Accent intensity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("low" to "Low", "balanced" to "Balanced", "bold" to "Bold").forEach { (value, label) ->
                FilterChip(
                    selected = accentIntensity == value,
                    onClick = { onAccentIntensity(value) },
                    label = { Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                    colors = premiumFilterChipColors(selectedColor = spark),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = accentIntensity == value,
                        borderColor = Color(0x44C8C1B3),
                        selectedBorderColor = spark
                    )
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        SettingsInlineToggle(
            title = "Compact dashboard",
            body = "Reduce vertical spacing on the Hub when you want a denser view.",
            checked = compactDashboard,
            onChange = onCompactDashboard
        )
    }
}

@Composable
private fun DashboardSettingsSection(
    apps: List<ConnectedAppCardModel>,
    onMoveCard: (String, Int) -> Unit,
    onShowCard: (String, Boolean) -> Unit,
    onResetDemoCards: () -> Unit
) {
    val visibleApps = apps.filter { it.showOnHub }.take(5)
    val hiddenApps = apps.filterNot { it.showOnHub }
    PremiumCard(accent = steelBlue) {
        SettingsSectionHeader(Icons.Filled.Dashboard, "Dashboard", "Choose what appears in the Hub.")
        Spacer(Modifier.height(14.dp))
        Text("Reorder cards", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
        Spacer(Modifier.height(8.dp))
        if (visibleApps.isEmpty()) {
            SettingsEmptyText("No visible dashboard cards right now.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                visibleApps.forEach { app ->
                    SettingsAppOrderRow(app, onMoveCard)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Hidden cards", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
        Spacer(Modifier.height(8.dp))
        if (hiddenApps.isEmpty()) {
            SettingsEmptyText("No hidden cards.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                hiddenApps.take(6).forEach { app ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(app.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = cream)
                        OutlinedButton(
                            onClick = { onShowCard(app.id, true) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                            border = BorderStroke(1.dp, Color(0x44C8C1B3))
                        ) {
                            Text("Show", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onResetDemoCards,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
            border = BorderStroke(1.dp, spark.copy(alpha = 0.55f))
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, tint = spark)
            Spacer(Modifier.width(8.dp))
            Text("Reset demo cards", fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun AssistantSettingsSection(assistantEnabled: Boolean, onAssistant: (Boolean) -> Unit) {
    PremiumCard(accent = signalPurple) {
        SettingsSectionHeader(Icons.Filled.AutoAwesome, "Assistant", "Local guidance only in this build.")
        Spacer(Modifier.height(12.dp))
        SettingsInlineToggle(
            title = "Assistant enabled",
            body = "Show the local Smithware Companion screen and suggestions.",
            checked = assistantEnabled,
            onChange = onAssistant
        )
        Spacer(Modifier.height(12.dp))
        PrivacyPoint("Local-only mode: responses use demo summaries and visible cards.")
        PrivacyPoint("Future AI connection: OpenAI can be added later with clear consent.")
    }
}

@Composable
private fun FuturePremiumSettingsSection(onLockedFeature: (PremiumFeature) -> Unit) {
    PremiumCard(accent = ember) {
        SettingsSectionHeader(Icons.Filled.FlashOn, "Future Pro Features", "Prepared for later upgrades. No billing is active.")
        Spacer(Modifier.height(14.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PremiumFeature.entries.forEach { feature ->
                LockedPremiumFeatureRow(feature = feature, onClick = { onLockedFeature(feature) })
            }
        }
    }
}

@Composable
private fun LockedPremiumFeatureRow(feature: PremiumFeature, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(Color(0x22252A30))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(feature.color.copy(alpha = 0.16f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(feature.icon, contentDescription = null, tint = feature.color, modifier = Modifier.size(25.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(feature.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = cream)
            Text(feature.shortDescription, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        Spacer(Modifier.width(10.dp))
        Text("Pro", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = spark)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PremiumFeatureSheet(feature: PremiumFeature, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15181B),
        contentColor = cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(feature.color.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(feature.icon, contentDescription = null, tint = feature.color, modifier = Modifier.size(34.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Coming Soon / Pro Feature", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = spark)
                    Text(feature.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = cream)
                }
            }
            Text(feature.longDescription, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            PremiumCard(accent = feature.color) {
                PrivacyPoint("No billing is connected yet.")
                Spacer(Modifier.height(8.dp))
                PrivacyPoint("No subscription is required for the current local features.")
                Spacer(Modifier.height(8.dp))
                PrivacyPoint("This is a preview of a possible future release.")
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = spark, contentColor = Color(0xFF1B0B00))
            ) {
                Text("Got it", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PrivacySettingsScreen(onDeleteLocalData: () -> Unit) {
    PremiumCard(accent = calmGreen) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .background(calmGreen.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PrivacyTip, contentDescription = null, tint = calmGreen, modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Privacy / Local-First", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = cream)
                Text("Clear controls for what stays on this device.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(14.dp))
        FlowChips(listOf("No cloud sync", "No hidden monitoring", "Delete local data"))
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PrivacyPoint("Data stays local in v1.")
            PrivacyPoint("No hidden monitoring")
            PrivacyPoint("No microphone use unless a future feature clearly asks")
            PrivacyPoint("No location tracking")
            PrivacyPoint("No cloud sync in v1")
            PrivacyPoint("You control which cards appear")
            PrivacyPoint("You can delete local data")
        }
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = Color(0x334D535A))
        Spacer(Modifier.height(16.dp))
        Text("Permissions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
        Spacer(Modifier.height(8.dp))
        Text(
            "Command Hub will explain why before asking for any Android permission. This build does not request microphone, location, notifications, accessibility, or background monitoring permissions.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onDeleteLocalData,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 54.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
            border = BorderStroke(1.dp, Color(0x66FF6257))
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, tint = Color(0xFFFF6257))
            Spacer(Modifier.width(8.dp))
            Text("Delete local data", fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun AboutSettingsSection() {
    PremiumCard(accent = metallic) {
        SettingsSectionHeader(Icons.Filled.FlashOn, "About", "Smithware Studios command software.")
        Spacer(Modifier.height(14.dp))
        SettingsInfoRow("Studio", "Smithware Studios")
        SettingsInfoRow("App version", "0.1.0")
        SettingsInfoRow("Brand", "Premium local-first tools for managing the day from one hub.")
        SettingsInfoRow("Feedback", "Feedback link can be added when the public channel is ready.")
    }
}

@Composable
private fun SettingsSectionHeader(icon: ImageVector, title: String, body: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(spark.copy(alpha = 0.14f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = spark, modifier = Modifier.size(30.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
            Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SettingsInlineToggle(title: String, body: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
            Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun SettingsAppOrderRow(app: ConnectedAppCardModel, onMoveCard: (String, Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(app.displayIcon(), contentDescription = null, tint = app.category.color, modifier = Modifier.size(24.dp))
        Text(app.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = cream, maxLines = 1, overflow = TextOverflow.Ellipsis)
        OutlinedButton(
            onClick = { onMoveCard(app.id, 1) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
            border = BorderStroke(1.dp, Color(0x44C8C1B3))
        ) {
            Text("Up", fontWeight = FontWeight.Bold)
        }
        OutlinedButton(
            onClick = { onMoveCard(app.id, -1) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
            border = BorderStroke(1.dp, Color(0x44C8C1B3))
        ) {
            Text("Down", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SettingsInfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = metallic)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = cream)
    }
}

@Composable
private fun SettingsEmptyText(text: String) {
    Text(text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun AppDetailScreen(
    app: ConnectedAppCardModel,
    alerts: List<CommandAlert>,
    onBack: () -> Unit,
    onToggleHub: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val launchIntent = app.packageName?.let { packageName ->
        context.packageManager.getLaunchIntentForPackage(packageName)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0x33252A30), RoundedCornerShape(8.dp))
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = cream)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(app.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                    Text(app.category.label, style = MaterialTheme.typography.bodyLarge, color = app.category.color)
                }
            }
        }
        item { AppDetailHero(app) }
        item {
            DetailSectionCard("Today's Summary") {
                Text(app.todaySummary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            DetailSectionCard("Alerts") {
                if (alerts.isEmpty()) {
                    InlineEmptyState("No active alerts for this app.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        alerts.forEach { alert -> CompactAlertRow(alert) }
                    }
                }
            }
        }
        item {
            DetailSectionCard("Recent Activity") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (app.recentActivity.isEmpty()) {
                        InlineEmptyState("No recent activity stored yet.")
                    } else {
                        app.recentActivity.forEach { activity -> ActivityRow(activity, app.category.color) }
                    }
                }
            }
        }
        item {
            PremiumCard {
                AppLaunchRow(app, launchIntent)
                if (app.packageName != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(app.packageName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            DetailSectionCard("App Settings") {
                AppSettingToggle(
                    title = "Show on Hub dashboard",
                    body = "Include this card in the Connected Life Apps grid.",
                    checked = app.showOnHub,
                    onChange = onToggleHub
                )
                Spacer(Modifier.height(12.dp))
                StaticSettingRow("Sample alerts", "Show local sample alerts and reminders for this connected app.", true)
                Spacer(Modifier.height(12.dp))
                StaticSettingRow("Local summary mode", "Use short summaries instead of raw app data.", true)
            }
        }
    }
}

@Composable
private fun AppDetailHero(app: ConnectedAppCardModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, app.cardAccent().copy(alpha = 0.42f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(app.category.color.copy(alpha = 0.30f), Color(0xFF171A1E), Color(0xFF2A2117))
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(66.dp)
                            .background(app.category.color.copy(alpha = 0.20f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(app.shortCode, style = MaterialTheme.typography.titleLarge, color = app.category.color, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(app.status, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                        Text(app.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                FlowChips(app.metrics + app.category.label)
            }
        }
    }
}

@Composable
private fun DetailSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    PremiumCard {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun CompactAlertRow(alert: CommandAlert) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (alert.priority == AlertPriority.Low) Icons.Filled.Notifications else Icons.Filled.Warning,
            contentDescription = null,
            tint = alert.priority.color,
            modifier = Modifier.size(26.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(alert.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(alert.description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ActivityRow(activity: String, color: Color) {
    Row(verticalAlignment = Alignment.Top) {
        Box(Modifier.padding(top = 8.dp).size(8.dp).background(color, CircleShape))
        Spacer(Modifier.width(10.dp))
        Text(activity, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun StaticSettingRow(title: String, body: String, checked: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = {})
    }
}

@Composable
private fun AppSettingToggle(title: String, body: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun SmithwareLogoHeader(activeAlerts: Int) {
    val dateLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault()))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0x44FF8A1C))
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF2E2419), Color(0xFF171A1E), Color(0xFF302A21))
                    )
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.smithware_central_logo),
                        contentDescription = "Smithware Command Hub logo",
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Smithware Command Hub",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = cream
                        )
                        Text(
                            "Good morning, Kyle • $dateLabel",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = metallic
                        )
                    }
                }
                Text(
                    "Your day at a glance",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    HeaderPill("Command ready", Icons.Filled.FlashOn, Modifier.weight(1f))
                    HeaderPill("${activeAlerts} alerts", Icons.Filled.Notifications, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun HeaderPill(text: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color(0x33252A30), RoundedCornerShape(8.dp))
            .heightIn(min = 48.dp)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = spark, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = cream)
    }
}

@Composable
private fun ScreenshotHero(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    chips: List<String> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.42f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(accent.copy(alpha = 0.26f), Color(0xFF171A1E), Color(0xFF261B10))
                    )
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(accent.copy(alpha = 0.20f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(38.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = cream)
                        Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                if (chips.isNotEmpty()) {
                    FlowChips(chips)
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
    icon: ImageVector,
    title: String,
    body: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    PremiumCard(accent = spark) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(spark.copy(alpha = 0.16f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = spark, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
                    Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (actionLabel != null && onAction != null) {
                Button(
                    onClick = onAction,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = spark, contentColor = Color(0xFF1B0B00))
                ) {
                    Text(actionLabel, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
private fun NotInstalledStateCard(
    app: ConnectedAppCardModel,
    compact: Boolean = false,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    if (compact) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 44.dp)
                .background(Color(0x30252A30), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Apps, contentDescription = null, tint = metallic.copy(alpha = 0.86f), modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Not installed", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = metallic.copy(alpha = 0.9f))
        }
        return
    }
    PremiumCard(accent = metallic) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(metallic.copy(alpha = 0.14f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Apps, contentDescription = null, tint = metallic, modifier = Modifier.size(28.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("This Smithware app is not installed yet.", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = cream)
                    Text("Install it or keep this card as a manual tracker.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(app.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = app.category.color)
                }
            }
            if (actionLabel != null && onAction != null) {
                OutlinedButton(
                    onClick = onAction,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                    border = BorderStroke(1.dp, spark.copy(alpha = 0.55f))
                ) {
                    Text(actionLabel, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
private fun InlineEmptyState(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x22252A30), RoundedCornerShape(8.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = calmGreen, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun premiumFilterChipColors(selectedColor: Color = spark) = FilterChipDefaults.filterChipColors(
    containerColor = Color(0x221B1E22),
    labelColor = cream,
    selectedContainerColor = selectedColor.copy(alpha = 0.22f),
    selectedLabelColor = cream
)

@Composable
private fun TodaysCommandBriefCard(
    items: List<CommandBriefItem>,
    dailyBriefEnabled: Boolean,
    onDailyBrief: (Boolean) -> Unit,
    onOpenBrief: () -> Unit
) {
    PremiumCard(accent = spark) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(spark.copy(alpha = 0.16f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.FlashOn, contentDescription = null, tint = spark, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Today's Command Brief", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                Text("High-level local overview", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = dailyBriefEnabled, onCheckedChange = onDailyBrief)
        }
        Spacer(Modifier.height(18.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items.take(5).forEach { item ->
                CommandBriefRow(item)
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onOpenBrief,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = spark, contentColor = Color(0xFF1B0B00))
        ) {
            Text("Open daily brief", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun CommandBriefRow(item: CommandBriefItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x24252A30), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(item.color.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cream)
            Text(item.body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun QuickActionsRow() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            QuickActionCard("Add reminder", "Create note", Icons.Filled.Notifications, spark, Modifier.weight(1f))
            QuickActionCard("Open assistant", "Ask hub", Icons.Filled.AutoAwesome, signalPurple, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            QuickActionCard("Add app card", "Starter card", Icons.Filled.Apps, calmGreen, Modifier.weight(1f))
            QuickActionCard("View all alerts", "Review list", Icons.Filled.Warning, ember, Modifier.weight(1f))
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(112.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = graphiteRaised.copy(alpha = 0.94f)),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(30.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ConnectedLifeAppsGrid(apps: List<HubLifeAppCard>, onAppSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        apps.chunked(2).forEach { rowApps ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                rowApps.forEach { app ->
                    LifeAppTile(app = app, onAppSelected = onAppSelected, modifier = Modifier.weight(1f))
                }
                if (rowApps.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun LifeAppTile(
    app: HubLifeAppCard,
    onAppSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(min = 158.dp)
            .clickable(enabled = app.appId != null) { app.appId?.let(onAppSelected) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xF21B1E22)),
        border = BorderStroke(1.dp, app.color.copy(alpha = 0.30f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(app.color.copy(alpha = 0.20f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(app.icon, contentDescription = null, tint = app.color, modifier = Modifier.size(26.dp))
                }
                Spacer(Modifier.width(10.dp))
                Icon(
                    if (app.urgent) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = if (app.urgent) ember else calmGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                app.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                app.status,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                app.metric,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = app.color
            )
        }
    }
}

@Composable
private fun LargeFeaturedAppCard(
    app: ConnectedAppCardModel,
    alertCount: Int,
    onAppSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val launchIntent = app.packageName?.let { context.packageManager.getLaunchIntentForPackage(it) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppSelected(app.id) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, app.category.color.copy(alpha = 0.42f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(app.cardAccent().copy(alpha = 0.28f), Color(0xF21B1E22), Color(0xFF20170F))
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppCardIconBadge(app, 58.dp)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(app.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
                        Text(app.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    AppInfoPill("${alertCount} alerts", Icons.Filled.Warning, if (alertCount > 0) ember else calmGreen, Modifier.weight(1f))
                    AppInfoPill(lastUpdatedStatus(app), Icons.Filled.CheckCircle, metallic, Modifier.weight(1f))
                    AppInfoPill(if (launchIntent != null) "Installed" else "Not installed", Icons.Filled.Apps, if (launchIntent != null) calmGreen else ember, Modifier.weight(1f))
                }
                AppLaunchRow(app, launchIntent)
            }
        }
    }
}

@Composable
private fun CompactAppGrid(
    apps: List<ConnectedAppCardModel>,
    alerts: List<CommandAlert>,
    onAppSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        apps.chunked(2).forEach { rowApps ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                rowApps.forEach { app ->
                    CompactGridAppCard(
                        app = app,
                        alertCount = alerts.alertCountFor(app),
                        onAppSelected = onAppSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowApps.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CompactGridAppCard(
    app: ConnectedAppCardModel,
    alertCount: Int,
    onAppSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val launchIntent = app.packageName?.let { context.packageManager.getLaunchIntentForPackage(it) }
    Card(
        modifier = modifier
            .heightIn(min = 226.dp)
            .clickable { onAppSelected(app.id) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xF21B1E22)),
        border = BorderStroke(1.dp, app.cardAccent().copy(alpha = 0.28f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppCardIconBadge(app, 44.dp)
                Spacer(Modifier.width(8.dp))
                ConnectedAppAlertCard(alertCount)
            }
            Text(app.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = cream, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(app.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(lastUpdatedStatus(app), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = metallic)
            Text(
                if (launchIntent != null) "Installed" else "Not installed",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (launchIntent != null) calmGreen else ember
            )
            AppLaunchRow(app, launchIntent, compact = true)
        }
    }
}

@Composable
private fun ConnectedAppAlertCard(alertCount: Int) {
    val color = if (alertCount > 0) ember else calmGreen
    Row(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 9.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (alertCount > 0) Icons.Filled.Warning else Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(alertCount.toString(), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = cream)
    }
}

@Composable
private fun DisabledNotInstalledCard(compact: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (compact) 42.dp else 48.dp)
            .background(Color(0x30252A30), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Warning, contentDescription = null, tint = metallic.copy(alpha = 0.72f), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("Not installed", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = metallic.copy(alpha = 0.86f))
    }
}

@Composable
private fun AppLaunchRow(
    app: ConnectedAppCardModel,
    launchIntent: Intent?,
    compact: Boolean = false
) {
    val context = LocalContext.current
    if (launchIntent == null) {
        if (!compact) {
            NotInstalledStateCard(
                app = app,
                actionLabel = if (app.fallbackPlayStoreUrl.isNullOrBlank()) "Keep manual tracker" else "Get app",
                onAction = {
                    if (app.fallbackPlayStoreUrl.isNullOrBlank()) {
                        Toast.makeText(context, "This card will stay available as a manual tracker.", Toast.LENGTH_SHORT).show()
                    } else {
                        openFallbackStoreLink(context, app.fallbackPlayStoreUrl)
                    }
                }
            )
            return
        }
        if (app.fallbackPlayStoreUrl.isNullOrBlank()) {
            NotInstalledStateCard(app = app, compact = true)
        } else {
            OutlinedButton(
                onClick = { openFallbackStoreLink(context, app.fallbackPlayStoreUrl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = if (compact) 44.dp else 50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                border = BorderStroke(1.dp, spark.copy(alpha = 0.55f))
            ) {
                Icon(Icons.Filled.Apps, contentDescription = null, tint = spark, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Get app", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
            }
        }
        return
    }
    Button(
        onClick = { launchInstalledApp(context, app, launchIntent) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (compact) 44.dp else 50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = spark,
            contentColor = Color(0xFF1B0B00)
        )
    ) {
        Icon(Icons.Filled.Apps, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("Open", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun AppIconBadge(category: AppCategory, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(category.color.copy(alpha = 0.20f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(category.icon, contentDescription = null, tint = category.color, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
private fun AppCardIconBadge(app: ConnectedAppCardModel, size: androidx.compose.ui.unit.Dp) {
    val accent = app.cardAccent()
    Box(
        modifier = Modifier
            .size(size)
            .background(accent.copy(alpha = 0.20f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(app.displayIcon(), contentDescription = null, tint = accent, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
private fun AppInfoPill(text: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.13f), RoundedCornerShape(8.dp))
            .heightIn(min = 44.dp)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(7.dp))
        Text(text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = cream, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun HubAlertsSection(alerts: List<CommandAlert>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        alerts.forEach { alert ->
            HubAlertCard(alert)
        }
    }
}

@Composable
private fun HubAlertCard(alert: CommandAlert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xF21B1E22)),
        border = BorderStroke(1.dp, alert.priority.color.copy(alpha = 0.30f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(alert.priority.color.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (alert.priority == AlertPriority.Low) Icons.Filled.Notifications else Icons.Filled.Warning,
                    contentDescription = null,
                    tint = alert.priority.color,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(alert.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(alert.description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun AssistantShortcutCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, signalPurple.copy(alpha = 0.36f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF211733), Color(0xFF1A1D21), Color(0xFF2D1C10))
                    )
                )
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(signalPurple.copy(alpha = 0.22f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = signalPurple, modifier = Modifier.size(34.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Smithware Companion", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    Text(
                        "Ask what needs attention across chores, renewals, projects, recordings, and wellness cards.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroCommandCard(
    appCount: Int,
    activeAlerts: Int,
    summary: DailySummary,
    dailyBriefEnabled: Boolean,
    onDailyBrief: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF2A2117), Color(0xFF191C20), Color(0xFF34302A))
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.smithware_central_logo),
                        contentDescription = "Smithware Command Hub logo",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        AssistChip(onClick = {}, label = { Text("Smithware Studios") })
                        Text("Smithware Command Hub", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Text(summary.headline, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LinearProgressIndicator(
                    progress = { summary.readinessScore / 100f },
                    modifier = Modifier.fillMaxWidth(),
                    color = spark,
                    trackColor = Color(0x553A3A3A)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$appCount starter cards • $activeAlerts active alerts", modifier = Modifier.weight(1f), color = cream)
                    Switch(checked = dailyBriefEnabled, onCheckedChange = onDailyBrief)
                }
            }
        }
    }
}

@Composable
private fun ConnectedAppCard(app: ConnectedAppCardModel, onAppSelected: (String) -> Unit) {
    PremiumCard(
        modifier = Modifier
            .heightIn(min = 132.dp)
            .clickable { onAppSelected(app.id) },
        accent = app.cardAccent()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppCardIconBadge(app, 48.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(app.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(app.status, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                if (app.health == AppHealth.Ready) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                null,
                tint = if (app.health == AppHealth.Ready) calmGreen else ember
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(app.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(10.dp))
        FlowChips(app.metrics + app.category.label)
    }
}

@Composable
private fun DailySummaryCard(summary: DailySummary) {
    PremiumCard(accent = spark) {
        Text(summary.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text(summary.headline, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        summary.items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                Box(Modifier.size(7.dp).background(spark, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(item, style = MaterialTheme.typography.bodyLarge, color = cream)
            }
        }
    }
}

@Composable
private fun AlertCard(
    alert: CommandAlert,
    sourceApp: ConnectedAppCardModel?,
    onComplete: () -> Unit,
    onOpen: () -> Unit
) {
    PremiumCard(accent = alert.priority.color) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(alert.priority.color.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (alert.priority == AlertPriority.Low) Icons.Filled.Notifications else Icons.Filled.Warning,
                    contentDescription = null,
                    tint = alert.priority.color,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(alert.title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = cream)
                    Text(alert.priority.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = alert.priority.color)
                }
                Text(alert.description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                FlowChips(
                    listOfNotNull(
                        sourceApp?.name ?: alert.sourceAppId.takeIf { it.isNotBlank() },
                        alert.category.label,
                        formatDueTime(alert.dueDateTime)
                    )
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    if (!alert.isCompleted) {
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = spark, contentColor = Color(0xFF1B0B00))
                        ) {
                            Text("Mark complete", fontWeight = FontWeight.Bold)
                        }
                    }
                    OutlinedButton(
                        onClick = onOpen,
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                        enabled = alert.sourceAppId.isNotBlank(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = cream),
                        border = BorderStroke(1.dp, Color(0x55C8C1B3))
                    ) {
                        Text("Open card", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsToggle(title: String, body: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}

@Composable
private fun ScreenHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = cream)
        Box(Modifier.size(width = 52.dp, height = 4.dp).background(spark, RoundedCornerShape(8.dp)))
        Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun MetricCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    PremiumCard(modifier = modifier) {
        Icon(icon, contentDescription = null, tint = spark)
        Spacer(Modifier.height(8.dp))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryChips(selected: AppCategory?, onCategory: (AppCategory?) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = selected == null,
            onClick = { onCategory(null) },
            label = { Text("All", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
            colors = premiumFilterChipColors(),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selected == null,
                borderColor = Color(0x44C8C1B3),
                selectedBorderColor = spark
            )
        )
        AppCategory.entries.forEach { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onCategory(category) },
                label = { Text(category.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                colors = premiumFilterChipColors(selectedColor = category.color),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected == category,
                    borderColor = category.color.copy(alpha = 0.28f),
                    selectedBorderColor = category.color
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowChips(labels: List<String>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        labels.forEach { label ->
            AssistChip(
                onClick = {},
                label = { Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0x26252A30), labelColor = cream),
                border = BorderStroke(1.dp, Color(0x33C8C1B3))
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.size(width = 5.dp, height = 24.dp).background(spark, RoundedCornerShape(8.dp)))
        Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = cream)
    }
}

@Composable
private fun PremiumCard(
    modifier: Modifier = Modifier,
    accent: Color = metallic,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xF21B1E22)),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.26f))
    ) {
        Column(Modifier.padding(18.dp), content = content)
    }
}

private class CommandHubViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CommandHubRepository(application)
    private val _state = MutableStateFlow(commandHubDemoState().copy(apps = emptyList(), alerts = emptyList()))
    val state: StateFlow<CommandHubState> = _state

    init {
        val demoState = commandHubDemoState()
        viewModelScope.launch {
            val demoApps = demoState.apps.map { it.toEntity() }
            repository.seedIfEmpty(
                apps = demoApps,
                alerts = demoState.alerts.map { it.toEntity() },
                assistantSummaries = demoState.assistantSummaries.map { it.toEntity() }
            )
            repository.syncBuiltInAppCatalog(demoApps)
            repository.syncDemoAlerts(demoState.alerts.map { it.toEntity() })
        }
        viewModelScope.launch {
            combine(
                repository.apps,
                repository.alerts,
                repository.assistantSummaries,
                repository.settings
            ) { apps, alerts, assistantSummaries, settings ->
                PersistentHubData(apps, alerts, assistantSummaries, settings)
            }.collect { data ->
                _state.update { current ->
                    current.copy(
                        apps = data.apps.map { it.toModel() },
                        alerts = data.alerts.map { it.toModel() },
                        assistantSummaries = data.assistantSummaries.map { it.toModel() },
                        assistantEnabled = data.settings.assistantEnabled,
                        dailyBriefEnabled = data.settings.dailyBriefEnabled,
                        offlineMode = data.settings.offlineMode,
                        demoMode = data.settings.demoMode,
                        premiumThemeEnabled = data.settings.premiumThemeEnabled,
                        themeMode = data.settings.themeMode,
                        accentIntensity = data.settings.accentIntensity,
                        compactDashboard = data.settings.compactDashboard,
                        onboardingComplete = data.settings.onboardingComplete,
                        selectedDashboardAreas = data.settings.selectedDashboardAreas
                    )
                }
            }
        }
    }

    fun selectTab(tab: CommandTab) = _state.update { it.copy(selectedTab = tab, selectedAppId = null) }
    fun selectCategory(category: AppCategory?) = _state.update { it.copy(selectedCategory = category) }
    fun selectApp(appId: String) = _state.update { it.copy(selectedAppId = appId) }
    fun clearSelectedApp() = _state.update { it.copy(selectedAppId = null) }
    fun openDailyBrief() = _state.update { it.copy(showDailyBrief = true, selectedAppId = null) }
    fun closeDailyBrief() = _state.update { it.copy(showDailyBrief = false) }
    fun refreshDailyBrief() = _state.update { it.copy(dailyBriefRefreshCount = it.dailyBriefRefreshCount + 1) }
    fun toggleAssistant(enabled: Boolean) = viewModelScope.launch { repository.updateAssistantEnabled(enabled) }
    fun toggleDailyBrief(enabled: Boolean) = viewModelScope.launch { repository.updateDailyBriefEnabled(enabled) }
    fun toggleOfflineMode(enabled: Boolean) = viewModelScope.launch { repository.updateOfflineMode(enabled) }
    fun toggleDemoMode(enabled: Boolean) = viewModelScope.launch { repository.updateDemoMode(enabled) }
    fun togglePremiumTheme(enabled: Boolean) = viewModelScope.launch { repository.updatePremiumTheme(enabled) }
    fun updateThemeMode(mode: String) = viewModelScope.launch { repository.updateThemeMode(mode) }
    fun updateAccentIntensity(intensity: String) = viewModelScope.launch { repository.updateAccentIntensity(intensity) }
    fun toggleCompactDashboard(enabled: Boolean) = viewModelScope.launch { repository.updateCompactDashboard(enabled) }
    fun toggleAppOnHub(appId: String, enabled: Boolean) = viewModelScope.launch {
        repository.setAppHubVisibility(appId, enabled)
    }
    fun toggleAlertCompleted(alertId: String, completed: Boolean) = viewModelScope.launch {
        repository.setAlertCompleted(alertId, completed)
    }
    fun addCustomCard(draft: CardCustomizationDraft) = viewModelScope.launch {
        val trimmedName = draft.name.trim()
        if (trimmedName.isBlank()) return@launch
        val now = System.currentTimeMillis()
        val nextOrder = (_state.value.apps.maxOfOrNull { it.sortOrder } ?: 0) + 10
        val card = ConnectedAppCardModel(
            id = "custom_$now",
            name = trimmedName,
            shortCode = trimmedName.toShortCode(),
            category = draft.category,
            status = "Custom card ready",
            summary = draft.summary.ifBlank { "Custom Smithware life card." },
            todaySummary = draft.summary.ifBlank { "This custom card is ready on your local dashboard." },
            metrics = listOf("Custom", draft.category.label),
            recentActivity = listOf("Custom card created locally."),
            priority = nextOrder,
            health = AppHealth.Ready,
            showOnHub = true,
            isCustom = true,
            packageName = draft.packageName.trim().ifBlank { null },
            fallbackPlayStoreUrl = draft.fallbackPlayStoreUrl.trim().ifBlank { null },
            iconKey = draft.iconKey,
            accentColor = draft.accentColor.toStoredLong(),
            sortOrder = nextOrder
        )
        repository.addCustomApp(card.toEntity())
    }
    fun updateCardCustomization(app: ConnectedAppCardModel, draft: CardCustomizationDraft) = viewModelScope.launch {
        val trimmedName = draft.name.trim()
        if (trimmedName.isBlank()) return@launch
        repository.updateCardCustomization(
            app.copy(
                name = trimmedName,
                shortCode = trimmedName.toShortCode(),
                category = draft.category,
                summary = draft.summary.ifBlank { app.summary },
                todaySummary = draft.summary.ifBlank { app.todaySummary },
                packageName = draft.packageName.trim().ifBlank { null },
                fallbackPlayStoreUrl = draft.fallbackPlayStoreUrl.trim().ifBlank { null },
                iconKey = draft.iconKey,
                accentColor = draft.accentColor.toStoredLong()
            ).toEntity()
        )
    }
    fun moveDashboardCard(appId: String, direction: Int) = viewModelScope.launch {
        val app = _state.value.apps.firstOrNull { it.id == appId } ?: return@launch
        repository.updateAppSortOrder(appId, app.sortOrder + (direction * 10))
    }
    fun deleteCustomCard(appId: String) = viewModelScope.launch {
        repository.deleteCustomApp(appId)
    }
    fun resetDemoCards() = viewModelScope.launch {
        val demoState = commandHubDemoState()
        repository.resetDemoCards(
            apps = demoState.apps.map { it.toEntity() },
            alerts = demoState.alerts.map { it.toEntity() },
            assistantSummaries = demoState.assistantSummaries.map { it.toEntity() }
        )
    }
    fun completeOnboarding(selectedAreas: Set<String>) = viewModelScope.launch {
        repository.completeOnboarding(selectedAreas)
    }
    fun deleteLocalData() = viewModelScope.launch {
        repository.deleteLocalData()
    }
}

private data class PersistentHubData(
    val apps: List<ConnectedAppEntity>,
    val alerts: List<AlertEntity>,
    val assistantSummaries: List<AssistantSummaryEntity>,
    val settings: HubSettings
)

private data class CommandHubState(
    val selectedTab: CommandTab = CommandTab.Hub,
    val selectedCategory: AppCategory? = null,
    val selectedAppId: String? = null,
    val showDailyBrief: Boolean = false,
    val dailyBriefRefreshCount: Int = 0,
    val apps: List<ConnectedAppCardModel>,
    val alerts: List<CommandAlert>,
    val dailySummary: DailySummary,
    val assistantSummaries: List<AssistantSummary> = emptyList(),
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

private data class ConnectedAppCardModel(
    val id: String,
    val name: String,
    val shortCode: String,
    val category: AppCategory,
    val status: String,
    val summary: String,
    val todaySummary: String,
    val metrics: List<String>,
    val recentActivity: List<String>,
    val priority: Int,
    val health: AppHealth,
    val showOnHub: Boolean = true,
    val isCustom: Boolean = false,
    val packageName: String? = null,
    val fallbackPlayStoreUrl: String? = null,
    val iconKey: String = "apps",
    val accentColor: Long? = null,
    val sortOrder: Int = priority
)

private data class CardCustomizationDraft(
    val name: String,
    val summary: String,
    val category: AppCategory,
    val iconKey: String,
    val accentColor: Color,
    val packageName: String = "",
    val fallbackPlayStoreUrl: String = ""
)

private data class CardIconOption(val key: String, val label: String, val icon: ImageVector)

private data class CardColorOption(val label: String, val color: Color)

private data class CommandAlert(
    val id: String,
    val title: String,
    val description: String,
    val category: AppCategory,
    val priority: AlertPriority,
    val sourceAppId: String,
    val dueDateTime: Long? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

private data class DailySummary(
    val title: String,
    val headline: String,
    val readinessScore: Int,
    val items: List<String>
)

private data class AssistantSummary(
    val id: String,
    val title: String,
    val body: String,
    val createdLabel: String
)

private data class CommandBriefItem(
    val title: String,
    val body: String,
    val icon: ImageVector,
    val color: Color
)

private data class DailyBriefSection(
    val title: String,
    val subtitle: String,
    val items: List<CommandBriefItem>,
    val icon: ImageVector,
    val color: Color,
    val actionLabel: String? = null,
    val actionAppId: String? = null,
    val opensAlerts: Boolean = false
)

private data class HubLifeAppCard(
    val title: String,
    val status: String,
    val metric: String,
    val icon: ImageVector,
    val color: Color,
    val urgent: Boolean = false,
    val appId: String? = null
)

private data class AssistantResponseCard(
    val title: String,
    val body: String,
    val icon: ImageVector,
    val color: Color
)

private enum class PremiumFeature(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val icon: ImageVector,
    val color: Color,
    val isLocked: Boolean = true
) {
    UnlimitedConnectedApps(
        title = "Unlimited connected apps",
        shortDescription = "Add more life areas and app cards when the hub grows.",
        longDescription = "Future versions can lift card limits for heavier Smithware setups while keeping the current demo cards available.",
        icon = Icons.Filled.Apps,
        color = steelBlue
    ),
    AiAssistant(
        title = "AI assistant",
        shortDescription = "Upgrade the local assistant with user-approved AI help.",
        longDescription = "A future AI assistant can summarize approved local context and answer questions without hidden monitoring.",
        icon = Icons.Filled.AutoAwesome,
        color = signalPurple
    ),
    CloudBackup(
        title = "Cloud backup",
        shortDescription = "Optional backup for cards, alerts, and settings.",
        longDescription = "Cloud backup is planned as an opt-in feature. The current MVP stays local-first and does not sync data.",
        icon = Icons.Filled.PrivacyTip,
        color = calmGreen
    ),
    AdvancedAutomations(
        title = "Advanced automations",
        shortDescription = "User-approved rules for reminders and repeated routines.",
        longDescription = "Future automations can help with recurring reminders, alert rules, and routine follow-ups after the user turns them on.",
        icon = Icons.Filled.FlashOn,
        color = spark
    ),
    CustomThemes(
        title = "Custom themes",
        shortDescription = "More Smithware color systems and dashboard styles.",
        longDescription = "Custom themes can expand beyond the current premium graphite look while keeping the interface readable.",
        icon = Icons.Filled.Settings,
        color = metallic
    ),
    Widgets(
        title = "Widgets",
        shortDescription = "Home screen snapshots for alerts and daily briefs.",
        longDescription = "Widgets can bring the command brief, urgent alerts, and quick card status to the Android home screen later.",
        icon = Icons.Filled.Dashboard,
        color = ember
    ),
    ExportReports(
        title = "Export reports",
        shortDescription = "Save summaries from alerts, cards, and activity.",
        longDescription = "Report exports can turn local card summaries into shareable files in a later release.",
        icon = Icons.Filled.CheckCircle,
        color = calmGreen
    ),
    CrossAppSync(
        title = "Cross-app sync",
        shortDescription = "Keep approved Smithware apps aligned with the hub.",
        longDescription = "Cross-app sync is planned for deeper Smithware integrations after clear user permission and app-side support.",
        icon = Icons.Filled.Notifications,
        color = steelBlue
    )
}

private enum class CommandTab(val label: String, val icon: ImageVector) {
    Hub("Hub", Icons.Filled.Dashboard),
    Apps("Apps", Icons.Filled.Apps),
    Assistant("Assistant", Icons.Filled.AutoAwesome),
    Alerts("Alerts", Icons.Filled.Notifications),
    Settings("Settings", Icons.Filled.Settings)
}

private enum class AppCategory(val label: String, val color: Color, val icon: ImageVector) {
    Health("Health", healthRed, Icons.Filled.MonitorHeart),
    Fitness("Fitness", fitnessViolet, Icons.Filled.FitnessCenter),
    Diet("Diet", dietGreen, Icons.Filled.Restaurant),
    Work("Work", steelBlue, Icons.Filled.Work),
    Tasks("Tasks", tasksBlue, Icons.Filled.Checklist),
    Chores("Chores", choresTeal, Icons.Filled.CleaningServices),
    Family("Family", familyLavender, Icons.Filled.Groups),
    Finance("Finance", financeGold, Icons.Filled.AttachMoney),
    Files("Files", filesGraphite, Icons.Filled.Folder),
    Reminders("Reminders", spark, Icons.Filled.Notifications),
    Projects("Projects", projectsCopper, Icons.Filled.Construction),
    Other("Other", metallic, Icons.Filled.Apps)
}

private enum class AppHealth { Ready, Attention }

private enum class AlertPriority(val label: String, val color: Color, val rank: Int) {
    High("High", Color(0xFFFF6257), 0),
    Medium("Medium", ember, 1),
    Low("Low", steelBlue, 2)
}

private enum class AlertListTab(val label: String) {
    Today("Today"),
    Upcoming("Upcoming"),
    Completed("Completed"),
    All("All")
}

private fun cardIconOptions() = listOf(
    CardIconOption("apps", "Apps", Icons.Filled.Apps),
    CardIconOption("health", "Health", AppCategory.Health.icon),
    CardIconOption("fitness", "Fitness", AppCategory.Fitness.icon),
    CardIconOption("diet", "Diet", AppCategory.Diet.icon),
    CardIconOption("work", "Work", AppCategory.Work.icon),
    CardIconOption("tasks", "Tasks", AppCategory.Tasks.icon),
    CardIconOption("chores", "Chores", AppCategory.Chores.icon),
    CardIconOption("family", "Family", AppCategory.Family.icon),
    CardIconOption("finance", "Finance", AppCategory.Finance.icon),
    CardIconOption("files", "Files", AppCategory.Files.icon),
    CardIconOption("projects", "Projects", AppCategory.Projects.icon),
    CardIconOption("spark", "Spark", Icons.Filled.AutoAwesome),
    CardIconOption("alert", "Alert", Icons.Filled.Warning),
    CardIconOption("bell", "Bell", AppCategory.Reminders.icon)
)

private fun cardColorOptions() = listOf(
    CardColorOption("Orange", spark),
    CardColorOption("Green", calmGreen),
    CardColorOption("Purple", signalPurple),
    CardColorOption("Blue", steelBlue),
    CardColorOption("Red", Color(0xFFE34B4B)),
    CardColorOption("Cream", metallic)
)

private fun iconForKey(key: String): ImageVector =
    cardIconOptions().firstOrNull { it.key == key }?.icon ?: Icons.Filled.Apps

private fun ConnectedAppCardModel.cardAccent(): Color =
    accentColor?.let { Color(it.toULong()) } ?: category.color

private fun ConnectedAppCardModel.displayIcon(): ImageVector =
    if (isCustom && iconKey != "apps") iconForKey(iconKey) else category.icon

private fun Color.toStoredLong(): Long = value.toLong()

private fun String.toShortCode(): String =
    trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
        .ifBlank { "CC" }

private fun categoryFromName(value: String): AppCategory = when (value) {
    "Home" -> AppCategory.Chores
    "Utility" -> AppCategory.Files
    "Studio" -> AppCategory.Projects
    "Money" -> AppCategory.Finance
    else -> AppCategory.entries.firstOrNull { it.name == value } ?: AppCategory.Other
}

private fun hubCommandBriefItems() = listOf(
    CommandBriefItem(
        title = "Tasks due today",
        body = "3 focus tasks and one build checklist need attention.",
        icon = AppCategory.Tasks.icon,
        color = AppCategory.Tasks.color
    ),
    CommandBriefItem(
        title = "Workout reminder",
        body = "Keep the next PivotFit session recovery-friendly.",
        icon = AppCategory.Fitness.icon,
        color = AppCategory.Fitness.color
    ),
    CommandBriefItem(
        title = "Chores pending",
        body = "5 TidyPilot chores are staged for the evening reset.",
        icon = AppCategory.Chores.icon,
        color = AppCategory.Chores.color
    ),
    CommandBriefItem(
        title = "Food and diet",
        body = "Use the high-priority fridge item before tomorrow.",
        icon = AppCategory.Diet.icon,
        color = AppCategory.Diet.color
    ),
    CommandBriefItem(
        title = "Bills and renewals",
        body = "2 demo renewals total $42.97 this week.",
        icon = AppCategory.Finance.icon,
        color = AppCategory.Finance.color
    )
)

private fun dailyCommandBriefSections(state: CommandHubState): List<DailyBriefSection> {
    val visibleApps = state.apps.filter { it.showOnHub }
    val activeAlerts = state.alerts.filter { !it.isCompleted }.sortedBy { it.priority.rank }
    val highPriorityAlerts = activeAlerts.filter { it.priority == AlertPriority.High }
    val workApps = visibleApps.filter { it.category == AppCategory.Work || it.category == AppCategory.Tasks || it.id.contains("work", ignoreCase = true) || it.id.contains("task", ignoreCase = true) }
    val healthApps = visibleApps.filter { it.category == AppCategory.Health || it.category == AppCategory.Fitness || it.id.contains("health", ignoreCase = true) }
    val foodApps = visibleApps.filter { it.category == AppCategory.Diet || it.id in setOf("fridgefinish", "platepilot") }
    val choreApps = visibleApps.filter { it.category == AppCategory.Chores || it.id.contains("chore", ignoreCase = true) }
    val financeApps = visibleApps.filter { it.category == AppCategory.Finance || it.id.contains("renewal", ignoreCase = true) }
    val recentlyUpdated = visibleApps.sortedByDescending { it.sortOrder }.take(3)

    val startItems = buildList {
        highPriorityAlerts.firstOrNull()?.let { alert ->
            add(alert.toBriefItem("High-priority alert"))
        }
        workApps.firstOrNull()?.let { app ->
            add(app.toBriefItem("Today tasks", AppCategory.Tasks.icon, AppCategory.Tasks.color))
        }
        if (isEmpty()) {
            add(CommandBriefItem("Start steady", "No critical item is blocking the day right now.", Icons.Filled.CheckCircle, calmGreen))
        }
    }

    val needsAttentionItems = buildList {
        highPriorityAlerts.take(3).forEach { add(it.toBriefItem("Needs attention")) }
        activeAlerts.filter { it.priority == AlertPriority.Medium }.take(2).forEach { add(it.toBriefItem("Upcoming attention")) }
        if (isEmpty()) {
            add(CommandBriefItem("No urgent alerts", "You're clear for now.", Icons.Filled.CheckCircle, calmGreen))
        }
    }

    val laterTodayItems = buildList {
        healthApps.firstOrNull()?.let { add(it.toBriefItem("Health and fitness", it.category.icon, it.category.color)) }
        foodApps.firstOrNull()?.let { add(it.toBriefItem("Food and diet", AppCategory.Diet.icon, AppCategory.Diet.color)) }
        choreApps.firstOrNull()?.let { add(it.toBriefItem("Chores", AppCategory.Chores.icon, AppCategory.Chores.color)) }
        financeApps.firstOrNull()?.let { add(it.toBriefItem("Finance and bills", AppCategory.Finance.icon, AppCategory.Finance.color)) }
        if (isEmpty()) {
            add(CommandBriefItem("Light afternoon", "No health, food, chore, or finance card is asking for attention.", Icons.Filled.CheckCircle, calmGreen))
        }
    }

    val niceToHandleItems = buildList {
        recentlyUpdated.forEach { app ->
            add(app.toBriefItem("Recently updated", app.category.icon, app.cardAccent()))
        }
        activeAlerts.filter { it.priority == AlertPriority.Low }.take(2).forEach { add(it.toBriefItem("Low priority")) }
        if (isEmpty()) {
            add(CommandBriefItem("Nothing extra queued", "Add cards or alerts when you want more to track.", Icons.Filled.Apps, steelBlue))
        }
    }

    val allClearItems = buildList {
        if (highPriorityAlerts.isEmpty()) {
            add(CommandBriefItem("High-priority alerts clear", "No high-priority local alerts are active.", Icons.Filled.CheckCircle, calmGreen))
        }
        if (visibleApps.isNotEmpty()) {
            add(CommandBriefItem("Cards are online", "${visibleApps.size} visible life cards are available on the dashboard.", Icons.Filled.Apps, steelBlue))
        } else {
            add(CommandBriefItem("No visible cards yet", "Add a life card to start building richer daily briefs.", Icons.Filled.Apps, metallic))
        }
    }

    return listOf(
        DailyBriefSection(
            title = "Start here",
            subtitle = "The first move for today.",
            items = startItems,
            icon = Icons.Filled.FlashOn,
            color = spark,
            actionLabel = workApps.firstOrNull()?.let { "Open ${it.name}" },
            actionAppId = workApps.firstOrNull()?.id
        ),
        DailyBriefSection(
            title = "Needs attention",
            subtitle = "Urgent and high-priority items first.",
            items = needsAttentionItems,
            icon = Icons.Filled.Warning,
            color = if (highPriorityAlerts.isNotEmpty()) Color(0xFFFF6257) else calmGreen,
            actionLabel = "View alerts",
            opensAlerts = true
        ),
        DailyBriefSection(
            title = "Later today",
            subtitle = "Health, food, chores, and bills to keep in view.",
            items = laterTodayItems,
            icon = Icons.Filled.Notifications,
            color = steelBlue,
            actionLabel = (healthApps + foodApps + choreApps + financeApps).firstOrNull()?.let { "Open ${it.name}" },
            actionAppId = (healthApps + foodApps + choreApps + financeApps).firstOrNull()?.id
        ),
        DailyBriefSection(
            title = "Nice to handle",
            subtitle = "Recently updated cards and lower-pressure items.",
            items = niceToHandleItems,
            icon = Icons.Filled.AutoAwesome,
            color = signalPurple,
            actionLabel = recentlyUpdated.firstOrNull()?.let { "Open ${it.name}" },
            actionAppId = recentlyUpdated.firstOrNull()?.id
        ),
        DailyBriefSection(
            title = "All clear",
            subtitle = "What looks settled right now.",
            items = allClearItems,
            icon = Icons.Filled.CheckCircle,
            color = calmGreen
        )
    )
}

private fun CommandAlert.toBriefItem(prefix: String): CommandBriefItem =
    CommandBriefItem(
        title = "$prefix: $title",
        body = description.ifBlank { "Review this alert when you have a minute." },
        icon = if (priority == AlertPriority.Low) Icons.Filled.Notifications else Icons.Filled.Warning,
        color = priority.color
    )

private fun ConnectedAppCardModel.toBriefItem(prefix: String, icon: ImageVector, color: Color): CommandBriefItem =
    CommandBriefItem(
        title = "$prefix: $name",
        body = todaySummary.ifBlank { summary },
        icon = icon,
        color = color
    )

private fun assistantSuggestedQuestions() = listOf(
    "What should I handle first?",
    "What am I forgetting?",
    "What needs attention today?",
    "Show my work items.",
    "Show my food and health items.",
    "Show my chores."
)

private fun localAssistantResponses(question: String, state: CommandHubState): List<AssistantResponseCard> {
    val activeAlerts = state.alerts.filter { !it.isCompleted }.sortedBy { it.priority.rank }
    val visibleApps = state.apps.filter { it.showOnHub }
    val workApps = visibleApps.filter { it.category == AppCategory.Work || it.category == AppCategory.Tasks }
    val foodHealthApps = visibleApps.filter { it.category == AppCategory.Health || it.category == AppCategory.Fitness || it.category == AppCategory.Diet || it.id in setOf("fridgefinish", "platepilot", "healthtracker") }
    val choreApps = visibleApps.filter { it.category == AppCategory.Chores || it.id == "choretracker" }

    if (visibleApps.isEmpty() && activeAlerts.isEmpty()) return emptyList()

    return when (question) {
        "What should I handle first?" -> {
            val firstAlert = activeAlerts.firstOrNull()
            listOf(
                if (firstAlert != null) {
                    AssistantResponseCard(
                        title = "Start with ${firstAlert.title.lowercase()}",
                        body = "Based on your connected cards, this looks like the most time-sensitive item: ${firstAlert.description}",
                        icon = Icons.Filled.Warning,
                        color = firstAlert.priority.color
                    )
                } else {
                    AssistantResponseCard(
                        title = "Nothing urgent right now.",
                        body = "Based on your connected cards, there are no active urgent demo alerts.",
                        icon = Icons.Filled.CheckCircle,
                        color = calmGreen
                    )
                }
            )
        }
        "What am I forgetting?" -> listOf(
            AssistantResponseCard(
                title = "Check reminders and follow-ups",
                body = "Based on your connected cards, review work follow-ups, the Renewal Radar watch list, and any chores pinned for tonight.",
                icon = Icons.Filled.Notifications,
                color = spark
            )
        )
        "Show my work items." -> workApps.toAssistantCards(
            emptyTitle = "No work items visible",
            emptyBody = "Nothing urgent right now from your visible work cards.",
            fallbackColor = steelBlue
        )
        "Show my food and health items." -> foodHealthApps.toAssistantCards(
            emptyTitle = "No food or health items visible",
            emptyBody = "Nothing urgent right now from your visible food and health cards.",
            fallbackColor = calmGreen
        )
        "Show my chores." -> choreApps.toAssistantCards(
            emptyTitle = "No chores visible",
            emptyBody = "Nothing urgent right now from your visible chore cards.",
            fallbackColor = calmGreen
        )
        else -> {
            if (activeAlerts.isEmpty()) {
                listOf(
                    AssistantResponseCard(
                        title = "Nothing urgent right now.",
                        body = "Based on your connected cards, there are no active urgent demo alerts.",
                        icon = Icons.Filled.CheckCircle,
                        color = calmGreen
                    )
                )
            } else {
                activeAlerts.take(3).map { alert ->
                    AssistantResponseCard(
                        title = alert.title,
                        body = "Here's what needs attention: ${alert.description}",
                        icon = if (alert.priority == AlertPriority.Low) Icons.Filled.Notifications else Icons.Filled.Warning,
                        color = alert.priority.color
                    )
                }
            }
        }
    }
}

private fun List<ConnectedAppCardModel>.toAssistantCards(
    emptyTitle: String,
    emptyBody: String,
    fallbackColor: Color
): List<AssistantResponseCard> =
    if (isEmpty()) {
        listOf(
            AssistantResponseCard(
                title = emptyTitle,
                body = emptyBody,
                icon = Icons.Filled.CheckCircle,
                color = fallbackColor
            )
        )
    } else {
        map { app ->
            AssistantResponseCard(
                title = app.name,
                body = "Based on your connected cards: ${app.todaySummary}",
                icon = app.category.icon,
                color = app.category.color
            )
        }
    }

private fun hubLifeAppCards(
    apps: List<ConnectedAppCardModel>,
    selectedAreas: Set<String>
): List<HubLifeAppCard> {
    fun appId(id: String) = apps.firstOrNull { it.id == id && it.showOnHub }?.id

    return listOf(
        HubLifeAppCard("Health", "Daily check-in ready", "Check-in", AppCategory.Health.icon, AppCategory.Health.color, appId = appId("healthtracker")),
        HubLifeAppCard("Workout", "Recovery reminder", "Today", AppCategory.Fitness.icon, AppCategory.Fitness.color, urgent = true, appId = appId("healthtracker")),
        HubLifeAppCard("Diet", "Meal plan steady", "3 meals", AppCategory.Diet.icon, AppCategory.Diet.color, appId = appId("platepilot")),
        HubLifeAppCard("Work", "Shift plan ready", "2 tasks", AppCategory.Work.icon, AppCategory.Work.color, urgent = true, appId = appId("workday")),
        HubLifeAppCard("Tasks", "Recordings indexed", "4 markers", AppCategory.Tasks.icon, AppCategory.Tasks.color, appId = appId("markermic")),
        HubLifeAppCard("Chores", "Home reset queued", "5 chores", AppCategory.Chores.icon, AppCategory.Chores.color, urgent = true, appId = appId("choretracker")),
        HubLifeAppCard("Family", "Shared reminders clear", "0 urgent", AppCategory.Family.icon, AppCategory.Family.color),
        HubLifeAppCard("Finance", "Renewals watched", "$42.97", AppCategory.Finance.icon, AppCategory.Finance.color, urgent = true, appId = appId("renewal")),
        HubLifeAppCard("Files", "Cleanup preview safe", "Demo scan", AppCategory.Files.icon, AppCategory.Files.color, appId = appId("foldersmith")),
        HubLifeAppCard("Reminders", "Daily brief enabled", "On", AppCategory.Reminders.icon, AppCategory.Reminders.color)
    ).filter { card ->
        when (card.title) {
            "Health", "Workout" -> "health" in selectedAreas
            "Diet" -> "diet" in selectedAreas
            "Work", "Tasks" -> "work" in selectedAreas
            "Chores" -> "chores" in selectedAreas
            "Family" -> "family" in selectedAreas
            "Finance" -> "money" in selectedAreas
            "Files" -> "files" in selectedAreas
            "Reminders" -> "reminders" in selectedAreas
            else -> true
        }
    }
}

private fun alertsForTab(alerts: List<CommandAlert>, tab: AlertListTab): List<CommandAlert> {
    val today = LocalDate.now()
    return alerts.filter { alert ->
        when (tab) {
            AlertListTab.Today -> !alert.isCompleted && alert.dueDateTime?.toLocalDate() == today
            AlertListTab.Upcoming -> !alert.isCompleted && (alert.dueDateTime?.toLocalDate()?.isAfter(today) == true)
            AlertListTab.Completed -> alert.isCompleted
            AlertListTab.All -> true
        }
    }.sortedWith(compareBy<CommandAlert> { it.isCompleted }.thenBy { it.priority.rank }.thenBy { it.dueDateTime ?: Long.MAX_VALUE })
}

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

private fun formatDueTime(value: Long?): String? {
    if (value == null) return null
    val zoned = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault())
    val date = zoned.toLocalDate()
    val time = zoned.format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()))
    val today = LocalDate.now()
    return when {
        date == today -> "Due today $time"
        date == today.plusDays(1) -> "Due tomorrow $time"
        else -> "Due ${date.format(DateTimeFormatter.ofPattern("MMM d", Locale.getDefault()))} $time"
    }
}

private fun demoDueTime(daysFromNow: Long, hour: Int, minute: Int = 0): Long =
    LocalDate.now()
        .plusDays(daysFromNow)
        .atTime(hour, minute)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

private fun List<CommandAlert>.alertCountFor(app: ConnectedAppCardModel): Int =
    count { !it.isCompleted && it.sourceAppId == app.id }

private fun lastUpdatedStatus(app: ConnectedAppCardModel): String = when (app.id) {
    "workday" -> "Updated 8:10 AM"
    "renewal" -> "Updated today"
    "fridgefinish" -> "Updated 9:20 AM"
    "markermic" -> "Updated yesterday"
    "foldersmith" -> "Updated today"
    "platepilot" -> "Updated 7:45 AM"
    "choretracker" -> "Updated 6:30 AM"
    "healthtracker" -> "Updated today"
    else -> "Updated today"
}

private fun launchInstalledApp(context: Context, app: ConnectedAppCardModel, launchIntent: Intent) {
    try {
        context.startActivity(Intent(launchIntent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Unable to open ${app.name}.", Toast.LENGTH_SHORT).show()
    } catch (_: SecurityException) {
        Toast.makeText(context, "Command Hub does not have access to open this app.", Toast.LENGTH_SHORT).show()
    }
}

private fun openFallbackStoreLink(context: Context, fallbackPlayStoreUrl: String?) {
    if (fallbackPlayStoreUrl.isNullOrBlank()) {
        Toast.makeText(context, "This card can stay on your dashboard as a manual tracker.", Toast.LENGTH_SHORT).show()
        return
    }
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fallbackPlayStoreUrl)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "This install link could not be opened on this device.", Toast.LENGTH_SHORT).show()
    } catch (_: SecurityException) {
        Toast.makeText(context, "Command Hub cannot open this install link.", Toast.LENGTH_SHORT).show()
    }
}

private fun ConnectedAppCardModel.toEntity() = ConnectedAppEntity(
    id = id,
    name = name,
    shortCode = shortCode,
    category = category.name,
    status = status,
    summary = summary,
    todaySummary = todaySummary,
    metrics = metrics,
    recentActivity = recentActivity,
    priority = priority,
    health = health.name,
    showOnHub = showOnHub,
    isCustom = isCustom,
    packageName = packageName,
    fallbackPlayStoreUrl = fallbackPlayStoreUrl,
    iconKey = iconKey,
    accentColor = accentColor,
    sortOrder = sortOrder
)

private fun ConnectedAppEntity.toModel() = ConnectedAppCardModel(
    id = id,
    name = name,
    shortCode = shortCode,
    category = categoryFromName(category),
    status = status,
    summary = summary,
    todaySummary = todaySummary,
    metrics = metrics,
    recentActivity = recentActivity,
    priority = priority,
    health = AppHealth.entries.firstOrNull { it.name == health } ?: AppHealth.Ready,
    showOnHub = showOnHub,
    isCustom = isCustom,
    packageName = packageName,
    fallbackPlayStoreUrl = fallbackPlayStoreUrl,
    iconKey = iconKey,
    accentColor = accentColor,
    sortOrder = sortOrder
)

private fun CommandAlert.toEntity() = AlertEntity(
    id = id,
    title = title,
    body = description,
    category = category.name,
    severity = priority.name,
    resolved = isCompleted,
    description = description,
    priority = priority.name,
    sourceAppId = sourceAppId,
    dueDateTime = dueDateTime,
    isCompleted = isCompleted,
    createdAt = createdAt
)

private fun AlertEntity.toModel() = CommandAlert(
    id = id,
    title = title,
    description = description.ifBlank { body },
    category = categoryFromName(category),
    priority = AlertPriority.entries.firstOrNull { it.name == priority } ?: AlertPriority.entries.firstOrNull { it.name == severity } ?: AlertPriority.Low,
    sourceAppId = sourceAppId,
    dueDateTime = dueDateTime,
    isCompleted = isCompleted || resolved,
    createdAt = createdAt
)

private fun AssistantSummary.toEntity() = AssistantSummaryEntity(
    id = id,
    title = title,
    body = body,
    createdLabel = createdLabel
)

private fun AssistantSummaryEntity.toModel() = AssistantSummary(
    id = id,
    title = title,
    body = body,
    createdLabel = createdLabel
)

private fun commandHubDemoState() = CommandHubState(
    apps = listOf(
        ConnectedAppCardModel(
            id = "workday",
            name = "Workday Planner",
            shortCode = "WP",
            category = AppCategory.Work,
            status = "Shift plan ready",
            summary = "Today's schedule, tasks, and work reminders in one place.",
            todaySummary = "Two work tasks are due before the afternoon block, with one meeting follow-up pinned for review.",
            metrics = listOf("2 tasks", "1 meeting", "Today"),
            recentActivity = listOf("Morning shift checklist refreshed.", "Follow-up task added from notes.", "Break reminder staged locally."),
            priority = 98,
            health = AppHealth.Attention,
            packageName = "com.smithware.workdayplanner",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.workdayplanner"
        ),
        ConnectedAppCardModel(
            id = "renewal",
            name = "Renewal Radar",
            shortCode = "RR",
            category = AppCategory.Finance,
            status = "Renewals watched",
            summary = "Upcoming bills, trials, and subscription reminders.",
            todaySummary = "Two demo renewals are due soon for a sample total of $42.97.",
            metrics = listOf("2 renewals", "$42.97", "7 days"),
            recentActivity = listOf("Weekly bill total refreshed.", "Streaming renewal added to watch list.", "Planning reminder sent to the brief."),
            priority = 94,
            health = AppHealth.Attention,
            packageName = "com.smithware.renewalradar",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.renewalradar"
        ),
        ConnectedAppCardModel(
            id = "fridgefinish",
            name = "Fridge Finish",
            shortCode = "FF",
            category = AppCategory.Diet,
            status = "Food item needs used",
            summary = "Expiring food, meal ideas, and grocery needs.",
            todaySummary = "One high-priority fridge item should be used before tomorrow.",
            metrics = listOf("1 urgent", "3 soon", "Meals"),
            recentActivity = listOf("Chicken moved to use-soon.", "Dinner idea generated locally.", "Grocery reminder updated."),
            priority = 90,
            health = AppHealth.Attention,
            packageName = "com.smithware.fridgefinish",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.fridgefinish"
        ),
        ConnectedAppCardModel(
            id = "markermic",
            name = "MarkerMic",
            shortCode = "MM",
            category = AppCategory.Tasks,
            status = "Recordings indexed",
            summary = "Marked moments, notes, and recording follow-ups.",
            todaySummary = "Four marked moments are ready for review and one follow-up should become a task.",
            metrics = listOf("12 recordings", "4 markers", "1 task"),
            recentActivity = listOf("Team Meeting added three markers.", "Ordering issue promoted to follow-up.", "Weekly notes summary refreshed."),
            priority = 86,
            health = AppHealth.Ready,
            packageName = "com.smithware.markermic",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.markermic"
        ),
        ConnectedAppCardModel(
            id = "foldersmith",
            name = "FolderSmith Mobile",
            shortCode = "FS",
            category = AppCategory.Files,
            status = "Cleanup preview safe",
            summary = "Review-first file cleanup and storage suggestions.",
            todaySummary = "A demo cleanup preview is ready. Nothing is changed automatically.",
            metrics = listOf("Demo scan", "Undo-safe", "Files"),
            recentActivity = listOf("Sample storage scan completed.", "Review-first cleanup card created.", "Undo-safe note attached."),
            priority = 78,
            health = AppHealth.Ready,
            packageName = "com.smithware.foldersmith",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.foldersmith"
        ),
        ConnectedAppCardModel(
            id = "platepilot",
            name = "PlatePilot",
            shortCode = "PP",
            category = AppCategory.Diet,
            status = "Meal plan steady",
            summary = "Diet notes, meal rhythm, and simple food goals.",
            todaySummary = "Today's demo plate goal is balanced protein, hydration, and one planned snack.",
            metrics = listOf("3 meals", "Hydration", "Snack"),
            recentActivity = listOf("Breakfast note logged.", "Hydration reminder pinned.", "Dinner plate suggestion updated."),
            priority = 72,
            health = AppHealth.Ready,
            packageName = "com.smithware.platepilot",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.platepilot"
        ),
        ConnectedAppCardModel(
            id = "choretracker",
            name = "Chore Tracker",
            shortCode = "CT",
            category = AppCategory.Chores,
            status = "Evening reset pending",
            summary = "Room resets, chores, and household routines.",
            todaySummary = "Five chores are queued with two room resets marked as priority.",
            metrics = listOf("5 chores", "2 rooms", "Reset"),
            recentActivity = listOf("Kitchen counter reset moved to tonight.", "Laundry follow-up marked small-step.", "Bedroom reset pinned to Hub."),
            priority = 68,
            health = AppHealth.Attention,
            packageName = "com.smithware.choretracker",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.choretracker"
        ),
        ConnectedAppCardModel(
            id = "healthtracker",
            name = "Health Tracker",
            shortCode = "HT",
            category = AppCategory.Health,
            status = "Daily check-in ready",
            summary = "Simple health reminders, habits, and wellness notes.",
            todaySummary = "A demo daily check-in is ready with hydration and recovery reminders.",
            metrics = listOf("Check-in", "Hydration", "Recovery"),
            recentActivity = listOf("Hydration reminder refreshed.", "Recovery note added.", "Daily check-in card prepared."),
            priority = 64,
            health = AppHealth.Ready,
            packageName = "com.smithware.healthtracker",
            fallbackPlayStoreUrl = "https://play.google.com/store/apps/details?id=com.smithware.healthtracker"
        )
    ),
    alerts = listOf(
        CommandAlert(
            id = "a1",
            title = "Evening reset pending",
            description = "Chore Tracker has a small home reset queued for tonight.",
            category = AppCategory.Chores,
            priority = AlertPriority.Medium,
            sourceAppId = "choretracker",
            dueDateTime = demoDueTime(0, 19, 30),
            createdAt = demoDueTime(0, 7, 0)
        ),
        CommandAlert(
            id = "a2",
            title = "Food item needs used",
            description = "Fridge Finish says one demo item should be used before tomorrow.",
            category = AppCategory.Diet,
            priority = AlertPriority.High,
            sourceAppId = "fridgefinish",
            dueDateTime = demoDueTime(0, 18, 0),
            createdAt = demoDueTime(0, 8, 15)
        ),
        CommandAlert(
            id = "a3",
            title = "Renewals coming up",
            description = "Renewal Radar has two demo renewals due soon.",
            category = AppCategory.Finance,
            priority = AlertPriority.High,
            sourceAppId = "renewal",
            dueDateTime = demoDueTime(2, 9, 0),
            createdAt = demoDueTime(0, 9, 20)
        ),
        CommandAlert(
            id = "a4",
            title = "Work task review",
            description = "Workday Planner has one follow-up task waiting.",
            category = AppCategory.Work,
            priority = AlertPriority.Low,
            sourceAppId = "workday",
            dueDateTime = demoDueTime(1, 10, 0),
            createdAt = demoDueTime(0, 10, 0)
        ),
        CommandAlert(
            id = "a5",
            title = "Recording follow-up complete",
            description = "MarkerMic demo notes were reviewed earlier today.",
            category = AppCategory.Tasks,
            priority = AlertPriority.Low,
            sourceAppId = "markermic",
            dueDateTime = demoDueTime(0, 11, 30),
            isCompleted = true,
            createdAt = demoDueTime(-1, 16, 0)
        )
    ),
    assistantSummaries = listOf(
        AssistantSummary(
            id = "brief_today",
            title = "Today needs attention",
            body = "Chores, renewals, and one recovery-friendly workout are the main local priorities.",
            createdLabel = "Local summary"
        ),
        AssistantSummary(
            id = "weekly_setup",
            title = "Weekly setup",
            body = "Review marked recordings, clear one storage preview, and check two upcoming renewals.",
            createdLabel = "Stored locally"
        )
    ),
    dailySummary = DailySummary(
        title = "Today at a Glance",
        headline = "Command Hub is running with a polished local dashboard foundation.",
        readinessScore = 82,
        items = listOf(
            "Handle one small chore reset before the evening window.",
            "Review marked recordings before they become stale.",
            "Keep workout guidance recovery-friendly today.",
            "Check upcoming renewals before the weekly planning pass."
        )
    )
)
