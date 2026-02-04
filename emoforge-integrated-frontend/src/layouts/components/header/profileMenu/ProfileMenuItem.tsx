interface Props {
  label: string;
  onClick: () => void;
  variant?: "default" | "danger";
}

export function ProfileMenuItem({
  label,
  onClick,
  variant = "default",
}: Props) {
  return (
    <button
      onClick={onClick}
      className={`w-full px-4 py-2 text-left text-sm hover:bg-neutral-100
        ${variant === "danger" ? "text-red-600" : ""}`}
    >
      {label}
    </button>
  );
}
