import type { Toast } from "@/shared/stores/toast.types";
import { useToastStore } from "@/shared/stores/toastStore";

const toastStyleMap: Record<
  Toast["type"],
  {
    bg: string;
    border: string;
    text: string;
    progress: string;
  }
> = {
  success: {
    bg: "bg-emerald-500/90",
    border: "border-emerald-600",
    text: "text-white",
    progress: "bg-emerald-200",
  },
  error: {
    bg: "bg-rose-500/90",
    border: "border-rose-600",
    text: "text-white",
    progress: "bg-rose-200",
  },
  info: {
    bg: "bg-sky-500/90",
    border: "border-sky-600",
    text: "text-white",
    progress: "bg-sky-200",
  },
};

export function ToastItem({ toast }: { toast: Toast }) {
  const { pause, resume, remove } = useToastStore();
  const style = toastStyleMap[toast.type];

  const progress = (toast.remaining / toast.duration) * 100;

  return (
    <div
      onMouseEnter={() => pause(toast.id)}
      onMouseLeave={() => resume(toast.id)}
      className={`
        relative w-80 rounded-lg border
        ${style.bg} ${style.border} ${style.text}
        p-4 shadow-lg backdrop-blur-sm
      `}
    >
      <div className="pr-6 font-medium">{toast.message}</div>

      {/* Progress bar */}
      <div className="absolute bottom-0 left-0 h-1 w-full bg-white/20">
        <div
          className={`h-full ${style.progress}`}
          style={{ width: `${progress}%` }}
        />
      </div>

      {/* Close */}
      <button
        onClick={() => remove(toast.id)}
        className="absolute top-2 right-2 text-white/70 hover:text-white"
      >
        ✕
      </button>
    </div>
  );
}
