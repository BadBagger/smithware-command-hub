# Smithware Central Agent Instructions

Smithware Central is a Smithware Studios local-first Android control hub.

Before publishing or connecting this app to DevHub:

1. Keep linked-app status, controls, alerts, and assistant settings local-first.
2. Do not add cloud, login, paid APIs, or embedded API keys for v1.
3. Build with the known local Android toolchain when Java or Gradle are missing on PATH.
4. Publish installable APK updates through GitHub Releases with APK assets before claiming DevHub availability.
5. Update the SoftSmith DevHub registry and `PROJECT_CONTEXT.md` after release changes.
