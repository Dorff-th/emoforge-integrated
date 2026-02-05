import type { LucideIcon } from "lucide-react";
import {
  HEADER_MENU_ITEM_BASE,
  HEADER_ICON_SIZE,
  HEADER_HOVER_TRANSITION,
  HEADER_HOVER_BG,
} from "@/layouts/components/header/header.constants";
import { cn } from "@/shared/utils/cn";

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
      className={cn(
        HEADER_MENU_ITEM_BASE,
        HEADER_HOVER_TRANSITION,
        HEADER_HOVER_BG,
        variant === "danger" ? "text-red-600" : "",
      )}
    >
      <Icon size={HEADER_ICON_SIZE} />
      {label}
    </button>
  );
}
