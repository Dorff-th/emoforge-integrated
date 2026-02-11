import type { Tag } from './Tag';

// src/types/PostRequest.ts
export interface PostRequest {
  title: string;
  categoryId: number;
  content: string;
  tags: Tag[]; // ["spring","gpt"]
  deleteTagIds?: number[]; // used only in edit mode
  attachmentIds?: number[]; // attachment identifiers returned after upload
}

