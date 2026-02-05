import type { LucideIcon } from "lucide-react";

interface Props {
  label: string;
  onClick: () => void;
  variant?: "default" | "danger";
  icon: LucideIcon;
}

export function ProfileMenuItem({
  label,
  onClick,
  variant = "default",
  icon: Icon,
}: Props) {
  return (
    <button
      onClick={onClick}
      className={`w-full px-4 py-2 text-left text-sm hover:bg-neutral-100 flex items-center gap-1.5
        ${variant === "danger" ? "text-red-600" : ""}`}
    >
      <Icon size={16} />
      {label}
    </button>
  );
}
