# Optional Mobile Automation

Kotlin/Appium automation bonus for both mobile lanes:

- Android on Windows
- iOS on macOS

This folder is outside the main API/frontend CI gate. It is a mobile automation supplement, not a product.

## Scope

Android and iOS run the same automation contract. The only difference is the OS-specific app artifact and Appium driver:

- Windows lane: Android SDK, Android Emulator, UiAutomator2.
- Mac lane: Xcode, iOS Simulator, XCUITest.

Included demo app sources:

- `android-demo-app/` - native Android APK demo used on Windows.
- `ios-demo-app/` - native SwiftUI iOS demo app used on macOS.

## Windows Android Setup

Required tools:

- JDK 21+
- Gradle
- Android SDK
- Android Emulator or connected Android device
- Appium server
- Appium UiAutomator2 driver
- target Android `.apk` implementing the same accessibility ids

Build the included Android demo APK:

```powershell
cd mobile
gradle :android-demo-app:assembleDebug
```

Built APK path:

```text
mobile/android-demo-app/build/outputs/apk/debug/android-demo-app-debug.apk
```

### Replacing the Android app

The Android Appium configuration uses `enforceAppInstall=true`. Every `gradle testAndroid` run therefore reinstalls the APK referenced by `MOBILE_APP_PATH`, including when the new build has the same Android `versionCode` as the installed app. This prevents an older build from remaining in the emulator.

To replace the app manually before inspecting it without Appium, run these commands from `mobile/` after building the APK:

```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
$apk = (Resolve-Path ".\android-demo-app\build\outputs\apk\debug\android-demo-app-debug.apk").Path

& $adb uninstall com.example.MoxymindDemoApp
& $adb install $apk
```

The uninstall command intentionally removes the previous app data as well as the old APK. After the clean install, either start the app from the emulator or run `gradle testAndroid`; Appium launches the configured APK automatically.

Start the configured AVD in a separate PowerShell terminal. Android Studio is not required once the Android SDK, emulator, and AVD are installed:

```powershell
$emulator = "$env:LOCALAPPDATA\Android\Sdk\emulator\emulator.exe"
& $emulator -list-avds
& $emulator -avd Moxymind_Android_API_36
```

The list command confirms that `Moxymind_Android_API_36` exists before launch. Wait until the Android home screen is visible.

Start Appium in a second terminal and leave it running:

```powershell
appium
```

Configure and run the tests from a third terminal opened at the repository root:

```powershell
cd mobile
$env:MOBILE_PLATFORM="android"
$env:MOBILE_APP_PATH=(Resolve-Path ".\android-demo-app\build\outputs\apk\debug\android-demo-app-debug.apk").Path
$env:APPIUM_SERVER_URL="http://127.0.0.1:4723"
$env:ANDROID_DEVICE_NAME="Moxymind_Android_API_36"
$env:MOBILE_EXPECTED_PACKAGE="com.example.MoxymindDemoApp"
gradle testAndroid
```

Current Android coverage:

- same login/tasks/detail scenarios as iOS
- runs against Android through UiAutomator2
- uses the configured APK from `MOBILE_APP_PATH`

## macOS iOS Setup

iOS Simulator cannot run on Windows. It is part of Xcode/macOS.

Expected iOS app:

- App name: `MoxymindDemoApp`
- Bundle identifier: `com.example.MoxymindDemoApp`
- Source folder: `mobile/ios-demo-app`
- Default simulator app artifact after building from `mobile/ios-demo-app`:

```text
mobile/ios-demo-app/build/DerivedData/Build/Products/Debug-iphonesimulator/MoxymindDemoApp.app
```

Required tools:

- macOS
- Xcode
- booted iOS Simulator, locally used as `iPhone 17` on iOS `26.5`
- JDK 21+
- Gradle
- Appium server
- Appium XCUITest driver
- built simulator `.app`

Environment:

```bash
cd mobile
export IOS_APP_PATH="$PWD/ios-demo-app/build/DerivedData/Build/Products/Debug-iphonesimulator/MoxymindDemoApp.app"
export IOS_DEVICE_NAME="iPhone 17"
export IOS_PLATFORM_VERSION="26.5"
export IOS_BUNDLE_ID="com.example.MoxymindDemoApp"
export APPIUM_SERVER_URL="http://127.0.0.1:4723"
```

`IOS_PLATFORM_VERSION` is optional. Omit it if Appium can select the booted simulator by name.

Run:

```bash
appium
gradle testIos
```

If `IOS_APP_PATH` is omitted, the Kotlin config defaults to the same `ios-demo-app/build/DerivedData/.../MoxymindDemoApp.app` path when run from `mobile/`.

## Automated Scenarios

The same scenarios apply to Android and iOS:

1. Valid login opens Tasks screen.
2. Invalid login shows `Invalid username or password` and does not navigate.
3. Selecting `Mobile automation` opens the detail screen.

Valid credentials:

```text
username: qa_user
password: password123
```

## Selector Rule

Flow selectors use `AppiumBy.accessibilityId(...)` only.

No coordinate selectors. No XPath for required elements.

Required accessibility identifiers on both OS targets:

```text
login.title
login.username
login.password
login.submit
login.error
tasks.title
tasks.row.api
tasks.row.frontend
tasks.row.mobile
tasks.logout
taskDetail.title
taskDetail.description
```

## Compile Without Launching Devices

```bash
gradle testClasses
```

Default `gradle test` excludes platform-tagged Appium tests. Use `testAndroid` or `testIos` explicitly.
