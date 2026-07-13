package showcase.mobile.core

import io.appium.java_client.AppiumBy
import io.appium.java_client.AppiumDriver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import showcase.mobile.AppiumDriverFactory
import java.time.Duration

abstract class AppiumTestBase {
    protected lateinit var driver: AppiumDriver
    protected lateinit var wait: WebDriverWait

    @BeforeEach
    fun startApp() {
        driver = AppiumDriverFactory.createDriver()
        wait = WebDriverWait(driver, Duration.ofSeconds(12))
    }

    @AfterEach
    fun stopApp() {
        if (::driver.isInitialized) {
            driver.quit()
        }
    }

    fun visibleByAccessibilityId(accessibilityId: String): WebElement =
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId(accessibilityId)))

    fun elementsByAccessibilityId(accessibilityId: String): List<WebElement> =
        driver.findElements(AppiumBy.accessibilityId(accessibilityId))
}
