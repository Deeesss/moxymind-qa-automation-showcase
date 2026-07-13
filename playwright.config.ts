import { existsSync } from "node:fs";
import { loadEnvFile } from "node:process";
import { defineConfig, devices } from "@playwright/test";

if (existsSync(".env")) {
  loadEnvFile(".env");
}

const reportOpenMode = process.env.CI ? "never" : "on-failure";

export default defineConfig({
  testDir: "./tests",
  fullyParallel: true,
  forbidOnly: Boolean(process.env.CI),
  retries: process.env.CI ? 1 : 0,
  ...(process.env.CI ? { workers: 2 } : {}),
  timeout: 30_000,
  expect: {
    timeout: 7_500
  },
  reporter: [
    ["list"],
    ["html", { outputFolder: "playwright-report", open: reportOpenMode }]
  ],
  use: {
    testIdAttribute: "data-test",
    actionTimeout: 10_000,
    navigationTimeout: 20_000,
    trace: "retain-on-failure",
    screenshot: "only-on-failure",
    video: "retain-on-failure"
  },
  projects: [
    {
      name: "api",
      testDir: "./tests/api",
      use: {
        // API requests carry a secret header, so failure artifacts must not retain network traces.
        trace: "off"
      }
    },
    {
      name: "chromium",
      testDir: "./tests/frontend",
      use: {
        ...devices["Desktop Chrome"],
        baseURL: process.env.SAUCEDEMO_BASE_URL ?? "https://www.saucedemo.com"
      }
    }
  ]
});
