import { expect } from "@playwright/test";

export type ReqresUser = {
  id: number;
  email: string;
  first_name: string;
  last_name: string;
  avatar: string;
};

export type ReqresListUsersResponse = {
  page: number;
  per_page: number;
  total: number;
  total_pages: number;
  data: ReqresUser[];
  support?: {
    url: string;
    text: string;
  };
};

export type ReqresCreateUserResponse = {
  name: string;
  job: string;
  id: string;
  createdAt: string;
};

export type ReqresErrorResponse = {
  error: string;
  message?: string;
};

export function assertListUsersResponse(
  body: unknown,
  expectedPage: number
): asserts body is ReqresListUsersResponse {
  expect(body).toEqual(
    expect.objectContaining({
      page: expectedPage,
      per_page: expect.any(Number),
      total: expect.any(Number),
      total_pages: expect.any(Number),
      data: expect.any(Array)
    })
  );

  const listBody = body as ReqresListUsersResponse;
  expect(listBody.data.length).toBeGreaterThan(0);

  for (const user of listBody.data) {
    expect(user).toEqual(
      expect.objectContaining({
        id: expect.any(Number),
        email: expect.stringMatching(/@reqres\.in$/),
        first_name: expect.any(String),
        last_name: expect.any(String),
        avatar: expect.stringMatching(/^https?:\/\//)
      })
    );
  }
}

export function assertCreateUserResponse(
  body: unknown,
  expected: { name: string; job: string }
): asserts body is ReqresCreateUserResponse {
  expect(body).toEqual(
    expect.objectContaining({
      name: expected.name,
      job: expected.job,
      id: expect.any(String),
      createdAt: expect.stringMatching(
        /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}/
      )
    })
  );
}

export function assertReqresError(
  body: unknown,
  expectedError: string
): asserts body is ReqresErrorResponse {
  expect(body).toEqual(
    expect.objectContaining({
      error: expectedError
    })
  );
}
