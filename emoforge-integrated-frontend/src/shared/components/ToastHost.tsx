import { useToastStore } from "@/shared/stores/toastStore";
import { ToastItem } from "./ToastItem";

export function ToastHost() {
  const toasts = useToastStore((s) => s.toasts);

  return (
    <div className="fixed top-4 right-4  flex flex-col gap-2 z-[9999]">
      {toasts.map((toast) => (
        <ToastItem key={toast.id} toast={toast} />
      ))}
    </div>
  );
}
