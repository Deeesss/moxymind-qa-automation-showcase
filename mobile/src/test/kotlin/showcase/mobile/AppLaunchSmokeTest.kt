package showcase.mobile

import io.appium.java_client.android.AndroidDriver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("android")
class AppLaunchSmokeTest {
    private var driver: AndroidDriver? = null

    @AfterEach
    fun tearDown() {
        driver?.quit()
    }

    @Test
    fun launchesConfiguredAndroidApp() {
        driver = AppiumDriverFactory.createAndroidDriver()

        assertNotNull(driver?.sessionId)

        val expectedPackage = System.getenv("MOBILE_EXPECTED_PACKAGE")
        if (!expectedPackage.isNullOrBlank()) {
            assertEquals(expectedPackage, driver?.currentPackage)
        }
    }
}
