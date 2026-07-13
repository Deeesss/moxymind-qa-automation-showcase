package showcase.mobile.config

import java.net.URI
import java.nio.file.Path

data class IosConfig(
    val appPath: String,
    val appiumServerUrl: URI,
    val deviceName: String,
    val platformVersion: String?,
    val bundleId: String,
    val fullReset: Boolean,
)

object MobileTestConfig {
    fun ios(): IosConfig {
        return IosConfig(
            appPath = env(
                "IOS_APP_PATH",
                defaultIosAppPath(),
            ),
            appiumServerUrl = URI(env("APPIUM_SERVER_URL", "http://127.0.0.1:4723")),
            deviceName = env("IOS_DEVICE_NAME", "iPhone 17"),
            platformVersion = optionalEnv("IOS_PLATFORM_VERSION"),
            bundleId = env("IOS_BUNDLE_ID", "com.example.MoxymindDemoApp"),
            fullReset = env("IOS_FULL_RESET", "true").toBooleanStrict(),
        )
    }

    private fun env(name: String, default: String): String =
        System.getenv(name)?.takeIf { it.isNotBlank() } ?: default

    private fun optionalEnv(name: String): String? =
        System.getenv(name)?.takeIf { it.isNotBlank() }

    private fun defaultIosAppPath(): String {
        val mobileRoot = if (Path.of("settings.gradle.kts").toFile().exists()) {
            Path.of(".")
        } else {
            Path.of("mobile")
        }

        return mobileRoot
            .resolve("ios-demo-app/build/DerivedData/Build/Products/Debug-iphonesimulator/MoxymindDemoApp.app")
            .toAbsolutePath()
            .normalize()
            .toString()
    }
}
