# Feature name

Post category filter

# Files modified

- emoforge-integrated-backend/module-post/src/main/java/dev/emoforge/post/controller/PostController.java
- emoforge-integrated-backend/module-post/src/main/java/dev/emoforge/post/service/query/PostQueryService.java
- emoforge-integrated-backend/module-post/src/main/java/dev/emoforge/post/repository/PostRepository.java
- emoforge-integrated-frontend/src/features/post/api/postApi.ts
- emoforge-integrated-frontend/src/features/post/components/CategoryFilter.tsx
- emoforge-integrated-frontend/src/features/post/pages/PostListPage.tsx

# Summary of changes

- Added optional `categoryId` filtering to the existing `GET /api/posts` endpoint.
- Added a category chip filter UI above the post list while preserving the existing page layout.
- Loaded categories from the existing category API and refreshed the post list when the selected category changes.
- Kept tag UI out of scope for this change.
