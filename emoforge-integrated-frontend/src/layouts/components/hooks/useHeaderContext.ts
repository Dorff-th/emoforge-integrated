import { useLocation } from "react-router-dom";

export type HeaderContext =
  | "PUBLIC"
  | "POST"
  | "USER"
  | "DIARY";


export function useHeaderContext(): HeaderContext {
  const { pathname } = useLocation();

  //if (pathname.startsWith("/admin")) return "ADMIN";
  if (pathname.startsWith("/user/diary")) return "DIARY";
  if (pathname.startsWith("/user")) return "USER";
  if (pathname.startsWith("/posts")) return "POST";

  return "PUBLIC";
}
