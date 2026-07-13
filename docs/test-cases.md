# Test Cases

## API Test Cases

| ID | Priority | Scenario | Expected Result | Automated |
| --- | --- | --- | --- | --- |
| API-001 | High | GET `/api/users?page=1` with valid API key | Returns `200`, exact `total`, and a valid user list schema | Yes |
| API-002 | High | GET `/api/users?page=2` with valid API key | Returns `200`; first two last names are `Lawson` and `Ferguson`; aggregated page count equals `total` | Yes |
| API-003 | High | POST `/api/users` with `{ name, job }` | Returns `201`, echoes submitted fields, includes `id` and `createdAt`, and stays below the configured response-time limit | Yes |
| API-004 | Medium | POST `/api/users` with second data row | Returns `201` with the same create-user contract and response-time limit | Yes |
| API-005 | High | GET `/api/users/23` without `x-api-key` | Returns `401` and explicit `missing_api_key` error before resource lookup | Yes |
| API-006 | Medium | GET `/api/users/23` with valid API key | Returns `404` for missing user | Yes |

## Frontend Test Cases

| ID | Role | Priority | Scenario | Expected Result | Why Essential | Automated |
| --- | --- | --- | --- | --- | --- | --- |
| WEB-001 | Core | High | Login as `standard_user` | User lands on inventory page | Login is the entry point to every authenticated shopping flow. | Yes |
| WEB-002 | Core | High | Login as `locked_out_user` | Login is blocked with visible locked-user error | Blocked accounts must not reach protected functionality, and the rejection must be understandable. | Yes |
| WEB-003 | Core | High | Add and remove backpack from cart | Cart badge changes from `1` back to absent | Cart state is required before checkout and must stay consistent with user actions. | Yes |
| WEB-004 | Additional | Medium | Sort inventory by price low to high | Displayed prices are ascending | Sorting correctness affects product discovery and is easy to regress without an explicit order assertion. | Yes |
| WEB-005 | Additional | Medium | Continue checkout without customer data | First-name validation error is visible | Required-field validation prevents incomplete orders and gives the user actionable feedback. | Yes |
| WEB-006 | Core | High | Complete checkout with two selected products | Completion page confirms order | Checkout is the primary end-to-end business flow from product selection to confirmation. | Yes |

The four core cases satisfy the requested essential scope. The two additional cases add focused negative and ordering coverage without expanding into a full regression suite.

## Optional Mobile Cases

| ID | Priority | Scenario | Expected Result | Automated |
| --- | --- | --- | --- | --- |
| MOB-001 | Low | Launch configured Android app through Appium | Driver session is created; configured package is foregrounded | Optional |
| MOB-002 | Low | Log in with valid credentials on Android and iOS | Tasks screen, all three rows, and logout control are visible | Optional |
| MOB-003 | Low | Log in with invalid credentials on Android and iOS | Exact validation error is visible and Tasks screen is not opened | Optional |
| MOB-004 | Low | Select Mobile automation on Android and iOS | Detail title and coverage description are visible | Optional |

Mobile cases are intentionally not part of the main CI gate.
