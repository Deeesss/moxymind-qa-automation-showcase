import { existsSync } from "node:fs";
import { loadEnvFile } from "node:process";

if (existsSync(".env")) {
  loadEnvFile(".env");
}

const apiKey = process.env.REQRES_API_KEY?.trim();

if (!apiKey) {
  console.error(
    [
      "REQRES_API_KEY is required for the full API suite.",
      "Create a free ReqRes key at https://app.reqres.in/api-keys and export it before running CI or test:api.",
      "The unauthenticated negative boundary is covered in tests, but the main API acceptance path must use a real key."
    ].join("\n")
  );
  process.exit(1);
}
