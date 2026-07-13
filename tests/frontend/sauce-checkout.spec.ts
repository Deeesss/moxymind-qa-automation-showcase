import { test } from "@playwright/test";
import { sauceDemoConfig } from "../../src/config/env.js";
import { CartPage } from "../../src/frontend/pages/cart-page.js";
import { CheckoutCompletePage } from "../../src/frontend/pages/checkout-complete-page.js";
import { CheckoutPage } from "../../src/frontend/pages/checkout-page.js";
import { InventoryPage } from "../../src/frontend/pages/inventory-page.js";
import { LoginPage } from "../../src/frontend/pages/login-page.js";

test.describe("SauceDemo checkout", () => {
  test.beforeEach(async ({ page }) => {
    const loginPage = new LoginPage(page);

    await loginPage.goto();
    await loginPage.login(sauceDemoConfig.standardUser, sauceDemoConfig.password);
  });

  test("blocks checkout when customer information is missing", async ({ page }) => {
    const inventoryPage = new InventoryPage(page);
    const cartPage = new CartPage(page);
    const checkoutPage = new CheckoutPage(page);

    await inventoryPage.addItemByName("Sauce Labs Backpack");
    await inventoryPage.openCart();
    await cartPage.expectLoaded();
    await cartPage.startCheckout();
    await checkoutPage.expectInformationStep();
    await checkoutPage.continueWithoutCustomerData();

    await checkoutPage.expectValidationError("First Name is required");
  });

  test("completes checkout for selected products", async ({ page }) => {
    const inventoryPage = new InventoryPage(page);
    const cartPage = new CartPage(page);
    const checkoutPage = new CheckoutPage(page);
    const completePage = new CheckoutCompletePage(page);

    await inventoryPage.addItemByName("Sauce Labs Backpack");
    await inventoryPage.addItemByName("Sauce Labs Bike Light");
    await inventoryPage.expectCartCount(2);
    await inventoryPage.openCart();
    await cartPage.expectLoaded();
    await cartPage.expectItemVisible("Sauce Labs Backpack");
    await cartPage.expectItemVisible("Sauce Labs Bike Light");
    await cartPage.startCheckout();
    await checkoutPage.expectInformationStep();
    await checkoutPage.continueWithCustomer({
      firstName: "Quality",
      lastName: "Engineer",
      postalCode: "04001"
    });
    await checkoutPage.expectOverviewStep();
    await checkoutPage.finishOrder();

    await completePage.expectOrderComplete();
  });
});
