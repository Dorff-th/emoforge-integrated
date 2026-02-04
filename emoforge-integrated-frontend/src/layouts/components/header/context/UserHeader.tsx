import { AppHeader } from "@/layouts/components/header/AppHeader";

export const UserHeader = () => {
  return (
    <AppHeader
      left={<div>EmoForge</div>}
      center={<nav>Diary · Posts</nav>}
      right={<button>Logout</button>}
    />
  );
};
