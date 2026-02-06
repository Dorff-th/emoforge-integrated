// shared/utils/resolveAttachmentUrl.ts
// shared/utils/resolveAttachmentUrl.ts
export const resolveAttachmentUrl = (publicUrl?: string | null) => {
  if (!publicUrl) return null;

  const base = import.meta.env.VITE_API_BASE_URL; // https://www.emoforge.dev
  const attachPrefix = "/api/attach";

  // publicUrl: "/upload/xxx.png" or "upload/xxx.png"
  const normalizedPath = publicUrl.startsWith("/")
    ? publicUrl
    : `/${publicUrl}`;

  return `${base}${attachPrefix}${normalizedPath}`;
};
