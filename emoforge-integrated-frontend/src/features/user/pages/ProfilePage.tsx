import ProfileHeader from "@/features/user/components/profile/ProfileHeader";
import ProfileImageSection from "@/features/user/components/profile/ProfileImageSection";
import ProfileInfoSection from "@/features/user/components/profile/ProfileInfoSection";
import ProfileStatsSection from "@/features/user/components/profile/ProfileStatsSection";

export default function ProfilePage() {
  return (
    <div className="w-full max-w-5xl mx-auto px-4 py-6 space-y-8">
      <ProfileHeader />

      <ProfileImageSection />

      <ProfileInfoSection />

      <ProfileStatsSection />
    </div>
  );
}
