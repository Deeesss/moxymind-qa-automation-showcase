import { expect, test } from "@playwright/test";
import { sauceDemoConfig } from "../../src/config/env.js";
import { InventoryPage } from "../../src/frontend/pages/inventory-page.js";
import { LoginPage } from "../../src/frontend/pages/login-page.js";

test.describe("SauceDemo inventory and cart", () => {
  test.beforeEach(async ({ page }) => {
    const loginPage = new LoginPage(page);

    await loginPage.goto();
    await loginPage.login(sauceDemoConfig.standardUser, sauceDemoConfig.password);
  });

  test("adds and removes a product from the cart", async ({ page }) => {
    const inventoryPage = new InventoryPage(page);

    await inventoryPage.expectLoaded();
    await inventoryPage.addItemByName("Sauce Labs Backpack");
    await inventoryPage.expectCartCount(1);
    await inventoryPage.removeItemByName("Sauce Labs Backpack");
    await inventoryPage.expectCartCount(0);
  });

  test("sorts products by ascending price", async ({ page }) => {
    const inventoryPage = new InventoryPage(page);

    await inventoryPage.expectLoaded();
    await inventoryPage.sortBy("lohi");

    const prices = await inventoryPage.prices();
    const sortedPrices = [...prices].sort((left, right) => left - right);
    expect(prices).toEqual(sortedPrices);
  });
});
