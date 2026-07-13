import { test } from "@playwright/test";
import { sauceDemoConfig } from "../../src/config/env.js";
import { InventoryPage } from "../../src/frontend/pages/inventory-page.js";
import { LoginPage } from "../../src/frontend/pages/login-page.js";

test.describe("SauceDemo authentication", () => {
  test("standard user can sign in and reach the inventory", async ({ page }) => {
    const loginPage = new LoginPage(page);
    const inventoryPage = new InventoryPage(page);

    await loginPage.goto();
    await loginPage.login(sauceDemoConfig.standardUser, sauceDemoConfig.password);

    await inventoryPage.expectLoaded();
  });

  test("locked user is rejected with a visible error", async ({ page }) => {
    const loginPage = new LoginPage(page);

    await loginPage.goto();
    await loginPage.login(sauceDemoConfig.lockedUser, sauceDemoConfig.password);

    await loginPage.expectLoginError("Sorry, this user has been locked out");
  });
});
