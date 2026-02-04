import { type ReactNode } from "react";
import { useLoadingStore } from "@/shared/stores/loadingStore";

type SectionLoadingProps = {
  scope: string;
  children?: ReactNode;
  message?: string;
};

export function SectionLoading({
  scope,
  children,
  message = "불러오는 중...",
}: SectionLoadingProps) {
  const isLoading = useLoadingStore((s) => s.isLoading(scope));

  if (isLoading) {
    return (
      <div className="flex min-h-[120px] items-center justify-center rounded-lg bg-slate-50">
        <div className="flex flex-col items-center gap-3">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-t-transparent border-slate-400" />
          <p className="text-sm text-slate-500">{message}</p>
        </div>
      </div>
    );
  }

  return <>{children}</>;
}
