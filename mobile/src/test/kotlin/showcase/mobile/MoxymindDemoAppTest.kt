package showcase.mobile

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import showcase.mobile.core.AppiumTestBase
import showcase.mobile.screens.LoginScreen
import showcase.mobile.screens.TaskDetailScreen
import showcase.mobile.screens.TasksScreen

@Tags(Tag("android"), Tag("ios"))
class MoxymindDemoAppTest : AppiumTestBase() {
    @Test
    fun validLoginOpensTasksScreen() {
        LoginScreen(this)
            .assertDisplayed()
            .login("qa_user", "password123")

        TasksScreen(this)
            .assertDisplayed()
            .assertTaskRowsDisplayed()
    }

    @Test
    fun invalidLoginShowsValidationError() {
        LoginScreen(this)
            .assertDisplayed()
            .login("wrong_user", "wrong_password")
            .assertInvalidCredentialsError()
    }

    @Test
    fun selectingMobileAutomationOpensDetailScreen() {
        LoginScreen(this)
            .assertDisplayed()
            .login("qa_user", "password123")

        TasksScreen(this)
            .assertDisplayed()
            .openMobileAutomation()

        TaskDetailScreen(this)
            .assertMobileAutomationDetail()
    }
}
