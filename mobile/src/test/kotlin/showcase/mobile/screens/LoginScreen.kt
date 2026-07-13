package showcase.mobile.screens

import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import showcase.mobile.core.AppiumTestBase

class LoginScreen(private val base: AppiumTestBase) {
    fun assertDisplayed(): LoginScreen {
        assertTrue(base.visible(LoginTitle).text.contains("Moxymind Demo"))
        return this
    }

    fun login(username: String, password: String): LoginScreen {
        base.visible(UsernameInput).sendKeys(username)
        base.visible(PasswordInput).sendKeys(password)
        base.visible(LoginButton).click()
        return this
    }

    fun assertInvalidCredentialsError(): LoginScreen {
        val error = base.visible(ErrorLabel)
        assertEquals("Invalid username or password", error.text)
        assertTrue(base.find(TasksTitle).isEmpty(), "Tasks screen must not be visible after invalid login.")
        return this
    }

    private fun AppiumTestBase.visible(accessibilityId: String) =
        visibleByAccessibilityId(accessibilityId)

    private fun AppiumTestBase.find(accessibilityId: String) =
        elementsByAccessibilityId(accessibilityId)

    private companion object {
        const val LoginTitle = "login.title"
        const val UsernameInput = "login.username"
        const val PasswordInput = "login.password"
        const val LoginButton = "login.submit"
        const val ErrorLabel = "login.error"
        const val TasksTitle = "tasks.title"
    }
}
