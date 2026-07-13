import { expect, type Locator, type Page } from "@playwright/test";

export class CheckoutCompletePage {
  readonly title: Locator;
  readonly completeHeader: Locator;

  constructor(private readonly page: Page) {
    this.title = page.getByTestId("title");
    this.completeHeader = page.getByTestId("complete-header");
  }

  async expectOrderComplete(): Promise<void> {
    await expect(this.title).toHaveText("Checkout: Complete!");
    await expect(this.completeHeader).toHaveText("Thank you for your order!");
  }
}
