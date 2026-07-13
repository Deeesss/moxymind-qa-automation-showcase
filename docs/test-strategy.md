# Test Strategy

## Purpose

This project demonstrates a scoped QA automation solution for a technical task. It is intentionally more complete than a three-test sample, but it avoids a custom framework, dashboard, test management clone, or enterprise platform.

## Scope

In scope:

- API automation against ReqRes user endpoints
- Frontend automation against SauceDemo
- Playwright HTML report and traces for failed runs
- GitHub Actions jobs for API and frontend suites
- Optional Kotlin/Appium suite against native Android and iOS demo apps outside the main gate

Out of scope:

- Owning or mocking the external systems under test
- Load, performance, security, or contract testing beyond the selected scenarios
- Custom reporting dashboards
- Emulator orchestration in CI
- Browser matrix expansion beyond Chromium for this task

## Test Layers

### API

The API layer is the highest priority because it is fast, deterministic when the service is healthy, and validates response contracts directly. ReqRes now requires `x-api-key`, so authenticated tests fail fast when `REQRES_API_KEY` is missing. One unauthenticated negative test remains runnable without a key to prove the auth boundary.

Covered API patterns:

- positive GET list users
- positive POST create user
- data-driven examples for list/create
- exact page-two `total` and leading `last_name` assertions required by the task
- paginated aggregation before comparing the returned user count with `total`
- configurable POST response-time threshold
- manual response schema assertions
- negative unauthorized request
- negative missing resource

### Frontend

The frontend layer uses Page Objects to keep intent separate from selectors. Tests cover core SauceDemo flows without turning the assignment into a full commerce regression suite.

Covered frontend patterns:

- positive login
- locked-user negative login
- add/remove cart behavior
- price sorting behavior
- checkout validation
- successful checkout

### Mobile

The mobile folder is optional. It contains shared Kotlin/Appium Screen Objects and three deterministic scenarios executed against the included native Android and iOS demo apps. UiAutomator2 drives Android and XCUITest drives iOS through the same accessibility-id contract. Mobile remains outside CI because emulator and simulator provisioning, Appium server lifecycle, and platform toolchains are machine-specific.

## Data Strategy

API data examples live in `src/test-data/api-users.ts`. The suite uses small, explicit data sets so the reviewer can see intent without digging through fixtures. Frontend credentials are read from environment variables with SauceDemo defaults.

The List Users response is paginated: one `data` array corresponds to `per_page`, not the global `total`. The page-two test therefore checks the required `Lawson` and `Ferguson` values, then aggregates every returned page before comparing the complete user count with `total`.

POST timing is measured around the complete request and JSON response read. `API_RESPONSE_TIME_LIMIT_MS` defaults to `2000` and can be adjusted for the execution environment. This is a bounded task assertion, not a load or performance test.

## Reporting

Playwright generates:

- list output in the terminal
- HTML report in `playwright-report/`
- frontend traces/screenshots/videos retained on failure
- API network traces disabled so authenticated request headers are not retained in failure artifacts

Use:

```bash
npm run report
```

## CI Strategy

GitHub Actions runs API and frontend jobs separately. The API job reports whether `REQRES_API_KEY` is configured. Authenticated tests run when the secret is available; otherwise only the public negative boundary runs. The frontend job installs Chromium and runs the SauceDemo suite.

## Stability Controls

- Tests are grouped by layer.
- External base URLs are configurable.
- API auth is explicit.
- Frontend locators prefer stable `data-test` attributes.
- Retries are limited to CI only.
- Mobile is isolated from the main acceptance path.
