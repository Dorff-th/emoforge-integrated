// shared/utils/generateId.ts
export const generateId = () => {
  if (typeof crypto !== 'undefined' && crypto.randomUUID) {
    return crypto.randomUUID();
  }

  // fallback (browser-safe)
  return `${Date.now()}-${Math.random().toString(36).slice(2, 9)}`;
};
