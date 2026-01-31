// auth.types.ts
export interface AuthUser {
  uuid: string;
  username: string;
  role: "USER" | "ADMIN";
}

export type AuthErrorCode =
  | "inactive"
  | "deleted"
  | "unauthorized";
