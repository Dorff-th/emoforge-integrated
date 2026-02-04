import type { ReactNode } from "react";

type AppHeaderProps = {
  left?: ReactNode;
  center?: ReactNode;
  right?: ReactNode;
};

export const AppHeader = ({ left, center, right }: AppHeaderProps) => {
  return (
    <header className="h-14 border-b flex items-center px-6">
      <div className="flex-1">{left}</div>
      <div className="flex-1 text-center">{center}</div>
      <div className="flex-1 flex justify-end">{right}</div>
    </header>
  );
};
