import type { CreateUserPayload } from "../api/reqres-client.js";

export const userListExamples = [
  {
    label: "first page",
    page: 1,
    expectedTotal: 12
  },
  {
    label: "second page",
    page: 2,
    expectedTotal: 12,
    expectedLeadingLastNames: ["Lawson", "Ferguson"]
  }
] as const;

export const createUserExamples: CreateUserPayload[] = [
  {
    name: "morpheus",
    job: "leader"
  },
  {
    name: "trinity",
    job: "operator"
  }
];
