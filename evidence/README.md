# Evidence

Captured evidence from the standalone QA automation showcase.

## API

- `api/api-test-output.txt` - raw output from `npm run test:api`.
- `api/openapi-endpoints.txt` - endpoint list extracted from the official ReqRes OpenAPI spec.
- `api/01-reqres-openapi-endpoints.png` - Swagger-like endpoint view generated from `https://reqres.in/openapi.json`.
- `api/01-reqres-api-reference.png` - ReqRes API reference / OpenAPI docs context.
- `api/02-api-test-run-summary.png` - visual summary of the full authenticated API Playwright run.

The captured API run used `REQRES_API_KEY` from the local ignored `.env` file. All six scenarios passed: exact paginated GET assertions, both data-driven POST rows with the response-time threshold, the unauthenticated `401` boundary, and the authenticated missing-user `404`. The secret value is not present in the evidence or repository.

## Frontend

- `frontend/frontend-test-output.txt` - raw output from `npm run test:frontend`.
- `frontend/00-frontend-test-run-summary.png` - visual summary of the frontend Playwright run.
- `frontend/01-saucedemo-login.png` - login screen.
- `frontend/02-inventory-after-login.png` - authenticated inventory screen.
- `frontend/03-cart-with-product.png` - cart state after adding a product.
- `frontend/04-checkout-complete.png` - successful checkout completion.
- `frontend/05-locked-user-error.png` - negative authentication scenario.

Generated Playwright folders such as `playwright-report/` and `test-results/` remain intentionally ignored. This folder stores only compact, reviewer-friendly evidence.
