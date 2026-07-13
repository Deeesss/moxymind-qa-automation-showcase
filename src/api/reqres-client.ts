import type { APIRequestContext, APIResponse } from "@playwright/test";
import { reqresConfig, requireSecret, type ReqresConfig } from "../config/env.js";

export type CreateUserPayload = {
  name: string;
  job: string;
};

export class ReqresClient {
  private readonly baseUrl: string;
  private readonly apiKey: string | undefined;

  constructor(
    private readonly request: APIRequestContext,
    config: ReqresConfig = reqresConfig
  ) {
    this.baseUrl = config.baseUrl.replace(/\/$/, "");
    this.apiKey = config.apiKey;
  }

  async listUsers(page: number): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/users`, {
      headers: this.authHeaders(),
      params: { page }
    });
  }

  async getUser(userId: number): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/users/${userId}`, {
      headers: this.authHeaders()
    });
  }

  async createUser(payload: CreateUserPayload): Promise<APIResponse> {
    return this.request.post(`${this.baseUrl}/api/users`, {
      headers: {
        ...this.authHeaders(),
        "content-type": "application/json"
      },
      data: payload
    });
  }

  async getUserWithoutApiKey(userId: number): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/users/${userId}`);
  }

  private authHeaders(): Record<string, string> {
    return {
      "x-api-key": requireSecret("REQRES_API_KEY", this.apiKey),
      "x-reqres-env": process.env.REQRES_ENV ?? "prod",
      "user-agent": "moxymind-qa-automation-showcase/1.0"
    };
  }
}
