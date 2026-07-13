import { expect, test } from "@playwright/test";
import {
  assertCreateUserResponse,
  assertListUsersResponse,
  assertReqresError
} from "../../src/api/reqres-assertions.js";
import { ReqresClient } from "../../src/api/reqres-client.js";
import { reqresConfig } from "../../src/config/env.js";
import { createUserExamples, userListExamples } from "../../src/test-data/api-users.js";

test.describe("ReqRes user API", () => {
  test("rejects an unauthenticated protected-user request with an explicit API-key error", async ({ request }) => {
    const client = new ReqresClient(request);

    const response = await client.getUserWithoutApiKey(23);
    const body: unknown = await response.json();

    expect(response.status()).toBe(401);
    assertReqresError(body, "missing_api_key");
  });

  test.describe("authenticated API flows", () => {
    test.skip(!process.env.REQRES_API_KEY, "REQRES_API_KEY is required for authenticated ReqRes tests.");

    for (const example of userListExamples) {
      test(`lists users for ${example.label}`, async ({ request }) => {
        const client = new ReqresClient(request);

        const response = await client.listUsers(example.page);
        const body: unknown = await response.json();

        expect(response.status()).toBe(200);
        assertListUsersResponse(body, example.page);
        expect(body.total).toBe(example.expectedTotal);

        if ("expectedLeadingLastNames" in example) {
          expect(body.data).toHaveLength(body.per_page);
          expect(body.data[0]?.last_name).toBe(example.expectedLeadingLastNames[0]);
          expect(body.data[1]?.last_name).toBe(example.expectedLeadingLastNames[1]);

          const pages = await Promise.all(
            Array.from({ length: body.total_pages }, async (_, index) => {
              const pageNumber = index + 1;

              if (pageNumber === body.page) {
                return body;
              }

              const pageResponse = await client.listUsers(pageNumber);
              const pageBody: unknown = await pageResponse.json();

              expect(pageResponse.status()).toBe(200);
              assertListUsersResponse(pageBody, pageNumber);
              return pageBody;
            })
          );

          const allUsers = pages.flatMap((page) => page.data);
          expect(allUsers).toHaveLength(body.total);
        }
      });
    }

    for (const payload of createUserExamples) {
      test(`creates user ${payload.name}`, async ({ request }) => {
        const client = new ReqresClient(request);

        const startedAt = performance.now();
        const response = await client.createUser(payload);
        const body: unknown = await response.json();
        const responseTimeMs = performance.now() - startedAt;

        expect(response.status()).toBe(201);
        assertCreateUserResponse(body, payload);
        expect(
          responseTimeMs,
          `POST /api/users took ${responseTimeMs.toFixed(0)} ms; limit is ${reqresConfig.responseTimeLimitMs} ms.`
        ).toBeLessThan(reqresConfig.responseTimeLimitMs);
      });
    }

    test("returns not found for a non-existing user id", async ({ request }) => {
      const client = new ReqresClient(request);

      const response = await client.getUser(23);

      expect(response.status()).toBe(404);
      expect(await response.text()).toBe("{}");
    });
  });
});
