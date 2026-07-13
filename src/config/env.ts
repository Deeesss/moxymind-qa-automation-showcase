export type ReqresConfig = {
  baseUrl: string;
  apiKey: string | undefined;
  responseTimeLimitMs: number;
};

export type SauceDemoConfig = {
  baseUrl: string;
  standardUser: string;
  lockedUser: string;
  password: string;
};

export const reqresConfig: ReqresConfig = {
  baseUrl: process.env.REQRES_BASE_URL ?? "https://reqres.in",
  apiKey: process.env.REQRES_API_KEY,
  responseTimeLimitMs: positiveNumberFromEnv("API_RESPONSE_TIME_LIMIT_MS", 2_000)
};

export const sauceDemoConfig: SauceDemoConfig = {
  baseUrl: process.env.SAUCEDEMO_BASE_URL ?? "https://www.saucedemo.com",
  standardUser: process.env.SAUCEDEMO_STANDARD_USER ?? "standard_user",
  lockedUser: process.env.SAUCEDEMO_LOCKED_USER ?? "locked_out_user",
  password: process.env.SAUCEDEMO_PASSWORD ?? "secret_sauce"
};

export function requireSecret(name: string, value: string | undefined): string {
  const trimmed = value?.trim();

  if (!trimmed) {
    throw new Error(`${name} is required. See README.md and .env.example.`);
  }

  return trimmed;
}

function positiveNumberFromEnv(name: string, fallback: number): number {
  const rawValue = process.env[name]?.trim();

  if (!rawValue) {
    return fallback;
  }

  const parsedValue = Number(rawValue);

  if (!Number.isFinite(parsedValue) || parsedValue <= 0) {
    throw new Error(`${name} must be a positive number.`);
  }

  return parsedValue;
}
