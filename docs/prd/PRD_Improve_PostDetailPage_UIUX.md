# PRD: Improve PostDetailPage UI/UX

Project: Emoforge
Page: PostDetailPage

---

# Background

The current PostDetailPage works functionally but lacks modern blog UX.

Goal is to improve readability, navigation, and layout hierarchy.

---

# Goals

Improve the PostDetailPage to feel closer to a modern developer blog.

Focus areas:

- navigation context
- visual hierarchy
- readability
- comment clarity

---

# Improvements

## 1. Add Breadcrumb Navigation

Add breadcrumb above the title.

Example:

Posts / Category / Post Title

---

## 2. Improve Title Hierarchy

Title should be visually dominant.

Tailwind example:

text-3xl font-bold

---

## 3. Improve Metadata Layout

Current:

Category · Author · Date

Improve styling:

- smaller font
- muted color

Example:

text-gray-500 text-sm

---

## 4. Improve Content Area

Current content card looks like an editor.

Change to article style:

- remove heavy border
- increase line-height
- max-width for readability

Example:

max-w-3xl mx-auto leading-relaxed

---

## 5. Improve Tags

Display tags as badges.

Example:

bg-gray-100 px-2 py-1 rounded text-sm

---

## 6. Improve Attachments Section

Add clear section label.

Example:

Attachments

List files with icon and size.

---

## 7. Improve Action Buttons

Layout:

Left side:

[List]

Right side:

[Edit] [Delete]

Delete should use destructive styling.

---

## 8. Improve Comment UI

Each comment should show:

Avatar
Author
Date
Content

Use flex layout for better readability.

---

## 9. Add Post Navigation

Add previous / next post navigation.

Location: above comments.

Example:

← Previous Post

Next Post →

---

# Non Goals

Do not modify backend APIs.

This PRD focuses on frontend layout and UI/UX only.