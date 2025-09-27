# Build APK Script for Frontline Helper
# This script builds a debug APK that can be side-loaded

Write-Host "🔧 Building Frontline Helper APK..." -ForegroundColor Cyan

# Check for Android Studio or manually set JAVA_HOME to Java 17
if (Test-Path "C:\Program Files\Android\Android Studio\jbr") {
    $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
    Write-Host "✅ Using Android Studio's bundled JDK" -ForegroundColor Green
} elseif (Test-Path "C:\Program Files\Eclipse Adoptium\jdk-17*") {
    $java17Path = Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory | Where-Object {$_.Name -like "jdk-17*"} | Select-Object -First 1
    $env:JAVA_HOME = $java17Path.FullName
    Write-Host "✅ Using Eclipse Adoptium JDK 17: $($env:JAVA_HOME)" -ForegroundColor Green
} else {
    Write-Host "❌ ERROR: Java 17 not found!" -ForegroundColor Red
    Write-Host "Please install Java 17 from: https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Yellow
    Write-Host "Or install Android Studio which includes JDK 17" -ForegroundColor Yellow
    exit 1
}

# Build the APK
Write-Host "🔨 Building debug APK..." -ForegroundColor Cyan
try {
    .\gradlew assembleDebug --no-daemon
    
    if ($LASTEXITCODE -eq 0) {
        $apkPath = "app\build\outputs\apk\debug\app-debug.apk"
        if (Test-Path $apkPath) {
            Write-Host "🎉 SUCCESS! APK built successfully!" -ForegroundColor Green
            Write-Host "📱 APK Location: $((Resolve-Path $apkPath).Path)" -ForegroundColor Cyan
            Write-Host ""
            Write-Host "📋 Next Steps:" -ForegroundColor Yellow
            Write-Host "1. Enable Developer Options on your Android device" -ForegroundColor White
            Write-Host "2. Enable USB Debugging and Install unknown apps" -ForegroundColor White
            Write-Host "3. Copy the APK to your device and install it" -ForegroundColor White
            Write-Host "   OR connect device and run: .\gradlew installDebug" -ForegroundColor White
        } else {
            Write-Host "❌ APK file not found at expected location" -ForegroundColor Red
        }
    } else {
        Write-Host "❌ Build failed with exit code: $LASTEXITCODE" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Build error: $($_.Exception.Message)" -ForegroundColor Red
}