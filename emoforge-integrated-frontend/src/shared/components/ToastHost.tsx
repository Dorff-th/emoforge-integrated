import { useToastStore } from "@/shared/stores/toastStore";

export function ToastHost() {
  const { toasts, remove } = useToastStore();

  return (
    <div className="fixed top-4 right-4 z-50 space-y-2">
      {toasts.map((toast) => (
        <div
          key={toast.id}
          className={`rounded px-4 py-3 shadow-md text-sm
            ${
              toast.type === "error"
                ? "bg-red-500 text-white"
                : toast.type === "success"
                  ? "bg-green-500 text-white"
                  : "bg-slate-800 text-white"
            }
          `}
          onClick={() => remove(toast.id)}
        >
          {toast.message}
        </div>
      ))}
    </div>
  );
}
