import ProfileHeader from "@/features/user/components/profile/ProfileHeader";
import ProfileImageSection from "@/features/user/components/profile/ProfileImageSection";
import ProfileInfoSection from "@/features/user/components/profile/ProfileInfoSection";
import ProfileStatsSection from "@/features/user/components/profile/ProfileStatsSection";
import ProfileWithdrawalSection from "../components/profile/ProfileWithdrawalSection";

export default function ProfilePage() {
  return (
    <div className="w-full max-w-lg rounded-xl mx-auto bg-white p-8 shadow-lg">
      <div className="bg-white rounded-2xl shadow-sm p-6">
        <ProfileHeader />
        <ProfileImageSection />
        <ProfileInfoSection />
        <ProfileStatsSection />
        <ProfileWithdrawalSection />
      </div>
    </div>
  );
}
