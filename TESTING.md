# Frontline Helper - Testing Guide

## 🚀 Quick Start Testing

### Current App Features (Basic Scaffold)
- ✅ Simple MainActivity with "Frontline Helper - Main" text
- ✅ Proper Android manifest and basic UI
- ✅ Ready for Frontline integration development

### Testing the Current Build

#### Option 1: Android Studio (Recommended)
1. **Install Android Studio**: https://developer.android.com/studio
2. **Open project**: Open the `android` folder in Android Studio  
3. **Wait for sync**: Let Gradle sync complete
4. **Run**: Click green play button or Shift+F10
   - **Device**: Connect Android device with USB debugging enabled
   - **Emulator**: Create/start an AVD (Android Virtual Device)

#### Option 2: Command Line Build
1. **Install Java 17** (required): https://adoptium.net/temurin/releases/?version=17
2. **Run build script**:
   ```powershell
   cd android
   .\build-apk.ps1
   ```
3. **Install APK**: Copy `app-debug.apk` to device or run `.\gradlew installDebug`

#### Option 3: GitHub Actions CI Build
The CI automatically builds APKs on every push:
- Check: https://github.com/tatahereGH/projects/actions
- Download APK artifacts from successful builds
- Side-load to test device

### What You Should See
1. **App launches** successfully
2. **Main screen** shows "Frontline Helper - Main" text
3. **No crashes** or error dialogs

### Next Development Steps
Based on your specification in `specs/001-title-frontline-job/spec.md`:

1. **Authentication Screen** - Add Frontline login UI
2. **Configuration Page** - Job filtering preferences  
3. **Job List UI** - Display fetched/filtered jobs
4. **Notification System** - Alert for matching jobs
5. **Accept Actions** - Single accept & Accept All (Consolidated Accept Page)
6. **Accepted Jobs Screen** - View accepted jobs history

### Testing Real Features (When Implemented)
1. **Manual Testing**: Use real Frontline credentials (test account recommended)
2. **Mock Testing**: Create mock job data for UI testing
3. **Unit Tests**: Test job parsing, filtering, and data models
4. **Integration Tests**: Test Frontline API interactions
5. **UI Tests**: Automated UI flow testing

### Debugging & Logs
- **Android Studio**: Use Logcat to view app logs
- **ADB**: `adb logcat | findstr FrontlineHelper`
- **Audit Logs**: App will create local audit logs (per FR-013)

### Performance Testing
- **Battery usage**: Monitor background polling impact
- **Network usage**: Track API call frequency  
- **Memory**: Check for leaks during long polling sessions
- **Rate limiting**: Verify backoff behavior with provider limits

### Security Testing  
- **Credential storage**: Verify secure storage (Android Keystore)
- **Network traffic**: Check HTTPS usage, no credential leaks
- **App permissions**: Minimal required permissions only
- **Side-loading safety**: Test APK integrity and signing

---

## 🔧 Current Status
- **✅ Basic Android scaffold working**
- **✅ CI/CD pipeline functional** 
- **📋 Ready for Frontline feature development**

The app currently shows a simple text screen but has all the infrastructure needed to implement the full Frontline job finder features described in your specification.