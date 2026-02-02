// scripts/gen-missing-pages.js
import fs from "fs";
import path from "path";

const ROOT = path.resolve("src");

const pages = [
  // 🔽 auth (이미 있는 것들은 안 적음)
  // LoginPage
  // OAuthCallbackPage
  // TermsAgreementPage

  // 🔽 user
  "features/user/pages/ProfilePage.tsx",

  // 🔽 post
  "features/post/pages/PostListPage.tsx",
  "features/post/pages/PostDetailPage.tsx",
  "features/post/pages/PostWritePage.tsx",
  "features/post/pages/PostEditPage.tsx",

  // 🔽 diary
  "features/diary/pages/DiaryWritePage.tsx",
  "features/diary/pages/DiaryCalendarPage.tsx",
  "features/diary/pages/DiaryListPage.tsx",
  "features/diary/pages/DiaryInsightsPage.tsx",
  "features/diary/pages/DiarySearchPage.tsx",

  // 🔽 admin (아직 router엔 없지만 TO-BE 예정)
  "features/admin/pages/AdminDashboardPage.tsx",
  "features/admin/pages/AdminMembersPage.tsx",
  "features/admin/pages/AdminPostCategoryPage.tsx",
];

const template = (name) => `export default function ${name}() {
  return <div>${name} (TODO)</div>;
}
`;

pages.forEach((relativePath) => {
  const fullPath = path.join(ROOT, relativePath);

  if (fs.existsSync(fullPath)) {
    console.log("SKIP (exists):", relativePath);
    return;
  }

  fs.mkdirSync(path.dirname(fullPath), { recursive: true });

  const name = path.basename(relativePath, ".tsx");
  fs.writeFileSync(fullPath, template(name));
  console.log("CREATE:", relativePath);
});
