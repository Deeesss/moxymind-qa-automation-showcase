import { expect, type Locator, type Page } from "@playwright/test";

export class InventoryPage {
  readonly title: Locator;
  readonly cartLink: Locator;
  readonly cartBadge: Locator;
  readonly sortSelect: Locator;
  readonly itemNames: Locator;
  readonly itemPrices: Locator;

  constructor(private readonly page: Page) {
    this.title = page.getByTestId("title");
    this.cartLink = page.getByTestId("shopping-cart-link");
    this.cartBadge = page.getByTestId("shopping-cart-badge");
    this.sortSelect = page.getByTestId("product-sort-container");
    this.itemNames = page.getByTestId("inventory-item-name");
    this.itemPrices = page.getByTestId("inventory-item-price");
  }

  async expectLoaded(): Promise<void> {
    await expect(this.title).toHaveText("Products");
    await expect(this.itemNames.first()).toBeVisible();
  }

  async addItemByName(itemName: string): Promise<void> {
    await this.inventoryItem(itemName).getByRole("button", { name: "Add to cart" }).click();
  }

  async removeItemByName(itemName: string): Promise<void> {
    await this.inventoryItem(itemName).getByRole("button", { name: "Remove" }).click();
  }

  async expectCartCount(count: number): Promise<void> {
    if (count === 0) {
      await expect(this.cartBadge).toHaveCount(0);
      return;
    }

    await expect(this.cartBadge).toHaveText(String(count));
  }

  async sortBy(option: "az" | "za" | "lohi" | "hilo"): Promise<void> {
    await this.sortSelect.selectOption(option);
  }

  async prices(): Promise<number[]> {
    const priceTexts = await this.itemPrices.allTextContents();
    return priceTexts.map((text) => Number(text.replace("$", "")));
  }

  async openCart(): Promise<void> {
    await this.cartLink.click();
  }

  private inventoryItem(itemName: string): Locator {
    return this.page
      .locator("[data-test='inventory-item']")
      .filter({ has: this.page.getByTestId("inventory-item-name").filter({ hasText: itemName }) });
  }
}
