import { useEffect, useRef } from "react";

type ModalSize = "sm" | "md" | "lg";

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  size?: ModalSize;
  children: React.ReactNode;
}

const sizeClassMap: Record<ModalSize, string> = {
  sm: "max-w-sm",
  md: "max-w-md",
  lg: "max-w-lg",
};

export function Modal({
  isOpen,
  onClose,
  title,
  size = "md",
  children,
}: ModalProps) {
  const modalRef = useRef<HTMLDivElement>(null);

  // ESC key
  useEffect(() => {
    if (!isOpen) return;

    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === "Escape") {
        onClose();
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [isOpen, onClose]);

  // 초기 focus
  useEffect(() => {
    if (isOpen) {
      modalRef.current?.focus();
    }
  }, [isOpen]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* backdrop */}
      <div
        className="absolute inset-0 bg-black/50 transition-opacity animate-fadeIn"
        onClick={onClose}
      />

      {/* modal */}
      <div
        ref={modalRef}
        tabIndex={-1}
        className={`
          relative z-10 w-full ${sizeClassMap[size]}
          rounded-lg bg-white p-6 shadow-xl
          outline-none
          animate-scaleIn
          dark:bg-neutral-800
        `}
        onClick={(e) => e.stopPropagation()}
      >
        {title && <h2 className="mb-4 text-lg font-semibold">{title}</h2>}

        {children}
      </div>
    </div>
  );
}
