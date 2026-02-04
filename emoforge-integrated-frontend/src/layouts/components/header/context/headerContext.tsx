import React, { createContext, useContext } from "react";

export type HeaderContextType = "PUBLIC" | "POST" | "USER" | "DIARY";

const HeaderContext = createContext<HeaderContextType | null>(null);

interface HeaderContextProviderProps {
  value: HeaderContextType;
  children: React.ReactNode;
}

export function HeaderContextProvider({
  value,
  children,
}: HeaderContextProviderProps) {
  return (
    <HeaderContext.Provider value={value}>{children}</HeaderContext.Provider>
  );
}

/**
 * AppHeader 같은 곳에서 현재 컨텍스트를 읽을 때 사용.
 * Provider 없이 호출되면 빠르게 터뜨려서(개발 중) 실수 잡기.
 */
export function useHeaderContext(): HeaderContextType {
  const ctx = useContext(HeaderContext);
  if (!ctx) {
    throw new Error(
      "useHeaderContext must be used within HeaderContextProvider.",
    );
  }
  return ctx;
}
