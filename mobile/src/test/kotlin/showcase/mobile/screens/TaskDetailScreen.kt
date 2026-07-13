package showcase.mobile.screens

import org.junit.jupiter.api.Assertions.assertTrue
import showcase.mobile.core.AppiumTestBase

class TaskDetailScreen(private val base: AppiumTestBase) {
    fun assertMobileAutomationDetail(): TaskDetailScreen {
        assertTrue(base.visible(Title).text.contains("Mobile automation"))
        assertTrue(base.visible(Description).text.contains("Automation coverage item"))
        return this
    }

    private fun AppiumTestBase.visible(accessibilityId: String) =
        visibleByAccessibilityId(accessibilityId)

    private companion object {
        const val Title = "taskDetail.title"
        const val Description = "taskDetail.description"
    }
}
