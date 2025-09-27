# Quickstart: Frontline Job Finder (Android)

1. Open the `android/` module in Android Studio (File → Open... → select project root).
2. Build the APK: Build → Build Bundle(s) / APK(s) → Build APK(s).
3. Enable Developer Options on your Android device and enable USB debugging.
4. Install the APK via `adb`:

```powershell
adb install -r path\to\app-debug.apk
```

5. First run: configure account in the Configuration page and set filter preferences.
6. For debugging parsing logic: put HTML fixtures under `specs/001-title-frontline-job/fixtures/` and run unit tests that reference them.

Security notes:
- Do not commit credentials or fixture files containing real user data.
- Follow the constitution: use Android Keystore for tokens; log audit events to local file/db and respect retention.
