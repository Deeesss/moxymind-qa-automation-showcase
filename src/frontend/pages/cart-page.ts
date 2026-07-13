import { expect, type Locator, type Page } from "@playwright/test";

export class CartPage {
  readonly title: Locator;
  readonly cartItems: Locator;
  readonly checkoutButton: Locator;

  constructor(private readonly page: Page) {
    this.title = page.getByTestId("title");
    this.cartItems = page.getByTestId("inventory-item");
    this.checkoutButton = page.getByTestId("checkout");
  }

  async expectLoaded(): Promise<void> {
    await expect(this.title).toHaveText("Your Cart");
  }

  async expectItemVisible(itemName: string): Promise<void> {
    await expect(this.page.getByTestId("inventory-item-name").filter({ hasText: itemName })).toBeVisible();
  }

  async startCheckout(): Promise<void> {
    await this.checkoutButton.click();
  }
}
