package showcase.mobile.screens

import org.junit.jupiter.api.Assertions.assertTrue
import showcase.mobile.core.AppiumTestBase

class TasksScreen(private val base: AppiumTestBase) {
    fun assertDisplayed(): TasksScreen {
        assertTrue(base.visible(Title).text.contains("Tasks"))
        base.visible(LogoutButton)
        return this
    }

    fun assertTaskRowsDisplayed(): TasksScreen {
        base.visible(ApiAutomationRow)
        base.visible(FrontendAutomationRow)
        base.visible(MobileAutomationRow)
        return this
    }

    fun openMobileAutomation(): TasksScreen {
        base.visible(MobileAutomationRow).click()
        return this
    }

    fun logout(): TasksScreen {
        base.visible(LogoutButton).click()
        return this
    }

    private fun AppiumTestBase.visible(accessibilityId: String) =
        visibleByAccessibilityId(accessibilityId)

    private companion object {
        const val Title = "tasks.title"
        const val ApiAutomationRow = "tasks.row.api"
        const val FrontendAutomationRow = "tasks.row.frontend"
        const val MobileAutomationRow = "tasks.row.mobile"
        const val LogoutButton = "tasks.logout"
    }
}
