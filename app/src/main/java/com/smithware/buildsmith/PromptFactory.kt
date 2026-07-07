package com.smithware.buildsmith

object PromptFactory {
    fun generate(
        project: ProjectEntity,
        screens: List<ScreenEntity>,
        features: List<FeatureEntity>,
        models: List<DataModelEntity>,
        settings: BuildSmithSettings,
        type: String
    ): String {
        val mvpFeatures = features.filter { it.priority == "Must-have" || it.priority == "Should-have" }
        val futureFeatures = features.filter { it.priority == "Future" || it.monetization.contains("Pro", true) }
        val assets = features.filter { it.category == "Asset tracker" }
        val bugs = features.filter { it.category == "Bug / update log" }
        return """
Build a native app for ${settings.studioName} called "${project.appName}".

Prompt type: $type
Tagline: Turn app ideas into build-ready prompts.

App goal:
${project.summary.ifBlank { project.description }}

Target audience:
${project.targetUser}

Core problem:
${project.problem}

Why this app should exist:
${project.difference}

Design style:
Use a clean, modern, builder/studio-style interface. Avoid default blue. Use charcoal, warm orange, cream, lime accents, graphite, and subtle gradients. Make it feel like a practical product studio dashboard.

Required screens:
${screens.joinToString("\n") { "- ${it.name}: ${it.purpose}. Components: ${it.components}. Actions: ${it.actions}." }.ifBlank { "- Dashboard\n- Add/Edit\n- Detail\n- Settings" }}

Feature list:
${features.joinToString("\n") { "- ${it.name} (${it.priority}, ${it.complexity}, ${it.monetization}): ${it.description}" }.ifBlank { "- Project dashboard\n- Local storage\n- Prompt generation\n- Settings" }}

Launch assets to prepare:
${assets.joinToString("\n") { "- ${it.name}: ${it.description}. Status: ${it.status}." }.ifBlank { "- Icon\n- Feature graphic\n- Store screenshots\n- Short description\n- Long description\n- Privacy policy notes" }}

Bug and update log:
${bugs.joinToString("\n") { "- ${it.description}. Status: ${it.status}. Notes: ${it.notes}" }.ifBlank { "- No test feedback logged yet." }}

MVP scope:
${mvpFeatures.joinToString("\n") { "- ${it.name}" }.ifBlank { "- Keep the first version focused and offline-ready." }}

Future premium features:
${futureFeatures.joinToString("\n") { "- ${it.name}" }.ifBlank { project.premiumFeatures }}

Data models:
${models.joinToString("\n\n") { "Model: ${it.name}\nPurpose: ${it.purpose}\nFields:\n${it.fields}\nRelationships: ${it.relationships}" }.ifBlank { "- Define Room entities for all locally saved records." }}

Local storage requirements:
Use Room for app data and DataStore for preferences. No login, no cloud, no paid APIs, and all plans stay local on device for v1.

Technical stack:
${settings.defaultTechStack}

Navigation:
Use smooth Material 3 navigation. Suggested main flow: ${project.mainFlow}

Safety and privacy notes:
Do not upload ideas, prompts, app plans, or project data anywhere in v1. Make this clear in the UI: "Your app ideas stay on this device."

Risks and complications:
${project.risks}

Suggested build order:
${project.buildOrder}

Smithware workflow:
Keep this aligned to the loop: idea -> app prompt -> Codex build -> logo -> screenshots -> update prompts -> Play Store checklist -> next app.

Deliverables:
- Full Android project structure
- Working Compose UI
- Room database
- Data models
- ViewModels
- Navigation
- Prompt generation logic
- Demo data
- Local storage
- Light and dark mode
- Empty states
- Input validation
- Edit/delete/archive support
""".trimIndent()
    }
}
