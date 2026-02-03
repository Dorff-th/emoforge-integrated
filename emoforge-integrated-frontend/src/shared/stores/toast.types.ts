export type ToastType = "success" | "error" | "info";
export type ToastId = string;

export interface Toast {
  id: ToastId;
  type: ToastType;
  message: string;

  duration: number;   // ✅ 상태는 항상 number
  createdAt: number;
  remaining: number;
  paused: boolean;
}

// ✅ add()로 들어오는 입력은 duration optional
export type ToastPayload = {
  type: ToastType;
  message: string;
  duration?: number;
};

