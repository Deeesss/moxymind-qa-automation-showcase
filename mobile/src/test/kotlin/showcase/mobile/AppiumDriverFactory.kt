package showcase.mobile

import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.ios.options.XCUITestOptions
import showcase.mobile.config.MobileTestConfig
import java.net.URI
import java.time.Duration

object AppiumDriverFactory {
    fun createDriver(): AppiumDriver {
        return when (mobilePlatform()) {
            MobilePlatform.Android -> createAndroidDriver()
            MobilePlatform.Ios -> createIosDriver()
        }
    }

    fun createAndroidDriver(): AndroidDriver {
        val appPath = requiredEnv("MOBILE_APP_PATH")
        val serverUrl = URI(System.getenv("APPIUM_SERVER_URL") ?: "http://127.0.0.1:4723").toURL()
        val options = UiAutomator2Options()

        options.setPlatformName("Android")
        options.setAutomationName("UiAutomator2")
        options.setDeviceName(System.getenv("ANDROID_DEVICE_NAME") ?: "Android Emulator")
        options.setApp(appPath)
        options.setAutoGrantPermissions(true)
        options.setNewCommandTimeout(Duration.ofSeconds(60))

        return AndroidDriver(serverUrl, options)
    }

    fun createIosDriver(): IOSDriver {
        val config = MobileTestConfig.ios()
        val options = XCUITestOptions()

        options.setPlatformName("iOS")
        options.setAutomationName("XCUITest")
        options.setDeviceName(config.deviceName)
        options.setBundleId(config.bundleId)
        options.setApp(config.appPath)
        options.setFullReset(config.fullReset)
        options.setNewCommandTimeout(Duration.ofSeconds(60))
        config.platformVersion?.let { options.setPlatformVersion(it) }

        return IOSDriver(config.appiumServerUrl.toURL(), options)
    }

    private fun mobilePlatform(): MobilePlatform {
        return when ((System.getenv("MOBILE_PLATFORM") ?: "android").lowercase()) {
            "android" -> MobilePlatform.Android
            "ios" -> MobilePlatform.Ios
            else -> error("MOBILE_PLATFORM must be either 'android' or 'ios'.")
        }
    }

    private fun requiredEnv(name: String): String =
        System.getenv(name)?.takeIf { it.isNotBlank() }
            ?: error("$name must be set before running mobile tests.")
}

private enum class MobilePlatform {
    Android,
    Ios
}
