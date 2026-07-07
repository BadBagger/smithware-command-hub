# Smithware Command Hub Project Context

## App

- Name: Smithware Command Hub
- Studio: Smithware Studios
- Package: `com.smithware.central`
- Repo: `https://github.com/BadBagger/smithware-command-hub`
- Role: local-first Smithware Studios command dashboard foundation using demo cards
- Latest release: `v0.1.0-smithware-command-hub`
- Release URL: `https://github.com/BadBagger/smithware-command-hub/releases/tag/v0.1.0-smithware-command-hub`
- Release APK assets: `SmithwareCommandHub.apk`, `SmithwareCommandHub-v0.1.0.apk`
- DevHub connection: connected in `v2.1.15-smithware-command-hub`
- DevHub release URL: `https://github.com/BadBagger/softsmith-devhub/releases/tag/v2.1.15-smithware-command-hub`

## Current Scope

Smithware Command Hub v0.1.0 includes:

- Kotlin, Jetpack Compose, Material 3, and ViewModel-backed state
- Five main tabs: Hub, Apps, Assistant, Alerts, Settings
- Demo Smithware app cards only
- Reusable models for connected app cards, command alerts, daily summaries, and app categories
- Dark premium Smithware visual system: graphite, metallic cream, orange sparks, subtle green/purple/steel category accents
- Offline/demo settings posture
- No real app integrations wired in this foundation pass

## Constraints

- Local-first
- No login required
- No cloud required for v1
- No paid APIs required for v1
- No ChatGPT/OpenAI API key embedded in the Android app
- No silent listening, screen watching, private-message reading, or location monitoring
- AI requests should use user-approved summaries instead of raw recordings, full histories, or private files in a future integration pass
- Deeper app controls, notifications, and real app integrations are future-version work

## Build

Known working local toolchain:

```powershell
$env:JAVA_HOME='C:\Users\KyleB\Documents\Codex\2026-07-04\build-a-native-android-app-using\.local-jdk\jdk-17.0.19+10'
$env:ANDROID_HOME='C:\Users\KyleB\Documents\Codex\2026-07-04\build-a-native-android-app-using\.android-sdk'
$env:ANDROID_SDK_ROOT=$env:ANDROID_HOME
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleRelease
```
