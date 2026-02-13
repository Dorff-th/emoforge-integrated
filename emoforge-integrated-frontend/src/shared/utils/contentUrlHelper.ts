// src/utils/contentUrlHelper.ts
import { backendBaseUrl } from '@/shared/config/config';


function escapeRegExp(value: string): string {
  return value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

/**
 * DB에 저장된 content를 Editor에서 보일 때 상대 경로를 절대 경로로 보정
 */
export function fixContentForEditor(content: string): string {
  if (!content) return content;

  const relativeImageRegex = /!\[(.*?)\]\((\/(?!\/)[^)]*)\)/g;

  return content.replace(relativeImageRegex, (_match, alt, path) => {
    return `![${alt}](${backendBaseUrl}${path})`;
  });
}

/**
 * Editor에서 작성한 content를 DB에 저장할 때 절대 경로를 상대 경로로 환원
 */
export function fixContentForSave(content: string): string {
  if (!content) return content;

  const escapedBase = escapeRegExp(backendBaseUrl);
  const absoluteImageRegex = new RegExp(`!\\[(.*?)\\]\\(${escapedBase}(\\/(?!\\/)[^)]*)\\)`, 'g');

  return content.replace(absoluteImageRegex, (_match, alt, path) => {
    return `![${alt}](${path})`;
  });
}
