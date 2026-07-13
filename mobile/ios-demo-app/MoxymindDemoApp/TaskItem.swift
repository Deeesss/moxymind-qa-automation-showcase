struct TaskItem: Identifiable, Hashable {
    let id: String
    let title: String
    let assignment: String
    let decision: String
    let implementation: String
    let steps: [String]
    let proof: String
    let accessibilityIdentifier: String

    static let catalog: [TaskItem] = [
        TaskItem(
            id: "api",
            title: "API automation",
            assignment: "Technical task #2 asks for REST API automation against ReqRes, specifically GET List Users and POST Create User. The result must run from the command line and be clear enough for a reviewer to reproduce.",
            decision: "I made API the primary layer because it gives fast feedback on the service contract. Playwright's API client lets the API and frontend suites share one runner and one HTML report. A small ReqRes client centralizes authentication and headers, while data tables add useful coverage without creating a custom framework.",
            implementation: "TypeScript tests are separated from the ReqRes client, response assertions and test data. Authenticated cases read REQRES_API_KEY. A public missing-key test still runs without the secret, so reviewer CI remains useful instead of failing only because configuration is absent.",
            steps: [
                "Request user pages 1 and 2 through the shared ReqRes client.",
                "Validate pagination fields and the shape of every returned user.",
                "Create two users from reusable name and job data rows.",
                "Validate the echoed data, generated ID and creation timestamp.",
                "Check both the missing API-key boundary and a user that does not exist."
            ],
            proof: "The suite proves the expected 200, 201, 401 and 404 behavior, including response fields and negative boundaries. It deliberately does not claim backend ownership, load testing or broad security coverage.",
            accessibilityIdentifier: "tasks.row.api"
        ),
        TaskItem(
            id: "frontend",
            title: "Frontend automation",
            assignment: "Technical task #1 asks for three to four essential frontend scenarios against SauceDemo, runnable from the command line. The important part is to show why the selected behavior matters, not to automate the entire website.",
            decision: "I chose the user-critical journey: authentication, product handling and checkout. These areas expose both successful behavior and useful validation failures. Six small tests keep each failure precise, while the overall scope still stays within those three business areas.",
            implementation: "Playwright drives Chromium and produces the same report format as the API suite. Page Objects own SauceDemo's data-test selectors and screen actions, so the tests read as user intent without adding a large abstraction layer. Credentials and the base URL remain configurable.",
            steps: [
                "Log in as the standard user and confirm the inventory page opens.",
                "Try the locked user and verify the visible rejection message.",
                "Add and remove a product, then check the cart count.",
                "Sort products by ascending price and compare the displayed values.",
                "Exercise missing checkout data and complete a valid two-product order."
            ],
            proof: "The suite proves that the main shopping path works and that common mistakes are rejected visibly. Stable selectors, explicit assertions and isolated tests make failures readable for a reviewer.",
            accessibilityIdentifier: "tasks.row.frontend"
        ),
        TaskItem(
            id: "mobile",
            title: "Mobile automation",
            assignment: "Technical task #3 asks for a simple mobile automation framework, at least two scenarios, command-line execution and an emulator or simulator. The application under test may be chosen freely, so the solution uses a small deterministic native demo instead of depending on an unstable third-party app.",
            decision: "I chose Kotlin and Appium because one readable test suite can drive both Android and iOS while UiAutomator2 and XCUITest keep the platform interaction native. Three scenarios cover the required positive and negative behavior plus navigation without expanding into a real product.",
            implementation: "The same login, task list and detail flow exists in a native Android Activity and a SwiftUI iOS app. Screen Objects use shared accessibility IDs instead of coordinates or XPath. Environment-driven capabilities select the app artifact, device and platform, and every test starts a fresh driver session.",
            steps: [
                "Launch a fresh app session and verify the branded login screen.",
                "Sign in with the fixed QA account and verify all three task rows.",
                "Start a clean session with invalid credentials and verify the exact error.",
                "Start another clean session, sign in and open Mobile automation detail.",
                "Run the same Kotlin scenarios on Android and iOS through Appium."
            ],
            proof: "The suite proves real app launch, stable accessibility selection, deterministic validation and equivalent core navigation on both platforms. The demo intentionally has no backend, network calls, database, permissions or analytics.",
            accessibilityIdentifier: "tasks.row.mobile"
        )
    ]
}
