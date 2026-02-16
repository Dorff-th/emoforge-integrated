export const resolveAttachmentUrl = (publicUrl?: string | null) => {
  if (!publicUrl) return null;

  const base = import.meta.env.VITE_API_BASE_URL; // https://www.emoforge.dev
  //const attachPrefix = "/api/attach";

  // publicUrl: "/upload/xxx.png" or "upload/xxx.png"
  const normalizedPath = publicUrl.startsWith("/")
    ? publicUrl
    : `/${publicUrl}`;

  //return `${base}${attachPrefix}${normalizedPath}`; //  이미지 publicUrl에 백엔드도메인/db의 public_url  이런형태로 노출하기 위해 attachPrefix제거
  return `${base}${normalizedPath}`;
};
