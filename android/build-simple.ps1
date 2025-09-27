# Build APK Script for Frontline Helper
Write-Host "Building Frontline Helper APK..." -ForegroundColor Cyan

# Build the APK (gradle.properties now handles JDK configuration)
Write-Host "Building debug APK..." -ForegroundColor Cyan
.\gradlew assembleDebug --no-daemon

if ($LASTEXITCODE -eq 0) {
    $apkPath = "app\build\outputs\apk\debug\app-debug.apk"
    if (Test-Path $apkPath) {
        Write-Host "SUCCESS! APK built successfully!" -ForegroundColor Green
        Write-Host "APK Location: $((Resolve-Path $apkPath).Path)" -ForegroundColor Cyan
        $apkSize = [math]::Round((Get-Item $apkPath).Length / 1MB, 2)
        Write-Host "APK Size: $apkSize MB" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Next Steps:" -ForegroundColor Yellow
        Write-Host "1. Enable Developer Options on your Android device" -ForegroundColor White
        Write-Host "2. Enable USB Debugging and Install unknown apps" -ForegroundColor White
        Write-Host "3. Copy the APK to your device and install it" -ForegroundColor White
        Write-Host "   OR connect device and run: .\gradlew installDebug" -ForegroundColor White
        Write-Host ""
        Write-Host "The app will show 'Frontline Helper - Main' when launched" -ForegroundColor Green
    } else {
        Write-Host "APK file not found at expected location" -ForegroundColor Red
    }
} else {
    Write-Host "Build failed with exit code: $LASTEXITCODE" -ForegroundColor Red
}