# BuildSmith Project Context

## App

- Name: BuildSmith
- Studio: Smithware Studios
- Package: `com.smithware.buildsmith`
- Repo: `https://github.com/BadBagger/buildsmith`
- Role: local-first app-builder planning tool for turning rough app ideas into build-ready plans and Codex prompts
- Current release target: `v0.1.0-devhub`

## Current Scope

BuildSmith v1 includes:

- Projects dashboard
- New app wizard
- Blueprint screen
- Screen, feature, and data model planners
- Codex prompt generator and prompt library
- Build checklist and launch planner
- Monetization planning content
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

