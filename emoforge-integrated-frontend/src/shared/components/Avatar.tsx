// shared/components/Avatar.tsx
import { resolveAttachmentUrl } from "../utils/resolveAttachmentUrl";

interface AvatarProps {
  publicUrl?: string | null;
  name?: string;
  size?: number;
  className?: string;
}

export function Avatar({ publicUrl, name, size = 32 }: AvatarProps) {
  const char = name?.trim()?.[0] ?? "?";

  // 🔥 blob URL은 그대로 사용
  const resolvedUrl = publicUrl?.startsWith("blob:")
    ? publicUrl
    : resolveAttachmentUrl(publicUrl);

  if (resolvedUrl) {
    return (
      <img
        src={resolvedUrl}
        alt={name}
        style={{ width: size, height: size }}
        className="rounded-full object-cover"
      />
    );
  }

  return (
    <div
      style={{ width: size, height: size }}
      className="
         flex items-center justify-center
  rounded-full
  bg-[var(--avatar-bg)]
  text-[var(--text)]
  font-semibold
      "
    >
      {char}
    </div>
  );
}
