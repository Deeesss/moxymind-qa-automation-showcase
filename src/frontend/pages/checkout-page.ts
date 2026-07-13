import { expect, type Locator, type Page } from "@playwright/test";

export class CheckoutPage {
  readonly title: Locator;
  readonly firstNameInput: Locator;
  readonly lastNameInput: Locator;
  readonly postalCodeInput: Locator;
  readonly continueButton: Locator;
  readonly finishButton: Locator;
  readonly errorMessage: Locator;

  constructor(private readonly page: Page) {
    this.title = page.getByTestId("title");
    this.firstNameInput = page.getByTestId("firstName");
    this.lastNameInput = page.getByTestId("lastName");
    this.postalCodeInput = page.getByTestId("postalCode");
    this.continueButton = page.getByTestId("continue");
    this.finishButton = page.getByTestId("finish");
    this.errorMessage = page.getByTestId("error");
  }

  async expectInformationStep(): Promise<void> {
    await expect(this.title).toHaveText("Checkout: Your Information");
  }

  async continueWithCustomer(customer: {
    firstName: string;
    lastName: string;
    postalCode: string;
  }): Promise<void> {
    await this.firstNameInput.fill(customer.firstName);
    await this.lastNameInput.fill(customer.lastName);
    await this.postalCodeInput.fill(customer.postalCode);
    await this.continueButton.click();
  }

  async continueWithoutCustomerData(): Promise<void> {
    await this.continueButton.click();
  }

  async expectValidationError(messagePart: string): Promise<void> {
    await expect(this.errorMessage).toContainText(messagePart);
  }

  async expectOverviewStep(): Promise<void> {
    await expect(this.title).toHaveText("Checkout: Overview");
    await expect(this.finishButton).toBeVisible();
  }

  async finishOrder(): Promise<void> {
    await this.finishButton.click();
  }
}
