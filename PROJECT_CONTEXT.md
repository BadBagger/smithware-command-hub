# BuildSmith Project Context

## App

- Name: BuildSmith Studio
- Studio: Smithware Studios
- Package: `com.smithware.buildsmith`
- Repo: `https://github.com/BadBagger/buildsmith`
- Repo visibility: public, so DevHub can read releases and download APK assets without a logged-in GitHub session
- Role: local-first Smithware Studios command center for turning rough app ideas into build-ready prompts, assets, update logs, and launch checklists
- Latest release: `v0.2.0-studio-command-center`
- Release URL: `https://github.com/BadBagger/buildsmith/releases/tag/v0.2.0-studio-command-center`
- Release APK assets: `BuildSmith.apk`, `BuildSmith-release-v0.2.0-studio-command-center.apk`
- DevHub connection: added in SoftSmith DevHub `v2.1.10-buildsmith`; refreshed as BuildSmith Studio in `v2.1.11-buildsmith-studio`

## Current Scope

BuildSmith Studio includes:

- Projects dashboard
- Smithware workflow command center
- New app wizard
- Blueprint screen
- Screen, feature, and data model planners
- Codex prompt generator and prompt library
- Build checklist and launch planner
- Monetization planning content
- Asset tracker for icons, screenshots, store descriptions, privacy notes, closed testing notes, and store listings
- Bug/update log that turns test feedback into Codex update prompts
- Settings
- Demo projects
- Local Room database and DataStore settings
- Icon Studio for picking an image, removing connected white background locally, previewing, and exporting a transparent PNG

## Constraints

- Local-first
- No login required
- No cloud required for v1
- No paid APIs required for v1
- Does not compile APKs directly on the phone in v1
- App ideas and prompts stay on device

## Build

Known working local toolchain:

```powershell
$env:JAVA_HOME='C:\Users\KyleB\Documents\Codex\2026-07-04\build-a-native-android-app-using\.local-jdk\jdk-17.0.19+10'
$env:ANDROID_HOME='C:\Users\KyleB\Documents\Codex\2026-07-04\build-a-native-android-app-using\.android-sdk'
$env:ANDROID_SDK_ROOT=$env:ANDROID_HOME
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleRelease
```
