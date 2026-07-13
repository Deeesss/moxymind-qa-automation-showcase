# Known Limitations

## External Systems

The main suites use public external systems:

- ReqRes: `https://reqres.in`
- SauceDemo: `https://www.saucedemo.com`

Failures can be caused by upstream outage, network filtering, rate limits, WAF behavior, or public demo-site changes. The project does not hide this behind mocks because the assignment asks for real API and frontend automation.

## ReqRes API Key

ReqRes requires `x-api-key` for API requests. Authenticated API tests require:

```bash
REQRES_API_KEY=<your key>
```

The suite includes one unauthenticated negative test that proves the missing-key boundary. The full API gate must run with a real key.

ReqRes can serve the legacy List Users endpoint in a public compatibility mode. The missing-key test therefore targets `GET /api/users/23`, which currently enforces authentication before resource lookup. The authenticated version of the same request then proves the expected `404` missing-resource behavior.

## Test Data Persistence

ReqRes create-user responses are treated as response-contract checks. This suite does not assume durable persistence of created users across later reads because the assignment only requires POST creation validation.

## Response-Time Assertion

The POST scenario enforces `API_RESPONSE_TIME_LIMIT_MS` with a `2000` ms default. This measures a single public-service round trip and JSON response read; it is not a load-test percentile or server-only latency measurement. Network conditions can affect the result, so the threshold is explicit and configurable rather than hidden in the test.

## Browser Coverage

The frontend suite runs Chromium only. This keeps the task bounded. Expanding to Firefox/WebKit would be straightforward, but it would increase runtime and maintenance without adding much signal for this assignment.

## Mobile Bonus

The mobile folder is an optional Kotlin/Appium bonus. It is not wired into CI and does not include emulator or simulator provisioning because device images, Appium server lifecycle, and real-device authority are machine-specific. The included Android and iOS demo app sources provide local target artifacts, but mobile setup intentionally stays isolated so it cannot block API or frontend review.

## No Dashboard

There is no custom dashboard. Playwright HTML reports are enough for this scale and avoid building a second product around the tests.
