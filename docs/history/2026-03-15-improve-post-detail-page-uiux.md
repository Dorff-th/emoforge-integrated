# Feature Name
Improve PostDetailPage UI/UX

# Files Modified
- emoforge-integrated-frontend/src/features/post/pages/PostDetailPage.tsx
- emoforge-integrated-frontend/src/features/post/components/UserComment.tsx

# Summary of Changes
- Reworked the post detail page into a blog-style article layout with breadcrumb navigation, stronger title hierarchy, muted metadata, badge-style tags, a clearer attachments section, and split action buttons.
- Added previous/next post cards above comments by deriving adjacent posts from the existing category post list API without changing backend contracts.
- Refined the comment area with a cleaner composer, avatar-based comment cards, clearer author/date presentation, and updated destructive actions.
- Reduced vertical whitespace by wrapping the content viewer in a lighter card container, tightening the previous/next navigation section, and shrinking the empty comment state padding.
- Aligned the detail page styling with the Emoforge design system by increasing border contrast, updating the content and attachment cards, tightening previous/next cards, and standardizing divider and empty-state treatments.
- Extracted the previous/next post UI into a dedicated `PostNavigation` component so `PostDetailPage` only passes adjacent post data and category context.
- Fixed stale post content during previous/next navigation by remounting the Toast UI viewer with `key={post.id}` so each route change reflects the current post body immediately.
